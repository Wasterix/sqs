package project;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import outdated.NBASeasonViewer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NBASeasonViewerTestParametrized {

    private static Map<String, Integer> lookupTable;

    @ParameterizedTest
    @MethodSource("provideTeamAndYearCombinations")
    public void testGetInput_ValidInput(String inputName, String inputSeason, String expectedId, String expectedSeason, String expectedYear) {
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

    private static Stream<String[]> provideTeamAndYearCombinations() {
        lookupTable = new HashMap<>();
        lookupTable.put("Atlanta Hawks", 1);
        lookupTable.put("Boston Celtics", 2);
        lookupTable.put("Brooklyn Nets", 3);
        lookupTable.put("Charlotte Hornets", 4);
        lookupTable.put("Chicago Bulls", 5);
        lookupTable.put("Cleveland Cavaliers", 6);
        lookupTable.put("Dallas Mavericks", 7);
        lookupTable.put("Denver Nuggets", 8);
        lookupTable.put("Detroit Pistons", 9);
        lookupTable.put("Golden State Warriors", 10);
        lookupTable.put("Houston Rockets", 11);
        lookupTable.put("Indiana Pacers", 12);
        lookupTable.put("LA Clippers", 13);
        lookupTable.put("Los Angeles Lakers", 14);
        lookupTable.put("Memphis Grizzlies", 15);
        lookupTable.put("Miami Heat", 16);
        lookupTable.put("Milwaukee Bucks", 17);
        lookupTable.put("Minnesota Timberwolves", 18);
        lookupTable.put("New Orleans Pelicans", 19);
        lookupTable.put("New York Knicks", 20);
        lookupTable.put("Oklahoma City Thunder", 21);
        lookupTable.put("Orlando Magic", 22);
        lookupTable.put("Philadelphia 76ers", 23);
        lookupTable.put("Phoenix Suns", 24);
        lookupTable.put("Portland Trail Blazers", 25);
        lookupTable.put("Sacramento Kings", 26);
        lookupTable.put("San Antonio Spurs", 27);
        lookupTable.put("Toronto Raptors", 28);
        lookupTable.put("Utah Jazz", 29);
        lookupTable.put("Washington Wizards", 30);

        String[] teams = lookupTable.keySet().toArray(new String[0]);
        String[] years = generateYearsArray(1980, 2010);

        return Stream.of(teams)
                .flatMap(team -> Stream.of(years).map(year -> new String[]{team, year, lookupTable.get(team).toString(), year}));
    }

    private static String[] generateYearsArray(int startYear, int endYear) {
        int numYears = endYear - startYear + 1;
        String[] years = new String[numYears];

        for (int i = 0; i < numYears; i++) {
            years[i] = String.valueOf(startYear + i);
        }

        return years;
    }
}
