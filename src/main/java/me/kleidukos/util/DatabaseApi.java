package me.kleidukos.util;

import me.kleidukos.object.item.BaseItem;
import me.kleidukos.object.item.DetailedItem;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface DatabaseApi {

    public CompletableFuture<Optional<BaseItem>> getBaseItemById(int id);
    public CompletableFuture<List<BaseItem>> getBaseItemsByName(String name);
    public CompletableFuture<Optional<BaseItem>> getBaseItemByName(String name);
    public CompletableFuture<Optional<BaseItem>> getBaseItemByNameExact(String name);

    public CompletableFuture<Optional<DetailedItem>> getDetailedItemById(int id);
    public CompletableFuture<Optional<DetailedItem>> getDetailedItemByName(String name);
}
