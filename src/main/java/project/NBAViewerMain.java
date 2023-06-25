package project;

import outdated.NBASeasonViewer;

public class NBAViewerMain {
    public static void main(String[] args) {
        NBASeasonViewer viewer = new NBASeasonViewer();
        viewer.run();
    }
/*
    public void run() {
        InputHandler inputHandler = new InputHandler();
        DataProcessor dataProcessor = new DataProcessor();
        HttpServerManager httpServerManager = new HttpServerManager();

        String inputName = inputHandler.getInputName();
        String inputSeason = inputHandler.getInputSeason();

        String teamId = LookupTable.getTeamId(inputName);
        String year = dataProcessor.calculateYear(inputSeason);

        dataProcessor.processInput(inputName, teamId, inputSeason, year);
        httpServerManager.startHttpServer(teamId, inputSeason, year);
    }*/
}
