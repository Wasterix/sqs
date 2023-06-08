package com.example.demo.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.*;

public class MavsHighLow {
    public static void main(String[] args) {
        try {
            // Erstellen des HTTP-Servers auf Port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Definieren des HTTP-Endpunkts
            server.createContext("/mavs_hl", new GamesHandler());

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/mavs_hl");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class GamesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // URL des API-Endpunkts für Spiele
                String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=2022&team_ids[]=7&per_page=50";

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
                    String line;
                    StringBuilder response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parsing der JSON-Antwort
                    String jsonResponse = response.toString();
                    String htmlTable = parseJsonResponse(jsonResponse);

                    // Setzen der HTML-Antwort
                    exchange.getResponseHeaders().add("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, htmlTable.length());
                    exchange.getResponseBody().write(htmlTable.getBytes());
                } else {
                    // Setzen der Fehlerantwort
                    String errorMessage = "Fehler: " + responseCode;
                    exchange.sendResponseHeaders(responseCode, errorMessage.length());
                    exchange.getResponseBody().write(errorMessage.getBytes());
                }

                // Schließen der Verbindung
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                // Setzen der Fehlerantwort
                String errorMessage = "Fehler: " + e.getMessage();
                exchange.sendResponseHeaders(500, errorMessage.length());
                exchange.getResponseBody().write(errorMessage.getBytes());
            }

            exchange.getResponseBody().close();
        }
    }

    // Hilfsmethode zum Parsen der JSON-Antwort und Erstellen der HTML-Tabelle
    private static String parseJsonResponse(String jsonResponse) throws IOException {
        // JSON-Daten in Java-Objekte konvertieren
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");

        // Liste für Siege und Niederlagen erstellen
        List<JsonNode> winsList = new ArrayList<>();
        List<JsonNode> lossesList = new ArrayList<>();

        // Spiele in Siege und Niederlagen aufteilen
        for (JsonNode gameNode : gamesNode) {
            String homeTeam = gameNode.get("home_team").get("full_name").asText();
            String visitorTeam = gameNode.get("visitor_team").get("full_name").asText();
            int homeScore = gameNode.get("home_team_score").asInt();
            int visitorScore = gameNode.get("visitor_team_score").asInt();

            if (homeTeam.equals("Dallas Mavericks") && homeScore > visitorScore) {
                winsList.add(gameNode);
            } else if (visitorTeam.equals("Dallas Mavericks") && visitorScore > homeScore) {
                lossesList.add(gameNode);
            }
        }

        // Siege und Niederlagen nach Punktedifferenz sortieren
        Collections.sort(winsList, new GameComparator(true));
        Collections.sort(lossesList, new GameComparator(false));

        // Die 10 höchsten Siege auswählen
        List<JsonNode> topWinsList = winsList.subList(0, Math.min(10, winsList.size()));

        // Die 10 höchsten Niederlagen auswählen
        List<JsonNode> topLossesList = lossesList.subList(0, Math.min(10, lossesList.size()));

        // HTML-Tabelle für Siege erstellen
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<h2>Top 10 Siege</h2>");
        htmlTable.append("<table><tr><th>Game ID</th><th>Season</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th><th>Punktedifferenz</th></tr>");

        for (JsonNode gameNode : topWinsList) {
            int gameId = gameNode.get("id").asInt();
            String season = gameNode.get("season").asText();
            String homeTeam = gameNode.get("home_team").get("full_name").asText();
            String visitorTeam = gameNode.get("visitor_team").get("full_name").asText();
            int homeScore = gameNode.get("home_team_score").asInt();
            int visitorScore = gameNode.get("visitor_team_score").asInt();
            int scoreDiff = Math.abs(homeScore - visitorScore);

            htmlTable.append("<tr><td>").append(gameId).append("</td><td>").append(season).append("</td><td>")
                    .append(homeTeam).append("</td><td>").append(visitorTeam).append("</td><td>")
                    .append(homeScore).append("</td><td>").append(visitorScore).append("</td><td>")
                    .append(scoreDiff).append("</td></tr>");
        }

        htmlTable.append("</table>");

        // HTML-Tabelle für Niederlagen erstellen
        htmlTable.append("<h2>Top 10 Niederlagen</h2>");
        htmlTable.append("<table><tr><th>Game ID</th><th>Season</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th><th>Punktedifferenz</th></tr>");

        for (JsonNode gameNode : topLossesList) {
            int gameId = gameNode.get("id").asInt();
            String season = gameNode.get("season").asText();
            String homeTeam = gameNode.get("home_team").get("full_name").asText();
            String visitorTeam = gameNode.get("visitor_team").get("full_name").asText();
            int homeScore = gameNode.get("home_team_score").asInt();
            int visitorScore = gameNode.get("visitor_team_score").asInt();
            int scoreDiff = Math.abs(homeScore - visitorScore);

            htmlTable.append("<tr><td>").append(gameId).append("</td><td>").append(season).append("</td><td>")
                    .append(homeTeam).append("</td><td>").append(visitorTeam).append("</td><td>")
                    .append(homeScore).append("</td><td>").append(visitorScore).append("</td><td>")
                    .append(scoreDiff).append("</td></tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }

    // Hilfsklasse zum Vergleichen der Spiele nach Punktedifferenz
    static class GameComparator implements Comparator<JsonNode> {
        private boolean ascending;

        public GameComparator(boolean ascending) {
            this.ascending = ascending;
        }

        @Override
        public int compare(JsonNode game1, JsonNode game2) {
            int score1 = game1.get("home_team_score").asInt() + game1.get("visitor_team_score").asInt();
            int score2 = game2.get("home_team_score").asInt() + game2.get("visitor_team_score").asInt();

            if (ascending) {
                return Integer.compare(score1, score2);
            } else {
                return Integer.compare(score2, score1);
            }
        }
    }
}

