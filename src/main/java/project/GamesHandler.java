package project;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GamesHandler implements HttpHandler {
    private final String teamId;
    private final String season;
    private final String input_name;
    private ParseJsonResponse jsonResponse2 = new ParseJsonResponse();



    public GamesHandler(String teamId, String season, String input_name){
        this.teamId = teamId;
        this.season = season;
        this.input_name = input_name;

    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {


        try {
            // URL des API-Endpunkts für Spiele
            String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=" + season + "&team_ids[]=" + teamId + "&per_page=82";

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
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parsing der JSON-Antwort
                String jsonResponse = response.toString();
                //String htmlTable = parseJsonResponse(jsonResponse);
                String htmlTable = jsonResponse2.parseJasonResponseRESTAPI(jsonResponse, input_name);


                // Setzen der HTML-Antwort
                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, 0);  // 0 als Content-Length

                // Schreiben der Daten in den OutputStream in Teilen
                OutputStream outputStream = exchange.getResponseBody();
                int chunkSize = 1024;
                byte[] data = htmlTable.getBytes();
                int offset = 0;
                while (offset < data.length) {
                    int length = Math.min(chunkSize, data.length - offset);
                    outputStream.write(data, offset, length);
                    offset += length;
                }

                // Schließen des OutputStreams
                outputStream.close();
            } else {
                // Setzen der Fehlerantwort
                String errorMessage = "Fehler: " + responseCode;
                exchange.sendResponseHeaders(responseCode, errorMessage.length());
                OutputStream outputStream = exchange.getResponseBody();
                outputStream.write(errorMessage.getBytes());
                outputStream.close();
            }

            // Schließen der Verbindung
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            // Setzen der Fehlerantwort
            String errorMessage = "Fehler: " + e.getMessage();
            exchange.sendResponseHeaders(500, errorMessage.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(errorMessage.getBytes());
            outputStream.close();
        }
    }}