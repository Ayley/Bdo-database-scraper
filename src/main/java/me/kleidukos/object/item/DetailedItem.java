package me.kleidukos.object.item;

import me.kleidukos.util.Languages;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DetailedItem {

    private final int id;
    private final int grade;
    private final String icon;
    private final Map<String, String> names;

    private transient final List<String> locals;

    private ItemMetadata metadata;

    public DetailedItem(BaseItem item) {
        this.id = item.getId();
        this.grade = item.getGrade();
        this.icon = item.getIcon();
        this.names = item.getNames();
        this.locals = item.getLocals();
        this.metadata = new ItemMetadata();
    }

    public DetailedItem(DetailedItem item) {
        this.id = item.getId();
        this.grade = item.getGrade();
        this.icon = item.getIcon();
        this.names = item.getNames();
        this.locals = item.getLocals();
        this.metadata = new ItemMetadata();
    }

    public int getId() {
        return id;
    }

    public int getGrade() {
        return grade;
    }

    public String getIcon() {
        return icon;
    }

    public Map<String, String> getNames() {
        return Collections.unmodifiableMap(names);
    }

    public String getName(String local) {
        return names.get(local);
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

    public void setName(String local, String name){
        if(!locals.contains(local))
            return;

        names.replace(local, name);
    }

    public List<String> getLocals() {
        return Collections.unmodifiableList(locals);
    }

    public ItemMetadata getMetadata() {
        return metadata;
    }

    //Merge other item in this item
    public void merge(DetailedItem other){
        for (var entry : other.names.entrySet()){
            if(!names.containsKey(entry.getKey())) {
                names.put(entry.getKey(), entry.getValue());
                locals.add(entry.getKey());
            }
        }

        getMetadata().merge(other);
    }

    @Override
    public String
    toString() {
        return "DetailedItem{" +
                "id=" + id +
                ", grade=" + grade +
                ", icon='" + icon + '\'' +
                ", names=" + names +
                ", metadata=" + metadata +
                '}';
    }
}
