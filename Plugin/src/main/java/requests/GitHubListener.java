package requests;
// Hier die "rückwärtige" Github Kommunikation rein: Polling,...

//GitHub API: Methode closedBy();

//TODO: Status von Issue wissen
//persistente Datenspeicherung

import org.kohsuke.github.*;

public class GitHubListener {
 /*public void checkForNewAnswers() {
     while (true) {
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
         }
     }*/
     //Wenn issue nicht offen, zeige Notification
     //https://www.codota.com/code/java/classes/org.kohsuke.github.GHIssue
 public static boolean isOpen(GHIssue issue) {
    GHIssueState state = issue.getState();
    if (state == null) {
      return true;
    }
    return state == GHIssueState.OPEN;
  }

  

}
