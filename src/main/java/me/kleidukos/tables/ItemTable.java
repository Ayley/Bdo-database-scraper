package me.kleidukos.tables;

import com.google.gson.Gson;
import de.chojo.sadu.base.QueryFactory;
import me.kleidukos.object.Item;

import javax.sql.DataSource;

public class ItemTable extends QueryFactory {
    public ItemTable(DataSource dataSource) {
        super(dataSource);
    }

    public void insertOrUpdate(Item item) {
        var gson = new Gson();
        builder().query("INSERT INTO 'item' (id, name, grade) VALUES (?,?,?) ON CONFLICT(id) DO UPDATE SET name=excluded.name, grade=excluded.grade;")
                .parameter(p -> p.setInt(item.getId()).setString(gson.toJson(item.getName())).setInt(item.getGrade()))
                .insert().send();
    }

    /*public CompletableFuture<Optional<BaseItem>> getBaseItem(int id, String[] locals){
        return builder(BaseItem.class).query("SELECT * FROM item WHERE id = ?")
                .parameter(p -> p.setInt(id))
                .readRow(rs -> {
                    BaseItem item = null;

                    var token = new TypeToken<Map<String, String>>() {}.getType();
                    Map<String, String> names = new Gson().fromJson(rs.getString("name"), token);

                    if(locals.length == 1){
                        item = new BaseItem(id, names.get(locals[0]), rs.getInt("grade"));
                    }else {
                        Map<String, String> newNames = new HashMap<>();
                        for (var local : locals){
                            if(names.containsKey(local))
                                newNames.put(local, names.get(local));
                        }

                        item = new BaseItem(id, newNames, rs.getInt("grade"));
                    }

                    return item;
                }).first();
    }*/
}
