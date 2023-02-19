package me.kleidukos.tables;

import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.wrapper.QueryBuilderConfig;

import javax.sql.DataSource;

public class ItemStatTable extends QueryFactory {
    public ItemStatTable(DataSource dataSource) {
        super(dataSource);
    }


}
