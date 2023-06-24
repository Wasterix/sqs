package project.UnitTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.ParseJsonResponse;


import java.io.IOException;
import java.util.List;

public class ParseJsonResponseTestFinal {

    @Test
    public void testParseJsonResponseRESTAPI() throws IOException {
        // Mock JSON-Antwort
        String jsonResponse = "{\"data\":[{\"id\":1,\"season\":\"2022-2023\",\"date\":\"2023-01-01T00:00:00Z\",\"home_team\":{\"full_name\":\"Boston Celtics\"},\"visitor_team\":{\"full_name\":\"Los Angeles Lakers\"},\"home_team_score\":110,\"visitor_team_score\":108},{\"id\":2,\"season\":\"2022-2023\",\"date\":\"2023-02-01T00:00:00Z\",\"home_team\":{\"full_name\":\"Boston Celtics\"},\"visitor_team\":{\"full_name\":\"Brooklyn Nets\"},\"home_team_score\":115,\"visitor_team_score\":120}]}";
        String inputName = "Boston Celtics";

        // Erwartete HTML-Tabellen
        String expectedHomeTable = "<h2>Heimspiele</h2><table><tr><th>Game Number</th><th>Game ID</th><th>Season</th><th>Date</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr><tr><td>1</td><td>1</td><td>2022-2023</td><td>2023-01-01</td><td>Boston Celtics</td><td>Los Angeles Lakers</td><td>110</td><td>108</td></tr><tr><td>2</td><td>2</td><td>2022-2023</td><td>2023-02-01</td><td>Boston Celtics</td><td>Brooklyn Nets</td><td>115</td><td>120</td></tr></table>";
        String expectedVisitorTable = "<h2>Auswärtsspiele</h2><table><tr><th>Game Number</th><th>Game ID</th><th>Season</th><th>Date</th><th>Home Team</th><th>Visitor Team</th><th>Home Score</th><th>Visitor Score</th></tr></table>";

        // Objektmapper für die JSON-Konvertierung
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON-Antwort in Java-Objekt konvertieren
        JsonNode gamesNode = objectMapper.readTree(jsonResponse).get("data");

        // ParseJsonResponse-Objekt erstellen
        ParseJsonResponse parser = new ParseJsonResponse();

        // parseJasonResponseRESTAPI-Methode aufrufen
        String result = parser.parseJasonResponseRESTAPI(jsonResponse, inputName);

        // HTML-Tabellen aus dem Ergebnis extrahieren
        String actualHomeTable = result.substring(result.indexOf("<h2>Heimspiele</h2>"), result.indexOf("<h2>Auswärtsspiele</h2>"));
        String actualVisitorTable = result.substring(result.indexOf("<h2>Auswärtsspiele</h2>"));

        // Überprüfen, ob die erwarteten und tatsächlichen HTML-Tabellen übereinstimmen
        Assert.assertEquals(expectedHomeTable, actualHomeTable);
        Assert.assertEquals(expectedVisitorTable, actualVisitorTable);
    }
}
