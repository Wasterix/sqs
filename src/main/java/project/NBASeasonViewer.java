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
        Map<Integer, String> lookupTable = new HashMap<>();

        // Füge die Zuordnungen hinzu
        lookupTable.put(1, "Atlanta Hawks");
        lookupTable.put(2, "Boston Celtics");
        lookupTable.put(3, "Brooklyn Nets");
        lookupTable.put(4, "Charlotte Hornets");
        lookupTable.put(5, "Chicago Bulls");
        lookupTable.put(6, "Cleveland Cavaliers");
        lookupTable.put(7, "Dallas Mavericks");
        lookupTable.put(8, "Denver Nuggets");
        lookupTable.put(9, "Detroit Pistons");
        lookupTable.put(10, "Golden State Warriors");
        lookupTable.put(11, "Houston Rockets");
        lookupTable.put(12, "Indiana Pacers");
        lookupTable.put(13, "LA Clippers");
        lookupTable.put(14, "Los Angeles Lakers");
        lookupTable.put(15, "Memphis Grizzlies");
        lookupTable.put(16, "Miami Heat");
        lookupTable.put(17, "Milwaukee Bucks");
        lookupTable.put(18, "Minnesota Timberwolves");
        lookupTable.put(19, "New Orleans Pelicans");
        lookupTable.put(20, "New York Knicks");
        lookupTable.put(21, "Oklahoma City Thunder");
        lookupTable.put(22, "Orlando Magic");
        lookupTable.put(23, "Philadelphia 76ers");
        lookupTable.put(24, "Phoenix Suns");
        lookupTable.put(25, "Portland Trail Blazers");
        lookupTable.put(26, "Sacramento Kings");
        lookupTable.put(27, "San Antonio Spurs");
        lookupTable.put(28, "Toronto Raptors");
        lookupTable.put(29, "Utah Jazz");
        lookupTable.put(30, "Washington Wizards");

        // Erstelle die umgekehrte Lookup-Tabelle
        Map<String, Integer> reverseLookupTable = new HashMap<>();
        for (Map.Entry<Integer, String> entry : lookupTable.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            reverseLookupTable.put(value, key);
        }

        // Eingabe der Mannschaft
        Scanner scanner = new Scanner(System.in);
        // Fordere den Benutzer auf, eine Eingabe einzugeben
        System.out.print("Gib einen vollständigen Teamnamen an: ");
        // Lese die Eingabe des Benutzers
        String input_name = scanner.nextLine();
        // Schließe den Scanner, um Ressourcen freizugeben
        //scanner.close();

        // Abfrage
        Integer input_id_int = reverseLookupTable.get(input_name);
        String input_id = (input_id_int != null) ? Integer.toString(input_id_int) : null;
        System.out.println("Die Id der " + input_name + " lautet: " + input_id);


        // Eingabe des Jahres
        //Scanner scanner = new Scanner(System.in);
        // Fordere den Benutzer auf, eine Eingabe einzugeben
        System.out.print("Welche Saison soll angezeigt werden: ");

        // Lese die Eingabe des Benutzers
        String input_season = scanner.nextLine();

        System.out.println("Jahr: " + input_season);

        // Schließe den Scanner, nachdem die Eingabe gelesen wurde
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



