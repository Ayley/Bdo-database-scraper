package me.kleidukos.object.item;

public class ItemMetadata {

    private ItemInfo details;

    private ItemProperties properties;

    public ItemInfo getDetails() {
        return details;
    }

    public ItemProperties getProperties() {
        return properties;
    }

    public void setDetails(ItemInfo details) {
        if (this.details != null)
            return;

        details.setLevel(details.getLevelRestriction());
        this.details = details;
    }

    public void setProperties(ItemProperties properties) {
        if (this.properties != null)
            return;

        this.properties = properties;
    }

    //Merge other item in this item

    protected void merge(DetailedItem other){
        for (var entry : other.getMetadata().getDetails().getInfos().entrySet()) {
            if(!getDetails().getInfos().containsKey(entry.getKey())){
                getDetails().addInfo(entry.getKey(), entry.getValue());
            }else {
                getDetails().setInfo(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String toString() {
        return "ItemMetadata{" +
                "info=" + details +
                ", properties=" + properties +
                '}';
    }
}
