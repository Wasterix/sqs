package IntegrationTests;

import org.junit.jupiter.api.Test;
import project.DataProcessor;
import project.LookupTable;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class DataProcessorIntegrationTest {
    @Test
    public void testProcessInput_ValidInput_PrintsCorrectOutput() {
        String inputName = "Brooklyn Nets";
        String teamId = LookupTable.getTeamId(inputName);
        String inputSeason = "2021";
        String year = "2022";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.processInput(inputName, teamId, inputSeason, year);

        System.out.flush();
        System.setOut(originalPrintStream);

        String expectedOutput = "Die Id der Brooklyn Nets lautet: 3" + System.lineSeparator()
                + "Ausgewählte Saison: 2021/2022" + System.lineSeparator();
        String actualOutput = outputStream.toString();

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCalculateYear_ValidInput_ReturnsYear() {
        String inputSeason = "2015/2016";
        String expectedYear = "2015";

        DataProcessor dataProcessor = new DataProcessor();
        String year = dataProcessor.calculateYear(inputSeason);

        assertEquals(expectedYear, year);
    }
}

