package me.kleidukos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class File {

    public static String readContent(String name){
        return new BufferedReader(new InputStreamReader(File.class.getClassLoader().getResourceAsStream(name))).lines().collect(Collectors.joining());
    }

    public static String getNameRegex(String data) {
        final String regex = "(</span>(.*?)</b>)";
        var r = Pattern.compile(regex, Pattern.MULTILINE);
        var m = r.matcher(data);

        if (m.find())
            return m.group(2);
        else
            return "";
    }
}
