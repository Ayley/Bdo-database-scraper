package me.kleidukos.object;

import java.util.HashMap;
import java.util.Map;

public class ItemDetail {

    private final int level;
    private final float weight;
    private final float durability;

    private final Map<String, String> type;
    private final Map<String, String> bound;
    private final Map<String, String> description;
    private final Map<String, String> eDescription;

    public ItemDetail(int level, float weight, float durability) {
        this.level = level;
        this.weight = weight;
        this.durability = durability;

        this.type = new HashMap<>();
        this.bound = new HashMap<>();
        this.description = new HashMap<>();
        this.eDescription = new HashMap<>();
    }

    public void addType(String local, String type){
        if(!this.type.containsKey(local))
            this.type.put(local, type);
    }

    public void addBound(String local, String bound){
        if(!this.bound.containsKey(local))
            this.bound.put(local, bound);
    }

    public void addDescription(String local, String description){
        if(!this.description.containsKey(local))
            this.description.put(local, description);
    }

    public void addEDescription(String local, String eDescription){
        if(!this.eDescription.containsKey(local))
            this.eDescription.put(local, eDescription);
    }

    public int getLevel() {
        return level;
    }

    public float getWeight() {
        return weight;
    }

    public float getDurability() {
        return durability;
    }

    public Map<String, String> getType() {
        return type;
    }

    public Map<String, String> getBound() {
        return bound;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public Map<String, String> geteDescription() {
        return eDescription;
    }

    @Override
    public String toString() {
        return "ItemDetail{" +
                "level=" + level +
                ", type='" + type + '\'' +
                ", bound='" + bound + '\'' +
                ", description='" + description + '\'' +
                ", eDescription='" + eDescription + '\'' +
                ", durability='" + durability + '\'' +
                ", weight='" + weight + '\'' +
                '}';
    }
}
