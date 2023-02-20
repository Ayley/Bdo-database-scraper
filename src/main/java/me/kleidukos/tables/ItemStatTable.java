package me.kleidukos.tables;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.wrapper.QueryBuilderConfig;
import me.kleidukos.object.Item;
import me.kleidukos.object.ItemStats;

import javax.sql.DataSource;

public class ItemStatTable extends QueryFactory {
    public ItemStatTable(DataSource dataSource) {
        super(dataSource);
    }

    public void insertOrUpdate(Item item){
        var s = item.getStats();
        builder().query("INSERT INTO 'item_stat' (id, attack, defense, accuracy, evasion, damageReduction) VALUES (?,?,?,?,?,?) ON CONFLICT(id) DO UPDATE SET attack=excluded.attack, defense=excluded.defense, accuracy=excluded.accuracy, evasion=excluded.evasion, damageReduction=excluded.damageReduction;")
                .parameter(p -> p.setInt(item.getId()).setString(s.attack()).setString(s.defense()).setString(s.accuracy()).setString(s.evasion()).setString(s.damageReduction()))
                .insert().send();
    }

}
