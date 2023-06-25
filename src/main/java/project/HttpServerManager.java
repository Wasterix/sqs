package project;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerManager {
    private HttpServer server;

    public void startHttpServer(String teamId, String inputSeason, String year) {
        try {
            server = HttpServer.create(new InetSocketAddress(8000), 0);
            String inputName = null;
            server.createContext("/id=" + teamId + "&season=" + inputSeason + "/" + year, new GamesHandler(teamId, inputSeason, null));
            server.start();
            System.out.println("HTTP-Server läuft auf http://localhost:8000/id=" + teamId + "&season=" + inputSeason + "/" + year);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HttpServer getServer() {

        return server;
    }

    public void stopHttpServer() {
    }


}
