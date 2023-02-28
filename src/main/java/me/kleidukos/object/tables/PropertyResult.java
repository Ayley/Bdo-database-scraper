package me.kleidukos.object.tables;

import me.kleidukos.util.MetaTags;

public record PropertyResult(MetaTags meta, Object val) {

    public int getValAsInt(){
        return (int) val;
    }

    public float getValAsFloat(){
        return (float) val;
    }

}
