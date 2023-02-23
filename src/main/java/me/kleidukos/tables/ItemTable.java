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

    public void insertBaseItem(BaseItem item){
        final var insertId = "INSERT OR IGNORE INTO item_ids (id) VALUES (?)";
        builder().query(insertId).parameter(param -> param.setInt(item.getId()))
                .insert()
                .send();

        final var insertNames = """
                            INSERT INTO translations (item_id, language_key, meta_tag, val)
                            VALUES (?, ?, ?, ?)
                            ON CONFLICT DO UPDATE SET
                            val=excluded.val
                            WHERE item_id = excluded.item_id AND language_key = excluded.language_key AND meta_tag = excluded.meta_tag;""";
        for (var name : item.getNames().entrySet()) {
            var lang = Languages.getByLocal(name.getKey());
            builder().query(insertNames).parameter(param -> param.setInt(item.getId())
                    .setInt(lang.ordinal())
                    .setInt(MetaTags.NAME.ordinal())
                    .setString(name.getValue()))
                    .insert()
                    .sendSync();
        }

        final var insertGrade = """
                          INSERT INTO metadata (item_id, meta_tag, val) 
                          VALUES (?, ?, ?)
                          ON CONFLICT DO UPDATE SET
                          val=excluded.val
                          WHERE item_id = excluded.item_id AND meta_tag = excluded.meta_tag;
                          """;
        builder().query(insertGrade).parameter(param -> param.setInt(item.getId())
                .setInt(MetaTags.GRADE.ordinal())
                .setInt(item.getGrade()))
                .insert()
                .sendSync();
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
