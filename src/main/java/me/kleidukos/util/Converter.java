package me.kleidukos.util;

public class Converter {

    public static int objectToInt(Object o) {
        var s = o.toString();
        return Integer.parseInt(s.substring(0, s.length() - 2));
    }

    public static float stringToFloat(String s){
        if(s.isEmpty() || s.isBlank())
            return 0;

        return Float.parseFloat(s);
    }

}
