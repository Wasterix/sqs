package project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Hilfsmethode zum Parsen der JSON-Antwort und Erstellen der HTML-Tabellen für Heim- und Auswärtsspiele
public class ParseJsonResponse {
    public String parseJasonResponseRESTAPI (String jsonResponse, String input_name) throws IOException {
        // JSON-Daten in Java-Objekte konvertieren
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");

        // Listen für Heim- und Auswärtsspiele erstellen
        List<JsonNode> homeGames = new ArrayList<>();
        List<JsonNode> visitorGames = new ArrayList<>();

        // Aufteilen der Spiele in Heim- und Auswärtsspiele
        for (JsonNode gameNode : gamesNode) {
            String homeTeam = gameNode.get("home_team").get("full_name").asText();

            if (homeTeam.equals(input_name)) {
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
