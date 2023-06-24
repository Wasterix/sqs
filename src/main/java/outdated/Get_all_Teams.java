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

public class Get_all_Teams {
    public static void main(String[] args) {
        try {
            // Erstellen des HTTP-Servers auf Port 8000
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            // Definieren des HTTP-Endpunkts
            server.createContext("/teams", new TeamsHandler());

            // Starten des HTTP-Servers
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/teams");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TeamsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                // URL des API-Endpunkts für Teams
                String teamsApiUrl = "https://www.balldontlie.io/api/v1/teams";

                // Erstellen des URL-Objekts
                URL url = new URL(teamsApiUrl);

                // Öffnen der Verbindung
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Überprüfen des HTTP-Statuscodes
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Setzen der HTTP-Antwortheader
                    exchange.getResponseHeaders().add("Content-Type", "text/html");
                    exchange.sendResponseHeaders(200, 0);  // 0 als Content-Length

                    // Lesen der Antwort und Senden an den HTTP-Server
                    InputStream inputStream = connection.getInputStream();
                    StringBuilder response = new StringBuilder();
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        response.append(new String(buffer, 0, bytesRead));
                    }
                    inputStream.close();

                    // Extrahieren der ID und des Teamnamens aus der JSON-Antwort
                    String jsonResponse = response.toString();
                    StringBuilder htmlTable = createTable(jsonResponse);

                    // Senden der HTML-Tabelle als HTTP-Antwort
                    OutputStream outputStream = exchange.getResponseBody();
                    outputStream.write(htmlTable.toString().getBytes());
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

        // Hilfsmethode zum Erstellen der HTML-Tabelle mit ID, Stadt, Teamname und vollem Namen
        private static StringBuilder createTable(String jsonResponse) {
            StringBuilder table = new StringBuilder();
            table.append("<h2>Teams</h2>")
                    .append("<table>")
                    .append("<tr>")
                    .append("<th>ID</th>")
                    .append("<th>City</th>")
                    .append("<th>Name</th>")
                    .append("<th>Full Name</th>")
                    .append("</tr>");

            // JSON-Parsing und Erstellen der Tabellenzeilen
            // Hier wird angenommen, dass die JSON-Antwort das Array "data" enthält
            // und jedes Objekt in "data" die Felder "id" und "name" hat
            // Du kannst diesen Code anpassen, wenn die JSON-Struktur anders ist
            // Zum Parsen der JSON-Antwort kannst du z.B. die Bibliothek Gson verwenden
            // Diese Implementierung dient nur zur Veranschaulichung
            // und nicht als vollständiges JSON-Parsing
            // und verwendet einfaches String-Manipulation
            String[] teamsData = jsonResponse.split("\"data\":")[1]
                    .split("\\[")[1]
                    .split("\\]")[0]
                    .split("\\},\\{");
            for (String teamData : teamsData) {
                String id = teamData.split("\"id\":")[1].split(",")[0];
                String city = teamData.split("\"city\":\"")[1].split("\"")[0];
                String name = teamData.split("\"name\":\"")[1].split("\"")[0];
                String full_name = teamData.split("\"full_name\":\"")[1].split("\"")[0];

                table.append("<tr>")
                        .append("<td>").append(id).append("</td>")
                        .append("<td>").append(city).append("</td>")
                        .append("<td>").append(name).append("</td>")
                        .append("<td>").append(full_name).append("</td>")
                        .append("</tr>");
            }

            table.append("</table>");
            return table;
        }
    }
}
