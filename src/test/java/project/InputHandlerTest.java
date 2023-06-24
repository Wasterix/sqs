package project;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class InputHandlerTest {
    private InputHandler inputHandler;

    @Before
    public void setUp() {
        inputHandler = new InputHandler();
    }

    @Test
    public void testGetInputName_ValidTeamName() {
        String teamName = "Boston Celtics";

        // Mocking the user input
        String simulatedUserInput = teamName + "\n";
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String input = inputHandler.getInputName();

        // Restoring the standard input
        System.setIn(savedStandardInputStream);

        Assert.assertEquals(teamName, input);
    }

    @Test
    public void testGetInputName_InvalidTeamName() {
        String invalidTeamName = "Invalid Team";

        // Mocking the user input
        String simulatedUserInput = invalidTeamName + "\nBoston Celtics\n";
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String input = inputHandler.getInputName();

        // Restoring the standard input
        System.setIn(savedStandardInputStream);

        Assert.assertEquals("Boston Celtics", input);
    }

    @Test
    public void testGetInputSeason_ValidSeason() {
        String season = "2020";

        // Mocking the user input
        String simulatedUserInput = season + "\n";
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String input = inputHandler.getInputSeason();

        // Restoring the standard input
        System.setIn(savedStandardInputStream);

        Assert.assertEquals(season, input);
    }

    @Test
    public void testGetInputSeason_InvalidSeason() {
        String invalidSeason = "abcd";

        // Mocking the user input
        String simulatedUserInput = invalidSeason + "\n2022\n";
        InputStream savedStandardInputStream = System.in;
        System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));

        String input = inputHandler.getInputSeason();

        // Restoring the standard input
        System.setIn(savedStandardInputStream);

        Assert.assertEquals("2022", input);
    }
}

