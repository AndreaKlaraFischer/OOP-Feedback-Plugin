package requests;

import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHUser;

import java.util.List;

//TODO: Mit ID abgleichen!
public class TutorAnswerModel {

    //Equivalent to request class

    public TutorAnswerModel() {

    }

    public void sendAnswer() {

    }

    private void createAnswer() {
        //TODO: Bestehend aus Uhrzeit, Name, Code und
        //TODO: Pull Code
        //Die Antwort ist der Kommentar unter dem Issue
        try {

        } catch (Exception e){

        }
    }

    //TODO: Geänderten Code commiten
    private void pullCode() {
        try {
           // PullCommand pullCommand = git.pull();
            // pullCommand.call();
        } catch (Exception e){
            System.out.println("Pull Exeption + " + e.toString());
        }
    }

    //getClosedAt()
    //Reports who has closed the issue.
    GHUser getClosedBy() {
        return null;
    }

    List<GHIssueComment> getComments() {
        return null;
    }
}
