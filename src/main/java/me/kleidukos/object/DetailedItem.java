package me.kleidukos.object;

public class DetailedItem {

    private final BaseItem item;

    private ItemMetadata metadata;

    public DetailedItem(BaseItem item) {
        this.item = item;
        this.metadata = new ItemMetadata();
    }

    public BaseItem getItem() {
        return item;
    }

    public ItemMetadata getMetadata() {
        return metadata;
    }

    @Override
    public String toString() {
        return "DetailedItem{" +
                "item=" + item +
                ", metadata=" + metadata +
                '}';
    }
}
