package project;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.Scanner;

//todo: Richtige Formatierung der Tabelle mit Heim und Auswärts fehlt
//todo: Eingabe soll auch nur Dallas oder nur Mavericks erlauben
//todo: Game Number Counter soll auf 82 hoch laufen.

public class NBASeasonViewer {
    public static void main(String[] args) {
        ParseJsonResponse jsonResponse = new ParseJsonResponse();


        // Erstelle eine Map, um die ID den Städten zuzuordnen
        Map<String, Integer> lookupTable = new HashMap<>();

        // Füge die Zuordnungen hinzu
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


        // Abfrage der Mannschaft
        Scanner scanner = new Scanner(System.in);
        System.out.print("Gib einen vollständigen Teamnamen an: ");
        String input_name = scanner.nextLine();

        Integer input_id_int = lookupTable.get(input_name);
        String input_id = (input_id_int != null) ? Integer.toString(input_id_int) : null;
        System.out.println("Die Id der " + input_name + " lautet: " + input_id);


        // Abfrage des Jahres
        System.out.print("Welche Saison soll angezeigt werden: ");
        String input_season = scanner.nextLine();
        System.out.println("Jahr: " + input_season);

        scanner.close();


        try {
            // Erstellen des HTTP-Servers auf Port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Definieren des HTTP-Endpunkts
            server.createContext("/" + input_season, new GamesHandler(input_id, input_season, input_name));

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/" + input_season);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



