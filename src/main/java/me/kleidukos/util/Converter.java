package me.kleidukos.util;

public class Converter {

    public static int objectToInt(Object o) {
        var s = o.toString();
        return Integer.parseInt(s.substring(0, s.length() - 2));
    }

}
