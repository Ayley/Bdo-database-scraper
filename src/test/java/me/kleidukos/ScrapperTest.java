package me.kleidukos;

import com.google.gson.Gson;
import me.kleidukos.object.BaseItem;
import org.junit.Test;

import java.io.IOException;

public class ScrapperTest {

    @Test
    public void testLoadItems() throws IOException, InterruptedException {
        var items = Scrapper.loadBaseItems("de");

        for (var item : items) {
            System.out.println(item);
        }
    }

    @Test
    public void testUpgradeItem() throws IOException, InterruptedException {
        var item = new BaseItem(732052, 1, 1);

        var detailedItem = Scrapper.upgradeItem(item, "de");

        Scrapper.updateDetailedItemToLocal(detailedItem, "us");

        var gson = new Gson().newBuilder().setPrettyPrinting().create();

        System.out.println(gson.toJson(detailedItem));
    }

}
