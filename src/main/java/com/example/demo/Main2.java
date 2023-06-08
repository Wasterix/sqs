package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main2 {
    public static void main(String[] args) {
        try {
            // URL der API-Endpunkts für Spiele
            String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=2018&team_ids[]=7";

            // Erstellen der URL-Objekts
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
                // Hier kannst du die Antwort verarbeiten und die Daten in einer Tabelle anzeigen

                // JSON-Daten in Java-Objekte konvertieren
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");

                // Ausgabe der Daten in einer Tabelle
                System.out.println("Game ID | Season | Home Team | Visitor Team | Home Score | Visitor Score");
                for (JsonNode gameNode : gamesNode) {
                    int gameId = gameNode.get("id").asInt();
                    String season = gameNode.get("season").asText();
                    String homeTeam = gameNode.get("home_team").get("full_name").asText();
                    String visitorTeam = gameNode.get("visitor_team").get("full_name").asText();
                    int homeScore = gameNode.get("home_team_score").asInt();
                    int visitorScore = gameNode.get("visitor_team_score").asInt();

                    System.out.printf("%7d | %-6s | %-12s | %-12s | %-10d | %-12d%n",
                            gameId, season, homeTeam, visitorTeam, homeScore, visitorScore);
                }
            } else {
                System.out.println("Fehler: " + responseCode);
            }

            // Schließen der Verbindung
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
