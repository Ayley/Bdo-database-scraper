package me.kleidukos.object.item;

import me.kleidukos.util.MetaTags;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemInfo {

    private int levelRestriction;
    private final float weight;
    private final float volume;

    private final Map<String, ItemInfos> infos;

    public ItemInfo(float weight, float volume) {
        this.weight = weight;
        this.volume = volume;
        this.infos = new HashMap<>();
    }

    public void addInfo(String local, ItemInfos infos){
        if(!this.infos.containsKey(local))
            this.infos.put(local, infos);
    }

    public void setInfo(String local, ItemInfos infos){
        if(this.infos.containsKey(local))
            this.infos.replace(local, infos);
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

    public float getVolume() {
        return volume;
    }

    public Map<String, ItemInfos> getInfos() {
        return Collections.unmodifiableMap(infos);
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
