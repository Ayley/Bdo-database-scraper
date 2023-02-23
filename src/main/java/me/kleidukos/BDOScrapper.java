package me.kleidukos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.kleidukos.object.callback.UpdateCallback;
import me.kleidukos.object.item.*;
import me.kleidukos.tables.ItemTable;
import me.kleidukos.util.Converter;
import me.kleidukos.util.DatabaseApi;
import me.kleidukos.util.ScrapperApi;
import me.kleidukos.util.ScrapperRegex;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public final class BDOScrapper implements ScrapperApi {

    private final String[] locals = new String[]
            {"de", "us", "fr", "ru", "es", "sp", "pt", "jp", "kr",
                    "cn", "tw", "th", "tr", "id"};

    private final DatabaseApi databaseApi;
    private final ItemTable itemTable;

    public BDOScrapper(DatabaseApi databaseApi, ItemTable itemTable) {
        this.databaseApi = databaseApi;
        this.itemTable = itemTable;
    }

    @Override
    public CompletableFuture<UpdateCallback> updateCompleteDatabase() {
        return CompletableFuture.supplyAsync(() -> {

            List<BaseItem> items = null;

            for (var local : locals) {
                List<BaseItem> tempItems = null;
                try {
                    tempItems = parseAllBaseItems(local);
                } catch (Exception e) {
                    continue;
                }

                if (items == null) {
                    items = tempItems;
                    continue;
                }

                for (var i : items) {
                    var it = tempItems.stream().filter(tp -> tp.getId() == i.getId()).findFirst();

                    it.ifPresent(i::merge);
                }
            }

            insertOrUpdateItemsIntoDatabase(items);

            return new UpdateCallback(items, locals);
        });
    }

    @Override
    public CompletableFuture<UpdateCallback> updateDatabase(String local) {
        return CompletableFuture.supplyAsync(() -> {

            List<BaseItem> items;
            try {
                items = parseAllBaseItems(local);

                insertOrUpdateItemsIntoDatabase(items);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return new UpdateCallback(items, new String[]{local});
        });
    }

    @Override
    public CompletableFuture<DetailedItem> upgradeBaseItem(BaseItem item, boolean tryGetFromDB) {
        return CompletableFuture.supplyAsync(() -> {
            var dbItem = databaseApi.getDetailedItemById(item.getId()).join();

            if (dbItem.isPresent())
                return dbItem.get();

            try {
                var detailedItem = upgradeItem(item);
                insertOrUpdateDetailedItemIntoDatabase(detailedItem);
                return detailedItem;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public CompletableFuture<DetailedItem> addLocalToDetailedItem(DetailedItem item, String local) {
        return CompletableFuture.supplyAsync(() -> {
            DetailedItem localItem = null;
            try {
                localItem = getLocalDetailItem(item, local);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            item.merge(localItem);

            return item;
        });
    }

    ////////////////////////////////////////////////////////////////////////

    private void insertOrUpdateItemsIntoDatabase(List<BaseItem> items) {
        for (var item : items) {
            //databaseApi.getBaseItemById(item.getId()).thenAccept(dbItem -> {
            //    dbItem.ifPresent(item::merge);
               itemTable.insertBaseItem(item);
            //});
        }
    }

    private void insertOrUpdateDetailedItemIntoDatabase(DetailedItem item) {
        databaseApi.getDetailedItemById(item.getId()).thenAccept(dbItem -> {
           dbItem.ifPresent(item::merge);

           //todo insert
        });
    }

    ////////////////////////////////////////////////////////////////////////

    private List<BaseItem> parseAllBaseItems(String local) throws IOException {
        System.out.println(local);
        var list = new ArrayList<BaseItem>();

        try {
            for (var data : getRawItemsData(local)) {
                var item = parseBaseItem(data, local);

                list.add(item);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    //Parse raw item data to base item
    private BaseItem parseBaseItem(List<Object> data, String local) {
        var itemData = (String) data.get(2) + data.get(3);

        var id = Converter.objectToInt(data.get(0));
        var grade = Converter.objectToInt(data.get(5));
        var level = Converter.objectToInt(data.get(3));
        var name = ScrapperRegex.getNameRegex(itemData).trim();

        var item = new BaseItem(id, grade, level);
        item.addName(local, name);

        return item;
    }

    //Get raw basic data
    private List<List<Object>> getRawItemsData(String local) throws IOException, InterruptedException {
        var content = getContent(getItemsUrl(local));
        var typeToken = new TypeToken<Map<String, List<List<Object>>>>() {
        }.getType();
        Map<String, List<List<Object>>> data = new Gson().fromJson(content, typeToken);

        return data.get("aaData");
    }

    private DetailedItem upgradeItem(BaseItem item) throws IOException, InterruptedException {
        var detailedItem = new DetailedItem(item);

        for (var local : item.getLocals()) {
            var content = Jsoup.connect(getItemUrl(item.getId(), local)).get();

            var name = content.select("div[id=item_name]").text().trim();

            var stats = getItemProperties(content);

            var weight = Converter.stringToFloat(ScrapperRegex.getWeightRegex(content.html()));
            var volume = Converter.stringToFloat(ScrapperRegex.getVolumeRegex(content.html()));

            detailedItem.getMetadata().setDetails(new ItemInfo(weight, volume));

            detailedItem.getMetadata().getDetails().addInfo(local, getItemInfos(content));

            detailedItem.getMetadata().setProperties(stats);


            detailedItem.addName(local, name);
        }

        return detailedItem;
    }

    private DetailedItem getLocalDetailItem(DetailedItem item, String local) throws IOException {
        var content = Jsoup.connect(getItemUrl(item.getId(), local)).get();

        var detailedItem = new DetailedItem(item);

        var name = content.select("div[id=item_name]").text().trim();

        var stats = getItemProperties(content);

        var weight = Converter.stringToFloat(ScrapperRegex.getWeightRegex(content.html()));
        var volume = Converter.stringToFloat(ScrapperRegex.getVolumeRegex(content.html()));

        detailedItem.getMetadata().setDetails(new ItemInfo(weight, volume));

        detailedItem.getMetadata().getDetails().addInfo(local, getItemInfos(content));

        detailedItem.getMetadata().setProperties(stats);


        detailedItem.addName(local, name);
        return detailedItem;
    }

    //Get the item properties
    private ItemProperties getItemProperties(Document document) {
        var attack = document.select("span[id=damage]").text().trim().split("~");
        var defense = document.select("span[id=defense]").text().trim();
        var accuracy = document.select("span[id=accuracy]").text().trim();
        var evasion = document.select("span[id=evasion]").text().trim();
        var hiddenEvasion = document.select("span[id=hevasion]").text().trim();
        var dreduction = document.select("span[id=dreduction]").text().trim();
        var hiddendReduction = document.select("span[id=hdreduction]").text().trim();

        return new ItemProperties(Integer.parseInt(attack[0]), Integer.parseInt(attack[1]), Integer.parseInt(defense), Integer.parseInt(accuracy), Integer.parseInt(evasion), Integer.parseInt(hiddenEvasion.length() > 0 ? hiddenEvasion.substring(2, hiddenEvasion.length() - 1) : "0"), Integer.parseInt(dreduction), Integer.parseInt(hiddendReduction.length() > 0 ? hiddendReduction.substring(2, hiddendReduction.length() - 1) : "0"));
    }

    //Get item infos
    private ItemInfos getItemInfos(Document document) {
        var itemType = ScrapperRegex.getItemTypeRegex(document.html()).trim();
        var bound = Jsoup.parse(ScrapperRegex.getBoundContentRegex(document.html())).text().trim();
        var description = Jsoup.parse(ScrapperRegex.getDescriptionContentRegex(document.html())).text().trim();
        var effectDescription = document.select("div[id=edescription]").text().trim();
        return new ItemInfos(itemType, bound, description, effectDescription);
    }

    private String getContent(String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(url)).GET().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    //Item url for specific data
    private String getItemUrl(int id, String local) {
        return "https://bdocodex.com/tip.php?id=item--" + id + "&enchant=0&l=" + local + "&nf=off";
    }

    //Items url for default data of every item
    private String getItemsUrl(String local) {
        return "https://bdocodex.com/query.php?a=items&l=" + local;
    }
}
