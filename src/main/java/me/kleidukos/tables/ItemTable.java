package me.kleidukos.tables;

import de.chojo.sadu.base.QueryFactory;

import javax.sql.DataSource;

public class ItemTable extends QueryFactory {
    public ItemTable(DataSource dataSource) {
        super(dataSource);
    }

}
