package me.kleidukos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqLite;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.updater.SqlUpdater;
import me.kleidukos.object.Item;
import me.kleidukos.object.ItemStats;
import me.kleidukos.tables.ItemDetailTable;
import me.kleidukos.tables.ItemStatTable;
import me.kleidukos.tables.ItemTable;
import me.kleidukos.util.Converter;
import me.kleidukos.util.File;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BDOCodexScrapper {
    private final String[] locals = new String[]{"de", "us", "fr", "ru", "es", "sp", "pt", "jp", "kr", "cn", "tw", "th", "tr", "id"};

    private HikariDataSource dataSource;
    private ItemTable itemTable;
    private ItemStatTable itemStatTable;
    private ItemDetailTable itemDetailTable;

    public BDOCodexScrapper() throws IOException, InterruptedException, SQLException {
        setupDatabase();
        setupTable();

        var itemsFile = getResponseContent("gl");
        var items = loadItems(itemsFile);

        for (var itemLocal : locals) {
            updateItems(items, itemLocal);
        }

        System.out.println("Update items finished");
    }

    private void setupTable() {
        itemTable = new ItemTable(dataSource);
        itemStatTable = new ItemStatTable(dataSource);
        itemDetailTable = new ItemDetailTable(dataSource);
    }

    private void setupDatabase() throws IOException, SQLException {
        dataSource = DataSourceCreator.create(SqLite.get()).configure(conf -> conf.path(new java.io.File("items.sqlite"))).create().build();

        SqlUpdater.builder(dataSource, SqLite.get()).execute();
    }

    private void updateItems(List<Item> items, String local) throws InterruptedException, IOException {

        for (var item : items) {
            var file = getResponseContent(getItemUrl(item.getId(), local));

            var itemFile = Jsoup.parse(file);

            var name = itemFile.select("div[id=item_name]").text().trim();
            var itemType = File.getItemTypeRegex(itemFile.html()).trim();

            var itemStats = getItemStats(itemFile);

            var eDescription = itemFile.select("div[id=edescription]").text().trim();
            var durability = Converter.stringToFloat(itemFile.select("span[id=durability]").text().split("/")[0].trim());
            var weight = Float.valueOf(File.getWeightRegex(itemFile.html()).trim());
            var bound = Jsoup.parse(File.getBoundContentRegex(itemFile.html())).text().trim();
            var description = Jsoup.parse(File.getDescriptionContentRegex(itemFile.html())).text().trim();

            item.setStats(itemStats);
            item.addName(local, name);

            if (item.getDetail() == null) {
                item.createDetails(weight, durability);
            }

            item.getDetail().addType(local, itemType);
            item.getDetail().addEDescription(local, eDescription);
            item.getDetail().addBound(local, bound);
            item.getDetail().addDescription(local, description);

            System.out.println("Item with id: " + item.getId() + " and name: " + item.getName() + " updated in language: " + local);

            itemTable.insertOrUpdate(item);
            itemStatTable.insertOrUpdate(item);
            itemDetailTable.insertOrUpdate(item);

            Thread.sleep(5000);
        }

    }

    private ItemStats getItemStats(Document itemFile) {
        var attack = itemFile.select("span[id=damage]").text().trim();
        var defense = itemFile.select("span[id=defense]").text().trim();
        var accuracy = itemFile.select("span[id=accuracy]").text().trim();
        var evasion = itemFile.select("span[id=evasion]").text().trim() + itemFile.select("span[id=hevasion]").text().trim();
        var dreduction = itemFile.select("span[id=dreduction]").text().trim() + itemFile.select("span[id=hdreduction]").text().trim();


        return new ItemStats(attack, defense, accuracy, evasion, dreduction);
    }

    private String getResponseContent(String url) throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder().GET().uri(URI.create(url)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private List<Item> loadItems(String itemsFile) {
        var items = new ArrayList<Item>();
        var type = new TypeToken<Map<String, List<List<Object>>>>() {
        }.getType();
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
        return "https://bdocodex.com/query.php?a=items&l=" + local;
    }

    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        new BDOCodexScrapper();
    }

}
