package me.kleidukos;

import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqLite;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.updater.SqlUpdater;
import me.kleidukos.tables.ItemTable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class BDOCodexScrapper {

    private BDOScrapper scrapper;
    private HikariDataSource dataSource;
    private ItemTable itemTable;

    public BDOCodexScrapper() throws IOException, SQLException, InterruptedException, ExecutionException {
        setupDatabase();
        setupTable();
        scrapper = new BDOScrapper(null, itemTable);

        scrapper.updateCompleteDatabase().join();
        System.out.println("Finished");
    }

    private void setupTable() {
        itemTable = new ItemTable(dataSource);
    }

    private void setupDatabase() throws IOException, SQLException {
        var file = new File("items.sqlite");
        if(!file.exists())
            Files.createFile(file.toPath());

        dataSource = DataSourceCreator.create(SqLite.get()).configure(conf -> conf.path(new java.io.File("items.sqlite"))).create().withMaximumPoolSize(1).build();

        SqlUpdater.builder(dataSource, SqLite.get()).execute();
    }

    public static void main(String[] args) throws IOException, InterruptedException, SQLException, ExecutionException {
        new BDOCodexScrapper();
    }

}
