package me.kleidukos.util;

import java.util.Arrays;

public enum Languages {

    DE("de"),
    US("us"),
    FR("fr"),
    RU("ru"),
    ES("es"),
    SP("sp"),
    PT("pt"),
    JP("jp"),
    KR("kr"),
    CN("cn"),
    TW("tw"),
    TH("th"),
    TR("tr"),
    ID("id");

    private final String local;

    Languages(String local){
        this.local = local;
    }

    public static Languages getById(int id){
        return Languages.values()[id];
    }

    public static Languages getByLocal(String local){
        return Arrays.stream(values()).filter(lang -> lang.local == local).findFirst().get();
    }
}
