package com.example.demo.command_line;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Dirk {
    public static void main(String[] args) {
        try {
            // URL der API-Endpunkts
            String apiUrl = "https://www.balldontlie.io/api/v1/players?search=Nowitzki";

            // Erstellen der URL-Objekts
            URL url = new URL(apiUrl);

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
                System.out.println("Fehler: " + responseCode);
            }

            // Schließen der Verbindung
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
