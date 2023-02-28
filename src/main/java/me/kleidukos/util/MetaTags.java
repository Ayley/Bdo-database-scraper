package me.kleidukos.util;

public enum MetaTags {
    NAME,
    GRADE,
    LEVEL_RESTRICTION,
    WEIGHT,
    VOLUME,
    TYPE,
    BOUND,
    DESCRIPTION,
    EFFECTS,
    MIN_ATTACK,
    MAX_ATTACK,
    DEFENSE,
    ACCURACY,
    EVASION,
    HIDDEN_EVASION,
    DAMAGE_REDUCTION,
    HIDDEN_DAMAGE_REDUCTION;

    public static MetaTags getById(int id){
        return values()[id];
    }
}
