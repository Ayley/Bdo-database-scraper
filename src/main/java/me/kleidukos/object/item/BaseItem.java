package me.kleidukos.object.item;

import java.util.*;

public class BaseItem {

    private final int id;
    private final int grade;
    private final int levelRestriction;
    private final String icon;

    private transient final List<String> locals = new ArrayList<>();

    private Map<String, String> names = new HashMap<>();

    public BaseItem(int id, int grade, int levelRestriction) {
        this.id = id;
        this.grade = grade;
        this.levelRestriction = levelRestriction;
        this.icon = "url/" + id + ".png";
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
        return Collections.unmodifiableMap(names);
    }

    public String getIcon() {
        return icon;
    }

    public boolean hasLocalName(String local) {
        return names.containsKey(local);
    }

    public void addName(String local, String name) {
        if (locals.contains(local))
            setName(local, name);
        else {
            names.put(local, name);
            locals.add(local);
        }
    }

    public void setName(String local, String name) {
        if (!locals.contains(local))
            return;

        names.replace(local, name);
    }

    public List<String> getLocals() {
        return Collections.unmodifiableList(locals);
    }

    //Merge other item in this item
    public void merge(BaseItem other){
        for (var entry : other.names.entrySet()){
            if(!names.containsKey(entry.getKey())) {
                names.put(entry.getKey(), entry.getValue());
                locals.add(entry.getKey());
            }else {
                setName(entry.getKey(), entry.getValue());
            }
        }
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
