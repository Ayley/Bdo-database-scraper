package me.kleidukos.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class File {

    public static String readContent(String name){
        return new BufferedReader(new InputStreamReader(File.class.getClassLoader().getResourceAsStream(name))).lines().collect(Collectors.joining());
    }

    public static String getWeightRegex(String data) {
        final String regex = "([0-9.-].*?) LT";
        var r = Pattern.compile(regex, Pattern.MULTILINE);
        var m = r.matcher(data);

        if (m.find())
            return m.group(1);
        else
            return "";
    }

    public static String getItemTypeRegex(String data) {
        final String regex = "\"titles_cell\">[\n](\s*)(.*)";
        var r = Pattern.compile(regex, Pattern.MULTILINE);
        var m = r.matcher(data);

        if (m.find())
            return m.group(2);
        else
            return "";
    }

    public static String getBoundContentRegex(String data) {
        final String regex = "/<hr class=\"tooltiphr\">(.*?)<hr class=\"tooltiphr\">/s";
        var r = Pattern.compile(regex);
        var m = r.matcher(data);

        if (m.find())
            return m.group(1);
        else
            return "";
    }
}
