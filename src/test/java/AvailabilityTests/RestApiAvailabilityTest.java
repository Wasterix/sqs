package AvailabilityTests;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RestApiAvailabilityTest {

    public static void main(String[] args) {
        String apiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=2015&team_ids[]=7&per_page=82";

        try {
            boolean isApiAvailable = checkApiAvailability(apiUrl);

            if (isApiAvailable) {
                System.out.println("REST-API ist verfügbar.");
            } else {
                System.out.println("REST-API ist nicht verfügbar.");
            }
        } catch (IOException e) {
            System.out.println("Fehler beim Testen der REST-API-Verfügbarkeit: " + e.getMessage());
        }
    }

    private static boolean checkApiAvailability(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        return (responseCode == 200);
    }
}
