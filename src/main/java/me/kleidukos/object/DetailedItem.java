package me.kleidukos.object;

public class DetailedItem {

    private final BaseItem item;

    private ItemInfo itemInfo;

    private ItemProperties itemProperties;

    public DetailedItem(BaseItem item) {
        this.item = item;
    }

    public BaseItem getItem() {
        return item;
    }

    public ItemInfo getItemInfo() {
        return itemInfo;
    }

    public ItemProperties getItemProperties() {
        return itemProperties;
    }

    public void setItemInfo(ItemInfo itemInfo) {
        if (this.itemInfo != null)
            return;

        itemInfo.setLevel(itemInfo.getLevelRestriction());
        this.itemInfo = itemInfo;
    }

    public void setItemProperties(ItemProperties itemProperties) {
        if (this.itemProperties != null)
            return;

        this.itemProperties = itemProperties;
    }

    @Override
    public String toString() {
        return "DetailedItem{" +
                "item=" + item +
                ", itemInfo=" + itemInfo +
                ", itemProperties=" + itemProperties +
                '}';
    }
}
