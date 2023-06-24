package project.UnitTests;

import org.junit.jupiter.api.Test;
import project.DataProcessor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataProcessorTestFinal {

    @Test
    public void testProcessInput() {
        DataProcessor dataProcessor = new DataProcessor();
        String inputName = "Los Angeles Lakers";
        String teamId = "14";
        String inputSeason = "2015/2016";
        String year = "2015";
        String expectedOutput = "Die Id der Los Angeles Lakers lautet: 14\nAusgewählte Saison: 2015/2016/2015\n";

        // Redirect System.out to capture the output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        dataProcessor.processInput(inputName, teamId, inputSeason, year);

        // Reset System.out
        System.setOut(System.out);

        // Check if the output contains the expected information
        String actualOutput = outputStream.toString().trim();
        assertTrue(actualOutput.contains("Die Id der " + inputName + " lautet: " + teamId));
        assertTrue(actualOutput.contains("Ausgewählte Saison: " + inputSeason + "/" + year));
    }

    @Test
    public void testCalculateYear() {
        DataProcessor dataProcessor = new DataProcessor();
        String inputSeason = "2015/2016";
        String expectedYear = "2015";

        String actualYear = dataProcessor.calculateYear(inputSeason);

        assertEquals(expectedYear, actualYear);
    }

}
