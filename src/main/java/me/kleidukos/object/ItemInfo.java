package me.kleidukos.object;

import java.util.HashMap;
import java.util.Map;

public class ItemInfo {

    private int levelRestriction;
    private final float weight;

    private final Map<String, ItemInfos> infos;

    public ItemInfo(float weight) {
        this.weight = weight;

        this.infos = new HashMap<>();
    }

    public void addInfo(String local, ItemInfos infos){
        if(!this.infos.containsKey(local))
            this.infos.put(local, infos);
    }

    protected void setLevel(int levelRestriction){
        this.levelRestriction = levelRestriction;
    }

    public int getLevelRestriction() {
        return levelRestriction;
    }

    public float getWeight() {
        return weight;
    }

    public Map<String, ItemInfos> getInfos() {
        return infos;
    }

    @Override
    public String toString() {
        return "ItemInfo{" +
                "levelRestriction=" + levelRestriction +
                ", weight=" + weight +
                ", infos=" + infos +
                '}';
    }
}
