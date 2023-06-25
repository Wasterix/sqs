package project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GamesHandler implements HttpHandler {
    private final String teamId;
    private final String season;
    private final String input_name;
    private final ParseJsonResponse jsonResponseParser;

    public GamesHandler(String teamId, String season, String input_name) {
        this.teamId = teamId;
        this.season = season;
        this.input_name = input_name;
        this.jsonResponseParser = new ParseJsonResponse();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=" + season + "&team_ids[]=" + teamId + "&per_page=82";
            URL url = new URL(gamesApiUrl);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String jsonResponse = readResponse(connection);

                // Überprüfen auf leeren JSON-Response
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");
                String htmlTable = jsonResponseParser.parseJsonResponseRESTAPI(jsonResponse, input_name, season);

                setResponseHeaders(exchange);
                writeResponse(exchange, htmlTable);
            } else {
                sendErrorResponse(exchange, responseCode, "Fehler: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
            sendErrorResponse(exchange, 500, "Fehler: " + e.getMessage());
        }
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    private void setResponseHeaders(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, 0);  // 0 als Content-Length
    }

    private void writeResponse(HttpExchange exchange, String htmlTable) throws IOException {
        try (OutputStream outputStream = exchange.getResponseBody()) {
            int chunkSize = 1024;
            byte[] data = htmlTable.getBytes();
            int offset = 0;
            while (offset < data.length) {
                int length = Math.min(chunkSize, data.length - offset);
                outputStream.write(data, offset, length);
                offset += length;
            }
            // Ausgabe des Response auf der Konsole
            System.out.println("Response: " + htmlTable);
        }
    }

    private void sendErrorResponse(HttpExchange exchange, int statusCode, String errorMessage) throws IOException {
        exchange.sendResponseHeaders(statusCode, errorMessage.length());
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(errorMessage.getBytes());
        }
    }
}
