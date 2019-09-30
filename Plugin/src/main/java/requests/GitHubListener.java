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

    public  GitHubListener() {

    }


//TODO: Polling ?
    public void waitForNewAnswers() {
     //TODO: alle 5 Minuten schauen --> Ist in den letzten 5 Minuten eines von meinen Issues geschlossen worden?
     //TODO: Wenn issue state geschlossen, dann TutorAnswerModel.sendAnswer
        //Test aus Stackoverflow: Funktioniert!
        try {
         while (true) {
             checkIssueState();
             Thread.sleep(Constants.THREAD_MILLISECONDS);
         }
        } catch (InterruptedException e) {
         System.out.println("Thread hat funktioniert");
         e.printStackTrace();
        }
     }

    private void checkIssueState() {

    }


}
