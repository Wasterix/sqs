package project;

import org.junit.jupiter.api.Test;
import outdated.NBASeasonViewer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBASeasonViewerTestSingle {

    @Test
    public void testGetInput_ValidInput() {
        String inputName = "Boston Celtics";
        String inputSeason = "2021";
        String expectedId = "2";
        String expectedSeason = "2021";
        String expectedYear = "2022";

        String input = inputName + System.lineSeparator() + inputSeason;
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);

        NBASeasonViewer viewer = new NBASeasonViewer();
        viewer.getInput();

        assertEquals(expectedId, viewer.getInputId());
        assertEquals(expectedSeason, viewer.getInputSeason());
        assertEquals(expectedYear, viewer.getYear());

        String expectedOutput = "Gib mir einen vollständigen Teamnamen: " +
                "Welche Saison soll angezeigt werden: ";
        assertEquals(expectedOutput, outputStream.toString());

        System.setIn(System.in);
        System.setOut(System.out);
    }
}
