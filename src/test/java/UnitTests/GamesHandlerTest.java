/*package UnitTests;

import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.Test;
import project.GamesHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.mockito.Mockito.*;

class GamesHandlerTest {

    @Test
    void handle_shouldReturnValidResponse() throws IOException {
        // Arrange
        String teamId = "7";
        String season = "2015";
        String inputName = "Dallas Mavericks";
        String expectedResponse = "https://www.balldontlie.io/api/v1/games?seasons[]=2015/2016&team_ids[]=7&per_page=82";

        HttpExchange exchange = mock(HttpExchange.class);
        HttpURLConnection connection = mock(HttpURLConnection.class);
        InputStream inputStream = mock(InputStream.class);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        when(exchange.getResponseBody()).thenReturn(outputStream);
        when(connection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
        when(connection.getInputStream()).thenReturn(inputStream);
        when(inputStream.read(any(byte[].class))).thenReturn(-1); // Mocking end of stream
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1); // Mocking end of stream
        when(inputStream.available()).thenReturn(expectedResponse.getBytes().length);
        when(inputStream.readAllBytes()).thenReturn(expectedResponse.getBytes());

        URL url = new URL("https://www.balldontlie.io/api/v1/games?seasons[]=" + season + "&team_ids[]=" + teamId + "&per_page=82");
        when(url.openConnection()).thenReturn(connection);

        GamesHandler gamesHandler = new GamesHandler(teamId, season, inputName);

        // Act
        gamesHandler.handle(exchange);

        // Assert
        verify(exchange, times(1)).getResponseHeaders();
        verify(exchange, times(1)).sendResponseHeaders(eq(200), eq(0));
        verify(outputStream, times(1)).write(eq(expectedResponse.getBytes()));
    }
}
*/