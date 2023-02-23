package me.kleidukos.util;

import me.kleidukos.object.callback.UpdateCallback;
import me.kleidukos.object.item.BaseItem;
import me.kleidukos.object.item.DetailedItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ScrapperApi {

    /**
     * Updates the whole database with all locals
     * Gives callback with <br>
     * {@link ScrapperCallback#finishedDatabaseUpdate(List, String[])} <br>
     * The list will directly return the base items
     * The string array returns all updated locals
     */
    public CompletableFuture<UpdateCallback> updateCompleteDatabase();

    /**
     * Updates the whole database with a specific local
     * Gives callback with <br>
     * {@link ScrapperCallback#finishedDatabaseUpdate(List, String[])} <br>
     * The list will directly return the base items
     * The string array returns all updated locals
     * @param local The specified local
     */
    public CompletableFuture<UpdateCallback> updateDatabase(String local);

    /**
     * Upgrade an item to DetailedItem with more specific information, the detailed item is forced put into the database
     * @param item The item that have to upgrade
     * @param tryGetFromDB True will try to get the item from database otherwise it will be scrapped
     * @return The detailed item
     */
    public CompletableFuture<DetailedItem> upgradeBaseItem(BaseItem item, boolean tryGetFromDB);

    /**
     * Add a new local to a Detailed item, the item will be updated in the database
     * @param item The item to add a new local
     * @param local The specific local
     * @return Returns the same item with the new added local
     */
    public CompletableFuture<DetailedItem> addLocalToDetailedItem(DetailedItem item, String local);
}
