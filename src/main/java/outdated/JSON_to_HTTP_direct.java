package outdated;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

public class JSON_to_HTTP_direct {
    public static void main(String[] args) {
        try {
            // Erstellen des HTTP-Servers auf Port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Definieren des HTTP-Endpunkts
            server.createContext("/JSON_ans", new GamesHandler());

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/JSON_ans");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class GamesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // URL des API-Endpunkts für Spiele
                String gamesApiUrl = "https://www.balldontlie.io/api/v1/games?seasons[]=2022&team_ids[]=7&per_page=82";

                // Erstellen des URL-Objekts
                URL url = new URL(gamesApiUrl);

                // Öffnen der Verbindung
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Überprüfen des HTTP-Statuscodes
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Setzen der HTTP-Antwortheader
                    exchange.getResponseHeaders().add("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, 0);  // 0 als Content-Length

                    // Lesen der Antwort und Senden an den HTTP-Server
                    InputStream inputStream = connection.getInputStream();
                    OutputStream outputStream = exchange.getResponseBody();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
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
        }
    }
}

