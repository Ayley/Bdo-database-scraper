import com.google.gson.Gson;
import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.SqLite;
import de.chojo.sadu.datasource.DataSourceCreator;
import de.chojo.sadu.updater.SqlUpdater;
import me.kleidukos.object.Item;
import me.kleidukos.object.Stats;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class test {


    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, SQLException {

        HikariDataSource dataSource = DataSourceCreator.create(SqLite.get()).create().build();

        SqlUpdater.builder(dataSource, SqLite.get()).execute();

        var data = new Gson().fromJson(new BufferedReader(new InputStreamReader(test.class.getResourceAsStream("ItemIds.txt"))), Data.class);

        var aa = 0;
        List<Item> items = new ArrayList<>();
        for (var d : data.aaData) {
            String da = (String) d.get(1) + d.get(2);

            if(aa == 3) break;

            Document doc = Jsoup.connect(getUrl(toInt(d.get(0)))).get();
            var title = doc.select("td.titles_cell");

            var stat = new Stats(title.select("span[id=damage]").text(), title.select("span[id=defense]").text(), title.select("span[id=accuracy]").text(), title.select("span[id=evasion]").text() + title.select("span[id=hevasion]").text(), title.select("span[id=dreduction]").text() + title.select("span[id=hdredution]").text());

            var s = doc.select("table.smallertext").select("td").last().children();

            int section = 0;
            String bound = "";
            String description = des(doc.html(), "Beschreibung");
            for (var ss : s){
                if(ss.className().equals("tooltiphr"))
                    section++;

                if(section == 1 && ss.hasText()){
                    bound += ss.text() + ";";
                }


            }

            //items.add(new Item(toInt(d.get(0)), getName(da), toInt(d.get(3)), toInt(d.get(5)), title.get(0).textNodes().get(0).text(), stat, bound, description, doc.select("div[id=edescription]").text(), doc.select("span[id=durability]").text(), weight(title.get(0).textNodes().get(title.get(0).textNodes().size() - 2).text())));
            aa++;
        }

        for (var item : items) {
            System.out.println(item);
        }
    }

    public static int toInt(Object o) {
        var s = o.toString();
        return Integer.parseInt(s.substring(0, s.length() - 2));
    }

    public static String getName(String data) {
        final String regex = "(</span>(.*?)</b>)";
        var r = Pattern.compile(regex, Pattern.MULTILINE);
        var m = r.matcher(data);

        if (m.find())
            return m.group(2);
        else
            return "";
    }

    public static String des(String data, String des) {
        final String regex = "(– " + des + ":(.*?)<div)";
        var r = Pattern.compile(regex, Pattern.DOTALL);
        var m = r.matcher(data);

        if (m.find())
            return m.group(2).replace("<br>", "").replace("\n", ";").trim();
        else
            return "";
    }

    public static String weight(String data) {
        final String regex = "(: (.*?) LT)";
        var r = Pattern.compile(regex, Pattern.MULTILINE);
        var m = r.matcher(data);

        if (m.find())
            return m.group(2);
        else
            return "";
    }

    public static String getUrl(int id){
        return "https://bdocodex.com/tip.php?id=item--" + id + "&enchant=0&l=de&nf=off";
    }
}
