package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class DirkFrontend {

    @GetMapping("/players")
    public String getPlayers() {
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

                // Schließen der Verbindung
                connection.disconnect();

                // Rückgabe der Antwort als JSON
                return response.toString();
            } else {
                // Schließen der Verbindung
                connection.disconnect();

                // Rückgabe einer Fehlermeldung
                return "Fehler: " + responseCode;
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Rückgabe einer Fehlermeldung
            return "Fehler bei der Anfrage";
        }
    }
}
