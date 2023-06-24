package project.UnitTests;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import project.HttpServerManager;

import java.io.IOException;
import java.net.InetSocketAddress;

import static org.junit.jupiter.api.Assertions.*;

public class HttpServerManagerTest {
    private HttpServerManager httpServerManager;
    private HttpServer httpServer;
    private String testTeamId = "testTeamId";
    private String testInputSeason = "testInputSeason";
    private String testYear = "testYear";
    private String expectedEndpoint = "/id=testTeamId&season=testInputSeason/testYear";
    private String expectedURL = "http://localhost:8000/id=testTeamId&season=testInputSeason/testYear";

    @BeforeEach
    public void setup() throws IOException {
        httpServerManager = new HttpServerManager();
        httpServer = HttpServer.create(new InetSocketAddress(8000), 0);
    }

    @AfterEach
    public void tearDown() {
        httpServer.stop(0);
    }

    @Test
    public void testStartHttpServer() {
        httpServerManager.startHttpServer(testTeamId, testInputSeason, testYear);

        assertNotNull(httpServerManager.getServer().getAddress());

        String actualURL = "http://" + httpServerManager.getServer().getAddress().getHostName() +
                ":" + httpServerManager.getServer().getAddress().getPort() + expectedEndpoint;

        assertEquals(expectedURL, actualURL);
    }


    @Test
    public void testStopHttpServer() {
        httpServerManager.startHttpServer(testTeamId, testInputSeason, testYear);

        assertNotNull(httpServerManager.getServer().getAddress());

        httpServerManager.stopHttpServer();

        assertNull(httpServerManager.getServer().getAddress());
    }

    // Neue Methode zur Überprüfung der Server-URL
    @Test
    public void testGetServerURL() {
        httpServerManager.startHttpServer(testTeamId, testInputSeason, testYear);

        String actualURLAsString = httpServerManager.getServer().toString();

        assertEquals(expectedURL, actualURLAsString);
    }

}
