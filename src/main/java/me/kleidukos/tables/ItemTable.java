package me.kleidukos.tables;

import com.google.gson.Gson;
import de.chojo.sadu.base.QueryFactory;
import me.kleidukos.object.BaseItem;

import javax.sql.DataSource;

public class ItemTable extends QueryFactory {
    public ItemTable(DataSource dataSource) {
        super(dataSource);
    }

    public void insertOrUpdate(BaseItem item) {
        var gson = new Gson();
        builder().query("INSERT INTO 'item' (id, name, grade) VALUES (?,?,?) ON CONFLICT(id) DO UPDATE SET name=excluded.name, grade=excluded.grade;")
                .parameter(p -> p.setInt(item.getId()).setString(gson.toJson(item.getNames())).setInt(item.getGrade()))
                .insert().send();
    }

}
