package me.kleidukos.util;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class File {

    public static String readContent(String name){
        return new BufferedReader(new InputStreamReader(File.class.getClassLoader().getResourceAsStream(name))).lines().collect(Collectors.joining());
    }

    public static String getWeightRegex(String data) {
        final String regex = ": ([0-9].*?) LT";
        var r = Pattern.compile(regex, Pattern.MULTILINE);
        var m = r.matcher(data);

        if (m.find())
            return m.group(1);
        else
            return "";
    }

    public static String getItemTypeRegex(String data) {
        final String regex = "<td class=\"titles_cell\">(.*?)<";
        var r = Pattern.compile(regex);
        var m = r.matcher(data);

        if (m.find())
            return m.group(1);
        else
            return "";
    }

    public static String getBoundContentRegex(String data) {
        final String regex = "<hr class=\"tooltiphr\">(.*?)<hr class=\"tooltiphr\">";
        var r = Pattern.compile(regex, Pattern.DOTALL);
        var m = r.matcher(data);

        if (m.find())
            return m.group(1);
        else
            return "";
    }
    public static String getDescriptionContentRegex(String data) {
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(data), null);
        final var regex = "<hr class=\"tooltiphr\">.(. [Beschreibung|Description|Описание|Descripción|Descrição|説明|설명|说明|說明|คำอธิบาย|Açıklama|Penjelasan].*?)<hr class=\"tooltiphr\">";
        final var pattern = Pattern.compile(regex, Pattern.DOTALL);
        final var matcher = pattern.matcher(data);

        if (matcher.find())
            return matcher.group(1);
        else
            return "";
    }
}
