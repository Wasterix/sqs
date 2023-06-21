package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Id_to_Teamname {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int input_id;

        do {
            System.out.print("Gib eine ganze Zahl zwischen 1 und 30 ein: ");
            String input = scanner.nextLine();

            try {
                input_id = Integer.parseInt(input);

                if (input_id < 1 || input_id > 30) {
                    System.out.println("Fehler: Die Zahl muss zwischen 1 und 30 liegen.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Fehler: Ungültige Eingabe. Bitte gib eine ganze Zahl ein.");
            }
        } while (true);

        //System.out.println("Die eingegebene Zahl ist: " + input_id);

        scanner.close();
        System.out.println();

        try {
            String teamsApiUrl = "https://www.balldontlie.io/api/v1/teams/" + input_id;
            URL url = new URL(teamsApiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                String jsonResponse = convertStreamToString(inputStream);
                printTeamTable(jsonResponse);
            } else {
                System.out.println("Fehler: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler: " + e.getMessage());
        }
    }

    private static String convertStreamToString(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static void printTeamTable(String jsonResponse) {
        int idStartIndex = jsonResponse.indexOf("\"id\":") + 5;
        int idEndIndex = jsonResponse.indexOf(",", idStartIndex);
        String id = jsonResponse.substring(idStartIndex, idEndIndex);

        int cityStartIndex = jsonResponse.indexOf("\"city\":\"") + 8;
        int cityEndIndex = jsonResponse.indexOf("\"", cityStartIndex);
        String city = jsonResponse.substring(cityStartIndex, cityEndIndex);

        int fullNameStartIndex = jsonResponse.indexOf("\"full_name\":\"") + 13;
        int fullNameEndIndex = jsonResponse.indexOf("\"", fullNameStartIndex);
        String fullName = jsonResponse.substring(fullNameStartIndex, fullNameEndIndex);

        int nameStartIndex = jsonResponse.indexOf("\"name\":\"") + 8;
        int nameEndIndex = jsonResponse.indexOf("\"", nameStartIndex);
        String name = jsonResponse.substring(nameStartIndex, nameEndIndex);


        System.out.println(id);
        System.out.println(city);
        System.out.println(name);
        System.out.println(fullName);
    }
}
