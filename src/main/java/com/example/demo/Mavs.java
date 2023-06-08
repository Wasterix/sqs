package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Mavs {
    public static void main(String[] args) {
        try {
            // URL der API-Endpunkts für Teams
            String teamsApiUrl = "https://www.balldontlie.io/api/v1/teams/7";

            // Erstellen der URL-Objekts
            URL url = new URL(teamsApiUrl);

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
                // Hier kannst du die Antwort verarbeiten und den Kader der Dallas Mavericks extrahieren
                // Du kannst die Team-ID der Dallas Mavericks finden und dann die Spielerdaten entsprechend filtern
                // Angenommen, die Team-ID der Dallas Mavericks ist 7:
                System.out.println("Aktueller Kader der Dallas Mavericks:");

                // Weitere Anfrage für den Kader der Dallas Mavericks
                String playersApiUrl = "https://www.balldontlie.io/api/v1/players?player=Nowitzki";

                // Erneutes Öffnen der Verbindung für die Spieleranfrage
                connection = (HttpURLConnection) new URL(playersApiUrl).openConnection();
                connection.setRequestMethod("GET");

                // Überprüfen des HTTP-Statuscodes für die Spieleranfrage
                responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Lesen der Antwort für die Spieleranfrage
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    response = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Parsing der JSON-Antwort für die Spieleranfrage
                    jsonResponse = response.toString();

                    // JSON-Daten in Java-Objekte konvertieren
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode playersNode = objectMapper.readTree(jsonResponse).get("data");

                    // Ausgabe der Spieler in Tabellenform
                    System.out.println("ID | Vorname | Nachname | Position");
                    for (JsonNode playerNode : playersNode) {
                        int playerId = playerNode.get("id").asInt();
                        String firstName = playerNode.get("first_name").asText();
                        String lastName = playerNode.get("last_name").asText();
                        String position = playerNode.get("position").asText();

                        System.out.printf("%-3d | %-7s | %-8s | %-8s%n", playerId, firstName, lastName, position);
                    }
                } else {
                    System.out.println("Fehler bei der Spieleranfrage: " + responseCode);
                }
            } else {
                System.out.println("Fehler bei der Teamanfrage: " + responseCode);
            }

            // Schließen der Verbindung
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
