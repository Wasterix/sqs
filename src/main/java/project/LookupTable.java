package project;

import java.util.HashMap;
import java.util.Map;

public class LookupTable {
    private static Map<String, Integer> lookupTable;

    static {
        lookupTable = new HashMap<>();
        lookupTable.put("Atlanta Hawks", 1);
        lookupTable.put("Boston Celtics", 2);
        lookupTable.put("Brooklyn Nets", 3);
        lookupTable.put("Charlotte Hornets", 4);
        lookupTable.put("Chicago Bulls", 5);
        lookupTable.put("Cleveland Cavaliers", 6);
        lookupTable.put("Dallas Mavericks", 7);
        lookupTable.put("Denver Nuggets", 8);
        lookupTable.put("Detroit Pistons", 9);
        lookupTable.put("Golden State Warriors", 10);
        lookupTable.put("Houston Rockets", 11);
        lookupTable.put("Indiana Pacers", 12);
        lookupTable.put("LA Clippers", 13);
        lookupTable.put("Los Angeles Lakers", 14);
        lookupTable.put("Memphis Grizzlies", 15);
        lookupTable.put("Miami Heat", 16);
        lookupTable.put("Milwaukee Bucks", 17);
        lookupTable.put("Minnesota Timberwolves", 18);
        lookupTable.put("New Orleans Pelicans", 19);
        lookupTable.put("New York Knicks", 20);
        lookupTable.put("Oklahoma City Thunder", 21);
        lookupTable.put("Orlando Magic", 22);
        lookupTable.put("Philadelphia 76ers", 23);
        lookupTable.put("Phoenix Suns", 24);
        lookupTable.put("Portland Trail Blazers", 25);
        lookupTable.put("Sacramento Kings", 26);
        lookupTable.put("San Antonio Spurs", 27);
        lookupTable.put("Toronto Raptors", 28);
        lookupTable.put("Utah Jazz", 29);
        lookupTable.put("Washington Wizards", 30);
    }

    public static boolean containsTeam(String teamName) {
        return lookupTable.containsKey(teamName);
    }

    public static String getTeamId(String teamName) {
        Integer id = lookupTable.get(teamName);
        if (id != null) {
            return id.toString();
        } else {
            return "";
        }
    }
}