package me.kleidukos.object.callback;

import me.kleidukos.object.item.BaseItem;

import java.util.List;

public record UpdateCallback(List<BaseItem> items, String[] locals) { }
