package project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ParseJsonResponse {
    public String parseJsonResponseRESTAPI(String jsonResponse, String inputName, String inputSeason) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");
        if (gamesNode == null || gamesNode.isEmpty()) {
            return "Die " +inputName+ " waren " +inputSeason+ " vermutlich noch nicht in der NBA.";
        }
        List<JsonNode> homeGames = new ArrayList<>();
        List<JsonNode> visitorGames = new ArrayList<>();

        for (JsonNode gameNode : gamesNode) {
            String homeTeam = gameNode.get("home_team").get("full_name").asText();

            if (homeTeam.equals(inputName)) {
                homeGames.add(gameNode);
            } else {
                visitorGames.add(gameNode);
            }
        }

        Comparator<JsonNode> dateComparator = Comparator.comparing(game -> game.get("date").asText());
        homeGames.sort(dateComparator);
        visitorGames.sort(dateComparator);

        StringBuilder homeTable = createTable("Heimspiele", homeGames);
        StringBuilder visitorTable = createTable("Auswärtsspiele", visitorGames);

        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append(homeTable).append(visitorTable);

        return htmlTable.toString();
    }

    private static StringBuilder createTable(String title, List<JsonNode> games) {

        int gameNumber = 1;
        StringBuilder table = new StringBuilder("<meta charset=\"UTF-8\">");
        table.append("<h2>").append(title).append("</h2>")
                .append("<table>")
                .append("<tr><th>Game Number</th><th>Game ID</th><th>Season</th><th>Date</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr>");


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