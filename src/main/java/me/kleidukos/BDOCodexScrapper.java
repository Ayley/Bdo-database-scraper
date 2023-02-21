package me.kleidukos;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqLite;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.updater.SqlUpdater;
import me.kleidukos.tables.ItemTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;

public class BDOCodexScrapper {
    private final String[] locals = new String[]{"de", "us", "fr", "ru", "es", "sp", "pt", "jp", "kr", "cn", "tw", "th", "tr", "id"};

    private HikariDataSource dataSource;
    private ItemTable itemTable;

    public BDOCodexScrapper() throws IOException, SQLException, InterruptedException {
        setupDatabase();
        setupTable();

        var items = Scrapper.loadBaseItems("de");

        for (var local : Arrays.copyOfRange(locals, 1, locals.length)) {
            Scrapper.updateItemsToLocal(items, local);
        }
    }

    private void setupTable() {
        itemTable = new ItemTable(dataSource);
    }

    private void setupDatabase() throws IOException, SQLException {
        var file = new File("items.splite");
        if(!file.exists())
            Files.createFile(file.toPath());

        dataSource = DataSourceCreator.create(SqLite.get()).configure(conf -> conf.path(new java.io.File("items.sqlite"))).create().build();

        SqlUpdater.builder(dataSource, SqLite.get()).execute();
    }



    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        new BDOCodexScrapper();
    }

}
