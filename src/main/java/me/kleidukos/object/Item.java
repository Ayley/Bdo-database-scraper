package me.kleidukos.object;

public class Item{
    private final int id;
    private final String name;
    private final int grade;
    private final int level;
    private Stats stats;
    private ItemDetail detail;

    public Item(int id, String name, int grade, int level) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public int getLevel() {
        return level;
    }

    public Stats getStats() {
        return stats;
    }

    public ItemDetail getDetail() {
        return detail;
    }

    public void setStats(Stats stats) {
        this.stats = stats;
    }

    public void setDetail(ItemDetail detail) {
        this.detail = new ItemDetail(level, detail.type(), detail.bound(), detail.description(), detail.eDescription(), detail.durability(), detail.weight());
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", grade=" + grade +
                ", level=" + level +
                ", stats=" + stats +
                ", detail=" + detail +
                '}';
    }
}
