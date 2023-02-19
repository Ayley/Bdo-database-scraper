package me.kleidukos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.kleidukos.object.Item;
import me.kleidukos.object.ItemStats;
import me.kleidukos.util.Converter;
import me.kleidukos.util.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDOCodexScrapper {
    private final String[] locals = new String[]{"de", "us", "fr", "ru", "es", "sp", "pt", "jp", "kr", "cn", "tw", "th", "tr", "id"};

    public BDOCodexScrapper() throws IOException, InterruptedException {
            var itemsFile = Jsoup.connect(getItemsUrl("gl")).get().html();
            var items = loadItems(itemsFile);

            /*for (var itemLocal : locals) {
                updateItems(items, itemLocal);
            }*/

        updateItems(items, locals[0]);
    }

    private void updateItems(List<Item> items, String local) throws InterruptedException, IOException {

        //for (var item : items) {
            var item = items.get(0);

            var itemFile = Jsoup.connect(getItemUrl(item.getId(), local)).get();

            var name = itemFile.select("div[id=item_name]").text().trim();
            var itemType = File.getItemTypeRegex(itemFile.html()).trim();

            var itemStats = getItemStats(itemFile);

            var eDescription = itemFile.select("div[id=edescription]").text().trim();
            var durability = Float.valueOf(itemFile.select("span[id=durability]").text().split("/")[0].trim());
            var weight = Float.valueOf(File.getWeightRegex(itemFile.html()).trim());
            var bound = Jsoup.parse(File.getBoundContentRegex(itemFile.html())).text();

            item.setStats(itemStats);
            item.addName(local, name);

            if(item.getDetail() == null){
                //Create item details
                item.createDetails(weight, durability);
            }

            item.getDetail().addType(local, itemType);
            item.getDetail().addEDescription(local, eDescription);
            item.getDetail().addBound(local, bound);

            System.out.println("Item with id: " + item.getId() + " and name: " + item.getName() + " updated in language: " + local);

            Thread.sleep(5000);
        //}

    }

    private ItemStats getItemStats(Document itemFile) {
        var attack = itemFile.select("span[id=damage]").text().trim();
        var defense = itemFile.select("span[id=defense]").text().trim();
        var accuracy = itemFile.select("span[id=accuracy]").text().trim();
        var evasion = itemFile.select("span[id=evasion]").text().trim() + itemFile.select("span[id=hevasion]").text().trim();
        var dreduction = itemFile.select("span[id=dreduction]").text().trim() + itemFile.select("span[id=hdreduction]").text().trim();


        return new ItemStats(attack, defense, accuracy, evasion, dreduction);
    }

    private List<Item> loadItems(String itemsFile) {
        var items = new ArrayList<Item>();
        var type = new TypeToken<Map<String, List<List<Object>>>>() {}.getType();
        Map<String, List<List<Object>>> data = new Gson().fromJson(itemsFile, type);

        for (var item : data.get("aaData")) {
            var id = Converter.objectToInt(item.get(0));
            var grade = Converter.objectToInt(item.get(5));
            var level = Converter.objectToInt(item.get(3));

            items.add(new Item(id, new HashMap<>(), grade, level));
        }

        return items;
    }

    public String getItemUrl(int id, String local) {
        return "https://bdocodex.com/tip.php?id=item--" + id + "&enchant=0&l=" + local + "&nf=off";
    }

    public String getItemsUrl(String local) {
        return "https://bdocodex.com/query.php?a=items&l=" + local + "&nl=off";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new BDOCodexScrapper();
    }

}
