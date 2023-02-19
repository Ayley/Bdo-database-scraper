package me.kleidukos.object;

public record ItemDetail(int level, String type, String bound, String description, String eDescription, String durability, String weight) {
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
