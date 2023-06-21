package project;

import org.junit.jupiter.api.Test;
import java.io.IOException;

class Mavs2018Test {

    @Test
    void testParseJsonResponse() throws IOException {
        String jsonResponse = "{\"data\":[{\"id\":1,\"season\":\"2022\",\"home_team\":{\"full_name\":\"Mavericks\"},\"visitor_team\":{\"full_name\":\"Opponent\"},\"home_team_score\":100,\"visitor_team_score\":90}]}";
        String expectedHtmlTable = "<table><tr><th>Game ID</th><th>Season</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr>" +
                "<tr><td>1</td><td>2022</td><td>Mavericks</td><td>Opponent</td><td>100</td><td>90</td></tr></table>";

        String actualHtmlTable = Mavs2018.parseJsonResponse(jsonResponse);

        Assertions.assertEquals(expectedHtmlTable, actualHtmlTable);
    }
}
