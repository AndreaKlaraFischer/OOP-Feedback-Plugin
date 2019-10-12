package controller;
// Hier die "rückwärtige" Github Kommunikation rein: Polling,...

//TODO: Aktion ausführen, wenn Issue geschlossen wurde --> darauf lauschen
//TODO: Code schicken, wenn Issue geschlossen
//TODO: Pullen
//TODO: Sichtbarmachen, dass Antwort da ist
//persistente Datenspeicherung

//TODO: Im Plugin Branch pullen

import config.Constants;
import requests.GitHubModel;

public class GitHubListener extends Thread {
    public Controller controller;
    public GitHubModel gitHubModel;

    public void run() {
        try {
            while (true) {
                gitHubModel.getClosedIssueList();
                gitHubModel.matchRequestAndAnswer();
                Thread.sleep(Constants.THREAD_MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public  GitHubListener(Controller controller) {
        this.controller = controller;
        gitHubModel = controller.gitHubModel;
    }


}
