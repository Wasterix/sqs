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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Games_in_2020 {
    public static void main(String[] args) {

        // Erstelle einen Scanner, um die Konsoleneingabe zu lesen
        Scanner scanner = new Scanner(System.in);
        // Fordere den Benutzer auf, eine Eingabe einzugeben
        System.out.print("Gib Team_Id an: ");
        // Lese die Eingabe des Benutzers
        String input_id = scanner.nextLine();
        // Schließe den Scanner, um Ressourcen freizugeben
        scanner.close();
        // Gib die eingegebene Variable aus
        System.out.println("Team_Id lautet: " + input_id);


        try {
            // Erstellen des HTTP-Servers auf Port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Definieren des HTTP-Endpunkts
            server.createContext("/2020", new GamesHandler(input_id));

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/2020");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class GamesHandler implements HttpHandler {
        private final String teamId;

        public GamesHandler(String teamId){
            this.teamId = teamId;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {



            try {
                // URL des API-Endpunkts für Spiele
                String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=2022&team_ids[]=" + teamId + "&per_page=82";

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
