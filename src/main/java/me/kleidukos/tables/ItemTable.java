package me.kleidukos.tables;

import com.google.gson.Gson;
import de.chojo.sadu.base.QueryFactory;
import me.kleidukos.object.item.BaseItem;
import me.kleidukos.object.item.DetailedItem;
import me.kleidukos.util.DatabaseApi;
import me.kleidukos.util.Languages;
import me.kleidukos.util.MetaTags;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ItemTable extends QueryFactory implements DatabaseApi {
    public ItemTable(DataSource dataSource) {
        super(dataSource);
    }

    public void insertOrUpdate(BaseItem item) {
        var gson = new Gson();
        builder().query("INSERT INTO 'item' (id, name, grade) VALUES (?,?,?) ON CONFLICT(id) DO UPDATE SET name=excluded.name, grade=excluded.grade;")
                .parameter(p -> p.setInt(item.getId()).setString(gson.toJson(item.getNames())).setInt(item.getGrade()))
                .insert().send();
    }

    public void insertBaseItem(BaseItem item){
        var insertId = "INSERT INTO IGNORE item_ids (id) VALUES (?)";
        builder().query(insertId).parameter(param -> param.setInt(item.getId())).insert().send();

        var insertNames = """
                            INSERT INTO languages (item_id, language_key, meta_tag, val)
                            VALUES (?, ?, ?, ?)
                            ON CONFLICT(item_id, meta_tag) DO UPDATE SET
                            val=excluded.val;""";
        for (var name : item.getNames().entrySet()) {
            var lang = Languages.getByLocal(name.getKey());
            builder().query(insertNames).parameter(param -> param.setInt(item.getId()).setInt(lang.ordinal()).setInt(MetaTags.NAME.ordinal()).setString(name.getValue())).insert().send();
        }

        var insertGrade = """
                          INSERT INTO metadata (item_id, meta_tag, val) 
                          VALUES (?, ?, ?)
                          ON CONFLICT(item_id, meta_tag) DO UPDATE SET
                          val=excluded.val;
                          """;
        builder().query(insertGrade).parameter(param -> param.setInt(item.getId()).setInt(item.getGrade())).insert().send();
    }

    @Override
    public CompletableFuture<Optional<BaseItem>> getBaseItemById(int id) {
        var query = """
                    SELECT languages.val, metadata.val FROM languages, metadata
                    WHERE languages.item_id = ? AND metadata.item_id = languages.item_id;
                    """;
        return null;
    }

    @Override
    public CompletableFuture<Optional<BaseItem>> getBaseItemByName(String name) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<DetailedItem>> getDetailedItemById(int id) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<DetailedItem>> getDetailedItemByName(String name) {
        return null;
    }
}
