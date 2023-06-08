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

public class Mavs2018 {
    public static void main(String[] args) {
        try {
            // Erstellen des HTTP-Servers auf Port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Definieren des HTTP-Endpunkts
            server.createContext("/mavs2018", new GamesHandler());

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/mavs2018");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class GamesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // URL des API-Endpunkts für Spiele
                String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=2022&team_ids[]=7&per_page=10";
                //String gamesApiUrl = "https://www.balldontlie.io/api/v1/players";

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

        // HTML-Tabelle erstellen
        StringBuilder htmlTable = new StringBuilder();
        htmlTable.append("<table><tr><th>Game ID</th><th>Season</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr>");

        // Schleife durch die Spieldaten und Erstellen der Tabellenzeilen
        for (JsonNode gameNode : gamesNode) {
            int gameId = gameNode.get("id").asInt();
            String season = gameNode.get("season").asText();
            String homeTeam = gameNode.get("home_team").get("full_name").asText();
            String visitorTeam = gameNode.get("visitor_team").get("full_name").asText();
            int homeScore = gameNode.get("home_team_score").asInt();
            int visitorScore = gameNode.get("visitor_team_score").asInt();

            htmlTable.append("<tr><td>").append(gameId).append("</td><td>").append(season).append("</td><td>")
                    .append(homeTeam).append("</td><td>").append(visitorTeam).append("</td><td>")
                    .append(homeScore).append("</td><td>").append(visitorScore).append("</td></tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }
}
