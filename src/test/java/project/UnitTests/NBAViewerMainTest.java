package project.UnitTests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import project.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NBAViewerMainTest {

    private HttpServerManager httpServerManager;

    @AfterEach
    public void tearDown() {
        if (httpServerManager != null) {
            httpServerManager.stopHttpServer();
        }
    }

    @Test
    public void testRun() throws InterruptedException {
        // Mock-Objekte erstellen
        InputHandler inputHandler = new InputHandler();
        DataProcessor dataProcessor = new DataProcessor();
        httpServerManager = new HttpServerManager();

        // Testdaten
        String inputName = "Los Angeles Lakers";
        String inputSeason = "2015/2016";

        // Erwartete Werte
        String teamId = "14";
        String year = "2015";

        NBAViewerMain viewerMain = new NBAViewerMain(inputHandler, dataProcessor, httpServerManager);
        viewerMain.run(inputName, inputSeason);

        // Wartezeit, um sicherzustellen, dass der HTTP-Server antworten kann
        Thread.sleep(2000);

        // Überprüfen der Ergebnisse
        assertEquals(inputName, inputHandler.getInputName());
        assertEquals(inputSeason, inputHandler.getInputSeason());
        assertEquals(teamId, LookupTable.getTeamId(inputName));
        assertEquals(year, dataProcessor.calculateYear(inputSeason));
        assertNotNull(httpServerManager.getServer()); // Überprüfen, ob der HTTP-Server gestartet wurde
    }

    private static class NBAViewerMain {
        private final InputHandler inputHandler;
        private final DataProcessor dataProcessor;
        private final HttpServerManager httpServerManager;

        public NBAViewerMain(InputHandler inputHandler, DataProcessor dataProcessor, HttpServerManager httpServerManager) {
            this.inputHandler = inputHandler;
            this.dataProcessor = dataProcessor;
            this.httpServerManager = httpServerManager;
        }

        public void run(String inputName, String inputSeason) {
            String teamId = LookupTable.getTeamId(inputName);
            String year = dataProcessor.calculateYear(inputSeason);

            dataProcessor.processInput(inputName, teamId, inputSeason, year);
            httpServerManager.startHttpServer(teamId, inputSeason, year);
        }
    }
}
