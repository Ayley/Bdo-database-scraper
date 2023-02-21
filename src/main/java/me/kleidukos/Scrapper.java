package me.kleidukos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.kleidukos.object.*;
import me.kleidukos.util.Converter;
import me.kleidukos.util.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class Scrapper {

    public static List<BaseItem> loadBaseItems(String local) throws IOException, InterruptedException {
        var items = new ArrayList<BaseItem>();

        var localItems = getLocalItems(local);

        for (var item : localItems) {
            var itemData = (String) item.get(2) + item.get(3);

            var id = Converter.objectToInt(item.get(0));
            var grade = Converter.objectToInt(item.get(5));
            var level = Converter.objectToInt(item.get(3));
            var name = File.getNameRegex(itemData).trim();

            var baseItem = new BaseItem(id, grade, level);
            baseItem.addName(local, name);

            items.add(baseItem);
        }

        return items;
    }

    public static void updateItemsToLocal(List<BaseItem> items, String local) throws IOException, InterruptedException {
        var localItems = getLocalItems(local);

        for (var item : localItems) {
            var itemData = (String) item.get(2) + item.get(3);

            var id = Converter.objectToInt(item.get(0));
            var name = File.getNameRegex(itemData).trim();

            var baseItem = items.stream().filter(i -> i.getId() == id).findFirst();

            baseItem.ifPresent(value -> value.addName(local, name));
        }
    }

    public static void updateItemToLocal(BaseItem item, String local) throws IOException, InterruptedException {
        var localItems = getLocalItems(local);

        for (var localItem : localItems) {
            var id = Converter.objectToInt(localItem.get(0));

            if (item.getId() != id) {
                continue;
            }

            var itemData = (String) localItem.get(2) + localItem.get(3);

            var name = File.getNameRegex(itemData).trim();

            item.addName(local, name);
        }
    }

    public static void updateDetailedItemToLocal(DetailedItem item, String local) throws IOException, InterruptedException {
        var content = Jsoup.parse(getResponseContent(getItemUrl(item.getItem().getId(), local)));

        var infos = getItemInfos(content);

        var name = content.select("div[id=item_name]").text().trim();

        item.getItem().addName(local, name);
        item.getItemInfo().addInfo(local, infos);
    }

    private List<DetailedItem> upgradeItems(List<BaseItem> items, String local, int waitTimeForNext) throws InterruptedException, IOException {
        var detailedItems = new ArrayList<DetailedItem>();
        for (var item : items) {
            detailedItems.add(upgradeItem(item, local));

            Thread.sleep(waitTimeForNext);
        }

        return detailedItems;
    }

    public static DetailedItem upgradeItem(BaseItem item, String local) throws IOException, InterruptedException {
        var file = getResponseContent(getItemUrl(item.getId(), local));

        var content = Jsoup.parse(file);

        var name = content.select("div[id=item_name]").text().trim();

        var stats = getItemStats(content);
        var info = getItemInfo(content, local);

        var detailedItem = new DetailedItem(item);

        detailedItem.setItemInfo(info);
        detailedItem.setItemProperties(stats);


        if (!item.hasLocalName(local))
            detailedItem.getItem().addName(local, name);

        System.out.println("Item with id: " + item.getId() + " and name: " + item.getName(local) + " updated in language: " + local);

        return detailedItem;
    }

    private static List<List<Object>> getLocalItems(String local) throws IOException, InterruptedException {
        var content = getResponseContent(getItemsUrl(local));

        var typeToken = new TypeToken<Map<String, List<List<Object>>>>() {
        }.getType();
        Map<String, List<List<Object>>> data = new Gson().fromJson(content, typeToken);

        return data.get("aaData");
    }

    private static String getResponseContent(String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private static ItemProperties getItemStats(Document document) {
        var attack = document.select("span[id=damage]").text().trim().split("~");
        var defense = document.select("span[id=defense]").text().trim();
        var accuracy = document.select("span[id=accuracy]").text().trim();
        var evasion = document.select("span[id=evasion]").text().trim();
        var hiddenEvasion = document.select("span[id=hevasion]").text().trim();
        var dreduction = document.select("span[id=dreduction]").text().trim();
        var hiddendReduction = document.select("span[id=hdreduction]").text().trim();

        return new ItemProperties(Integer.parseInt(attack[0]), Integer.parseInt(attack[1]), Integer.parseInt(defense), Integer.parseInt(accuracy), Integer.parseInt(evasion), Integer.parseInt(hiddenEvasion.substring(2, hiddenEvasion.length() - 1)), Integer.parseInt(dreduction), Integer.parseInt(hiddendReduction.substring(2, hiddendReduction.length() - 1)));
    }

    private static ItemInfo getItemInfo(Document document, String local) {

        var weight = Converter.stringToFloat(File.getWeightRegex(document.html()));

        var item = new ItemInfo(weight);

        var infos = getItemInfos(document);

        item.addInfo(local, infos);

        return item;
    }

    private static ItemInfos getItemInfos(Document document) {
        var itemType = File.getItemTypeRegex(document.html()).trim();
        var bound = Jsoup.parse(File.getBoundContentRegex(document.html())).text().trim();
        var description = Jsoup.parse(File.getDescriptionContentRegex(document.html())).text().trim();
        var effectDescription = document.select("div[id=edescription]").text().trim();
        return new ItemInfos(itemType, bound, description, effectDescription);
    }

    public static String getItemUrl(int id, String local) {
        return "https://bdocodex.com/tip.php?id=item--" + id + "&enchant=0&l=" + local + "&nf=off";
    }

    public static String getItemsUrl(String local) {
        return "https://bdocodex.com/query.php?a=items&l=" + local;
    }

}
