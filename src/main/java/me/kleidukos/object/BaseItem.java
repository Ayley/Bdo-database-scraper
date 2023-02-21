package me.kleidukos.object;

import java.util.HashMap;
import java.util.Map;

public class BaseItem {

    private final int id;
    private final int grade;
    private final int levelRestriction;

    private Map<String, String> names = new HashMap<>();

    public BaseItem(int id, int grade, int levelRestriction) {
        this.id = id;
        this.grade = grade;
        this.levelRestriction = levelRestriction;
    }

    public int getId() {
        return id;
    }

    public int getGrade() {
        return grade;
    }

    public String getName(String local) {
        return names.get(local);
    }

    public Map<String, String> getNames() {
        return names;
    }

    public boolean hasLocalName(String local){
        return names.containsKey(local);
    }

    public void addName(String local, String name){
        names.put(local, name);
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                "id=" + id +
                ", grade=" + grade +
                ", levelRestriction=" + levelRestriction +
                ", names=" + names +
                '}';
    }
}
