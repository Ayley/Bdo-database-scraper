package me.kleidukos.util;

import me.kleidukos.object.item.BaseItem;

import java.util.List;

public interface ScrapperCallback {

    public void finishedDatabaseUpdate(List<BaseItem> items, String[] locals);

}
