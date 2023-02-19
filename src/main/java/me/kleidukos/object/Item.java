package me.kleidukos.object;

import java.util.Map;

public class Item{
    private final int id;
    private final Map<String, String> names;
    private final int grade;
    private final int level;
    private ItemStats stats;
    private ItemDetail detail;

    public Item(int id, Map<String, String> names, int grade, int level) {
        this.id = id;
        this.names = names;
        this.grade = grade;
        this.level = level;

    }

    public int getId() {
        return id;
    }

    public Map<String, String> getName() {
        return names;
    }

    public void addName(String local, String name){
        if(!names.containsKey(local))
            names.put(local, name);
    }

    public int getGrade() {
        return grade;
    }

    public int getLevel() {
        return level;
    }

    public ItemStats getStats() {
        return stats;
    }

    public ItemDetail getDetail() {
        return detail;
    }

    public void setStats(ItemStats stats) {
        this.stats = stats;
    }

    public void createDetails(float weight, float durability){
        this.detail = new ItemDetail(level, weight, durability);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + names.toString() + '\'' +
                ", grade=" + grade +
                ", level=" + level +
                ", stats=" + stats +
                ", detail=" + detail +
                '}';
    }
}
