package requests;
// Hier die "rückwärtige" Github Kommunikation rein: Polling,...

//GitHub API: Methode closedBy();

//TODO: Status von Issue wissen
//TODO: Aktion ausführen, wenn Issue geschlossen wurde
//TODO: Wissen, von wem das Issue geschlossen wurde (Das muss nämlich auch angezeigt werden)
//TODO: Code und Antwort zurück schicken, wenn Issue geschlossen
//TODO: Pullen
//TODO: Sichtbarmachen, dass Antwort da ist
//persistente Datenspeicherung

//TODO: Im Plugin Branch pullen
//TODO: Der muss im Controller noch aufgerufen werden!
//TODO: Liste mit ALLEN Issues aus dem Repo holen

import config.Constants;

public class GitHubListener {
    //Subklasse für Multithreading
    class WaitForAnswerThread extends Thread {
        private GitHubModel gitHubModel;
        public WaitForAnswerThread() {
            gitHubModel = new GitHubModel();
        }
        public void run() {
            try {
                while (true) {
                    gitHubModel.getClosedIssueList();
                    gitHubModel.matchRequestAndAnswer();
                    Thread.sleep(Constants.THREAD_MILLISECONDS);
                }
            } catch (InterruptedException e) {
                System.out.println("Thread hat funktioniert");
                e.printStackTrace();
            }
        }
    }

    public  GitHubListener() {
        WaitForAnswerThread waitForAnswerThread = new WaitForAnswerThread();
        waitForAnswerThread.start();
    }


}
