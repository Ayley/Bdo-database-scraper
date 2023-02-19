package me.kleidukos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.kleidukos.object.Item;
import me.kleidukos.util.File;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BDOCodexScrapper {

    private final List<Item> items = new ArrayList<>();

    public BDOCodexScrapper(){
        //Load items from file "ItemIds.txt"
        loadIds();

        for (var item : items) {
            System.out.println(item);
        }
    }

    private void loadIds() {
        var itemIds = File.readContent("ItemIds.txt");

        var type = new TypeToken<Map<String, List<List<Object>>>>() {}.getType();
        Map<String, List<List<Object>>> data = new Gson().fromJson(itemIds, type);

        for (var item : data.get("aaData")) {
            var itemData = (String) item.get(1) + item.get(2);

            var id = objectToInt(item.get(0));
            var name = File.getNameRegex(itemData);
            var grade = objectToInt(item.get(5));
            var level = objectToInt(item.get(3));

            items.add(new Item(id, name, grade, level));
        }
    }

    public int objectToInt(Object o) {
        var s = o.toString();
        return Integer.parseInt(s.substring(0, s.length() - 2));
    }

    public static void main(String[] args) {
        new BDOCodexScrapper();
    }

}
