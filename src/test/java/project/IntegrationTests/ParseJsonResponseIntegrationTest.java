package project.IntegrationTests;

import org.junit.jupiter.api.Test;
import project.ParseJsonResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParseJsonResponseIntegrationTest {

    @Test
    public void testParseJsonResponseRESTAPI() throws IOException {
        String jsonResponse = "{\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"season\": \"2022-2023\",\n" +
                "      \"date\": \"2023-01-01T18:00:00Z\",\n" +
                "      \"home_team\": {\n" +
                "        \"full_name\": \"Team A\"\n" +
                "      },\n" +
                "      \"visitor_team\": {\n" +
                "        \"full_name\": \"Team B\"\n" +
                "      },\n" +
                "      \"home_team_score\": 100,\n" +
                "      \"visitor_team_score\": 90\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"season\": \"2022-2023\",\n" +
                "      \"date\": \"2023-02-01T18:00:00Z\",\n" +
                "      \"home_team\": {\n" +
                "        \"full_name\": \"Team A\"\n" +
                "      },\n" +
                "      \"visitor_team\": {\n" +
                "        \"full_name\": \"Team C\"\n" +
                "      },\n" +
                "      \"home_team_score\": 110,\n" +
                "      \"visitor_team_score\": 95\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"season\": \"2022-2023\",\n" +
                "      \"date\": \"2023-03-01T18:00:00Z\",\n" +
                "      \"home_team\": {\n" +
                "        \"full_name\": \"Team B\"\n" +
                "      },\n" +
                "      \"visitor_team\": {\n" +
                "        \"full_name\": \"Team A\"\n" +
                "      },\n" +
                "      \"home_team_score\": 95,\n" +
                "      \"visitor_team_score\": 100\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ParseJsonResponse parser = new ParseJsonResponse();
        String result = parser.parseJsonResponseRESTAPI(jsonResponse, "Team A", "2022-2023");

        String expected = "<meta charset=\"UTF-8\">" +
                "<h2>Heimspiele</h2>" +
                "<table>" +
                "<tr><th>Game Number</th><th>Game ID</th><th>Season</th><th>Date</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr>" +
                "<tr><td>1</td><td>1</td><td>2022-2023</td><td>2023-01-01</td><td>Team A</td><td>Team B</td><td>100</td><td>90</td></tr>" +
                "<tr><td>2</td><td>2</td><td>2022-2023</td><td>2023-02-01</td><td>Team A</td><td>Team C</td><td>110</td><td>95</td></tr>" +
                "</table>" +
                "<meta charset=\"UTF-8\">" +
                "<h2>Auswärtsspiele</h2>" +
                "<table>" +
                "<tr><th>Game Number</th><th>Game ID</th><th>Season</th><th>Date</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr>" +
                "<tr><td>1</td><td>3</td><td>2022-2023</td><td>2023-03-01</td><td>Team B</td><td>Team A</td><td>95</td><td>100</td></tr>" +
                "</table>";

        assertEquals(expected, result);
    }
}
