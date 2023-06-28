package UnitTests;

import org.junit.jupiter.api.Test;
import project.DataProcessor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataProcessorTest {

    @Test
    void processInput_validInput_printsCorrectOutput() {
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        PrintStream originalPrintStream = System.out;
        System.setOut(printStream);

        // Create an instance of project.DataProcessor
        DataProcessor dataProcessor = new DataProcessor();

        // Define test input values
        String inputName = "Boston Celtics";
        String teamId = "2";
        String inputSeason = "2015";
        String year = "2016";

        // Call the processInput method
        dataProcessor.processInput(inputName, teamId, inputSeason, year);

        // Restore System.out
        System.out.flush();
        System.setOut(originalPrintStream);

        // Get the printed output
        String printedOutput = outputStream.toString().trim();

        // Define the expected output
        String expectedOutput = "Die Id der Boston Celtics lautet: 2" +
                "Ausgewählte Saison: 2015/2016";

        // Assert that the printed output matches the expected output
        assertEquals(expectedOutput, printedOutput);
    }

    @Test
    void calculateYear_validInput_returnsCorrectYear() {
        // Create an instance of project.DataProcessor
        DataProcessor dataProcessor = new DataProcessor();

        // Define test input value
        String inputSeason = "2015/2016";

        // Call the calculateYear method
        String year = dataProcessor.calculateYear(inputSeason);

        // Define the expected year
        String expectedYear = "2015";

        // Assert that the returned year matches the expected year
        assertEquals(expectedYear, year);
    }
}

