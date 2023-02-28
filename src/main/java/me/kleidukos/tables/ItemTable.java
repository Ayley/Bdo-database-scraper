package me.kleidukos.tables;

import com.google.gson.Gson;
import de.chojo.sadu.base.QueryFactory;
import me.kleidukos.object.item.BaseItem;
import me.kleidukos.object.item.DetailedItem;
import me.kleidukos.object.tables.PropertyResult;
import me.kleidukos.object.tables.TranslationResult;
import me.kleidukos.util.DatabaseApi;
import me.kleidukos.util.Languages;
import me.kleidukos.util.MetaTags;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class ItemTable extends QueryFactory implements DatabaseApi {
    public ItemTable(DataSource dataSource) {
        super(dataSource);
    }

    public void insertBaseItem(BaseItem item) {
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
        return CompletableFuture.supplyAsync(() -> {
            var properties = getItemProperties(id).join();

            var grade = properties.stream().filter(pr -> pr.meta() == MetaTags.GRADE).findFirst().orElseGet(() -> new PropertyResult(MetaTags.GRADE, 0)).getValAsInt();
            var levelRestriction = properties.stream().filter(pr -> pr.meta() == MetaTags.LEVEL_RESTRICTION).findFirst().orElseGet(() -> new PropertyResult(MetaTags.LEVEL_RESTRICTION, 0)).getValAsInt();

            var item = new BaseItem(id, grade, levelRestriction);

            var translations = getItemTranslations(id).join();

            for (var translation : translations) {
                if (translation.tag() == MetaTags.NAME)
                    item.addName(translation.lang().getLocal(), translation.val());
            }

            return Optional.of(item);
        });
    }

    @Override
    public CompletableFuture<List<BaseItem>> getBaseItemsByName(String name) {
        var query = """
                SELECT DISTINCT item_id FROM translations WHERE val LIKE ?;
                """;
        return builder(Integer.class)
                .query(query)
                .parameter(param -> param.setString("%" + name + "%"))
                .readRow(rs -> rs.getInt("item_id"))
                .all()
                .thenApply(ids -> {
                    var list = new ArrayList<BaseItem>();
                    for (var id : ids) {
                        var item = getBaseItemById(id).join();
                        item.ifPresent(list::add);
                    }

                    return list;
                });
    }

    @Override
    public CompletableFuture<Optional<BaseItem>> getBaseItemByName(String name) {
        var query = """
                SELECT DISTINCT item_id FROM translations WHERE val LIKE ? LIMIT 1;
                """;
        return builder(Integer.class)
                .query(query)
                .parameter(param -> param.setString("%" + name + "%"))
                .readRow(rs -> rs.getInt("item_id"))
                .first()
                .thenApply(id -> {
                    if (id.isPresent())
                        return getBaseItemById(id.get()).join();
                    else
                        return Optional.empty();
                });
    }

    @Override
    public CompletableFuture<Optional<BaseItem>> getBaseItemByNameExact(String name) {
        var query = """
                SELECT DISTINCT item_id FROM translations WHERE val = ?;
                """;
        return builder(Integer.class)
                .query(query)
                .parameter(param -> param.setString(name))
                .readRow(rs -> rs.getInt("item_id"))
                .first()
                .thenApply(id -> {
                    if (id.isPresent())
                        return getBaseItemById(id.get()).join();
                    else
                        return Optional.empty();
                });
    }

    @Override
    public CompletableFuture<Optional<DetailedItem>> getDetailedItemById(int id) {
        return null;
    }

    @Override
    public CompletableFuture<Optional<DetailedItem>> getDetailedItemByName(String name) {
        return null;
    }

    private CompletableFuture<List<TranslationResult>> getItemTranslations(int id) {
        var query = """
                SELECT translations.language_key, translations.meta_tag, translations.val
                FROM translations
                WHERE item_id = ?;
                """;

        return builder(TranslationResult.class)
                .query(query)
                .parameter(param -> param.setInt(id))
                .readRow(rs -> {
                    var lang = Languages.getById(rs.getInt("language_key"));
                    var meta = MetaTags.getById(rs.getInt("meta_tag"));
                    var val = rs.getString("val");

                    return new TranslationResult(lang, meta, val);
                }).all();
    }

    private CompletableFuture<List<PropertyResult>> getItemProperties(int id) {
        var query = """
                SELECT metadata.meta_tag, metadata.val
                FROM metadata
                WHERE item_id = ?;
                """;

        return builder(PropertyResult.class)
                .query(query)
                .parameter(param -> param.setInt(id))
                .readRow(rs -> {
                    var meta = MetaTags.getById(rs.getInt("meta_tag"));
                    var val = rs.getObject("val");

                    return new PropertyResult(meta, val);
                }).all();
    }
}
