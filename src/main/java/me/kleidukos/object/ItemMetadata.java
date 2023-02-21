package me.kleidukos.object;

public class ItemMetadata {

    private ItemInfo info;

    private ItemProperties properties;

    public ItemInfo getInfo() {
        return info;
    }

    public ItemProperties getProperties() {
        return properties;
    }

    public void setInfo(ItemInfo info) {
        if (this.info != null)
            return;

        info.setLevel(info.getLevelRestriction());
        this.info = info;
    }

    public void setProperties(ItemProperties properties) {
        if (this.properties != null)
            return;

        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ItemMetadata{" +
                "info=" + info +
                ", properties=" + properties +
                '}';
    }
}
