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

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;

//TODO: Im Plugin Branch pullen
//TODO: Der muss im Controller noch aufgerufen werden!
//TODO: Liste mit ALLEN Issues aus dem Repo holen

public class GitHubListener {



    public  GitHubListener() {

    }

    public boolean isOpen(GHIssue issue) {
        GHIssueState state = issue.getState();
        System.out.println(issue.getState());
        if (state == null) {
            return true;
        }
        return state == GHIssueState.OPEN;
    }

//TODO: Polling ?
 public void checkForNewAnswers() {
       //TODO: Wenn issue state geschlossen, dann TutorAnswerModel.sendAnswer
     /*while (true) {
         try {
             incomingMessages.addAll(fullPoll());
             System.out.println("waiting 6 seconds");
             //perform this operation in a loop
             Thread.sleep(6000);
         } catch (InterruptedException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }*/
     }



}
