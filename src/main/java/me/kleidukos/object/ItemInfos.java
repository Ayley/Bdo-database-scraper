package me.kleidukos.object;

public class ItemInfos {

    private final String type;
    private final String bound;
    private final String description;
    private final String effects;

    public ItemInfos(String type, String bound, String description, String effects) {
        this.type = type;
        this.bound = bound;
        this.description = description;
        this.effects = effects;
    }

    public String getType() {
        return type;
    }

    public String getBound() {
        return bound;
    }

    public String getDescription() {
        return description;
    }

    public String getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return "ItemInfos{" +
                "type='" + type + '\'' +
                ", bound='" + bound + '\'' +
                ", description='" + description + '\'' +
                ", effects='" + effects + '\'' +
                '}';
    }
}
