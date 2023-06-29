/*package UnitTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import project.HttpServerManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class HttpServerManagerTest {
    private HttpServerManager httpServerManager;
    private String teamId = "7";
    private String inputSeason = "2015";
    private String year = "2016";

    @Before
    public void setup() {
        httpServerManager = new HttpServerManager();
        httpServerManager.startHttpServer(teamId, inputSeason, year);
    }

    @After
    public void teardown() {
        httpServerManager.stopHttpServer();
    }

    @Test
    public void testServerResponseCode() throws IOException {
        // Verbinden mit dem HTTP-Server und Überprüfen des Response-Codes
        URL url = new URL("http://localhost:8000/id=" + teamId + "&season=" + inputSeason + "/" + year);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        assertEquals(200, responseCode);

        connection.disconnect();
    }
}
*/
