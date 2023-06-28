/*package IntegrationTests;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.HttpServerManager;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpServerManagerIntegrationTest {
    private static final String TEAM_ID = "14";
    private static final String SEASON = "2021";
    private static final String YEAR = "2023";
    private static final String MOCK_RESPONSE = "Mock HTTP Response";

    private HttpServerManager httpServerManager;
    private HttpServer server;

    @BeforeEach
    public void setUp() throws IOException {
        httpServerManager = new HttpServerManager();
        server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/id=" + TEAM_ID + "&season=" + SEASON + "/" + YEAR, new MockGamesHandler());
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop(0);
    }

    @Test
    public void testStartHttpServer_SuccessfulRequest_ResponseContainsExpectedData() throws IOException {
        httpServerManager.startHttpServer(TEAM_ID, SEASON, YEAR);

        String response = sendHttpRequest("http://localhost:8000/id=" + TEAM_ID + "&season=" + SEASON + "/" + YEAR);

        assertEquals(MOCK_RESPONSE, response);
    }

    private String sendHttpRequest(String url) throws IOException {
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return response.toString();
            }
        } else {
            throw new RuntimeException("HTTP request failed with response code: " + responseCode);
        }
    }

    private static class MockGamesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.sendResponseHeaders(200, MOCK_RESPONSE.getBytes().length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(MOCK_RESPONSE.getBytes());
            }
        }
    }
}
*/