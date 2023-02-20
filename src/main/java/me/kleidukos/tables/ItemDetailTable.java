package me.kleidukos.tables;

import com.google.gson.Gson;
import de.chojo.sadu.base.QueryFactory;
import me.kleidukos.object.Item;

import javax.sql.DataSource;

public class ItemDetailTable extends QueryFactory {
    public ItemDetailTable(DataSource dataSource) {
        super(dataSource);
    }

    public void insertOrUpdate(Item item){
        var d = item.getDetail();
        var gson = new Gson();
        builder().query("INSERT INTO 'item_detail' (id, level, type, bound, description, e_description, durability, weight) VALUES (?,?,?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET level=excluded.level, type=excluded.type, bound=excluded.bound, description=excluded.description, e_description=excluded.e_description, durability=excluded.durability, weight=excluded.weight;")
                .parameter(p -> p.setInt(item.getId()).setInt(d.getLevel()).setString(gson.toJson(d.getType())).setString(gson.toJson(d.getBound())).setString(gson.toJson(d.getDescription())).setString(gson.toJson(d.geteDescription())).setFloat(d.getDurability()).setFloat(d.getWeight()))
                .insert().send();
    }
}
