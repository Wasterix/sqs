package project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;
import java.util.Scanner;

//todo: Richtige Formattierung der Tabelle mit Heim und Auswärts fehlt
//todo: Eingabe soll auch nur Dallas oder nur Mavericks erlauben
//todo: Jahr soll noch in Eingabe abgefragt werden

public class NBASeasonViewer {
    public static void main(String[] args) {

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
        System.out.println("Die Id der " + input_name + " lautet: "+ input_id);


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
            server.createContext("/" + input_season, new GamesHandler(input_id, input_season));

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/" + input_season);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class GamesHandler implements HttpHandler {
        private final String teamId;
        private final String season;


        public GamesHandler(String teamId, String season){
            this.teamId = teamId;
            this.season = season;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {



            try {
                // URL des API-Endpunkts für Spiele
                String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=" + season + "&team_ids[]=" + teamId + "&per_page=82";

                // Erstellen des URL-Objekts
                URL url = new URL(gamesApiUrl);

                // Öffnen der Verbindung
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Überprüfen des HTTP-Statuscodes
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Lesen der Antwort
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parsing der JSON-Antwort
                    String jsonResponse = response.toString();
                    String htmlTable = parseJsonResponse(jsonResponse);

                    // Setzen der HTML-Antwort
                    exchange.getResponseHeaders().add("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, 0);  // 0 als Content-Length

                    // Schreiben der Daten in den OutputStream in Teilen
                    OutputStream outputStream = exchange.getResponseBody();
                    int chunkSize = 1024;
                    byte[] data = htmlTable.getBytes();
                    int offset = 0;
                    while (offset < data.length) {
                        int length = Math.min(chunkSize, data.length - offset);
                        outputStream.write(data, offset, length);
                        offset += length;
                    }

                    // Schließen des OutputStreams
                    outputStream.close();
                } else {
                    // Setzen der Fehlerantwort
                    String errorMessage = "Fehler: " + responseCode;
                    exchange.sendResponseHeaders(responseCode, errorMessage.length());
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(errorMessage.getBytes());
                    outputStream.close();
                }

                // Schließen der Verbindung
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                // Setzen der Fehlerantwort
                String errorMessage = "Fehler: " + e.getMessage();
                exchange.sendResponseHeaders(500, errorMessage.length());
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(errorMessage.getBytes());
                outputStream.close();
            }
        }
    }

    // Hilfsmethode zum Parsen der JSON-Antwort und Erstellen der HTML-Tabellen für Heim- und Auswärtsspiele
    private static String parseJsonResponse(String jsonResponse) throws IOException {
        // JSON-Daten in Java-Objekte konvertieren
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");

        // Listen für Heim- und Auswärtsspiele erstellen
        List<JsonNode> homeGames = new ArrayList<>();
        List<JsonNode> visitorGames = new ArrayList<>();

        // Aufteilen der Spiele in Heim- und Auswärtsspiele
        for (JsonNode gameNode : gamesNode) {
            String homeTeam = gameNode.get("home_team").get("full_name").asText();
            if (homeTeam.equals("Dallas Mavericks")) {
                homeGames.add(gameNode);
            } else {
                visitorGames.add(gameNode);
            }
        }

        // Sortieren der Auswärtsspiele nach der Reihenfolge "Home Team, Visitor Team, Home Score, Visitor Score"
        Comparator<JsonNode> visitorComparator = Comparator.comparing((JsonNode game) -> game.get("home_team").get("full_name").asText())
                .thenComparing(game -> game.get("visitor_team").get("full_name").asText())
                .thenComparingInt(game -> game.get("home_team_score").asInt())
                .thenComparingInt(game -> game.get("visitor_team_score").asInt());

        visitorGames.sort(visitorComparator);


        // Erstellen der HTML-Tabellen für Heim- und Auswärtsspiele
        StringBuilder homeTable = createTable("Heimspiele", homeGames);
        StringBuilder visitorTable = createTable("Auswärtsspiele", visitorGames);

        // HTML-Code für die beiden Tabellen kombinieren
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append(homeTable).append(visitorTable);

        return htmlTable.toString();
    }

    // Hilfsmethode zum Erstellen der HTML-Tabelle für Spiele
    private static StringBuilder createTable(String title, List<JsonNode> games) {
        StringBuilder table = new StringBuilder();
        table.append("<h2>").append(title).append("</h2>")
                .append("<table>")
                .append("<tr><th>Game Number</th><th>Game ID</th><th>Season</th><th>Date</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr>");

        int gameNumber = 1;
        for (JsonNode gameNode : games) {
            int gameId = gameNode.get("id").asInt();
            String season = gameNode.get("season").asText();
            String date = gameNode.get("date").asText().split("T")[0];
            String homeTeam = gameNode.get("home_team").get("full_name").asText();
            String visitorTeam = gameNode.get("visitor_team").get("full_name").asText();
            int homeScore = gameNode.get("home_team_score").asInt();
            int visitorScore = gameNode.get("visitor_team_score").asInt();

            table.append("<tr>")
                    .append("<td>").append(gameNumber).append("</td>")
                    .append("<td>").append(gameId).append("</td>")
                    .append("<td>").append(season).append("</td>")
                    .append("<td>").append(date).append("</td>")
                    .append("<td>").append(homeTeam).append("</td>")
                    .append("<td>").append(visitorTeam).append("</td>")
                    .append("<td>").append(homeScore).append("</td>")
                    .append("<td>").append(visitorScore).append("</td>")
                    .append("</tr>");

            gameNumber++;
        }

        table.append("</table>");

        return table;
    }
}
