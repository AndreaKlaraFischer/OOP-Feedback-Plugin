package adapters;

import answers.Answer;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GHIssueToAnswerAdapter implements BaseAdapter<GHIssue, Answer> {


    @Override
    public Answer transform(GHIssue issue) throws IOException {
        return new Answer(issue.getNumber(), issue.getId(), getAnswerText(issue), getTutorName(issue), issue.getClosedAt());
    }

    private String getAnswerText(GHIssue issue) {
        //Hier werden die Kommentare geholt
        ArrayList<String> answers = new ArrayList<>();
        List<GHIssueComment> comments = null;
        try {
            comments = issue.getComments();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder result = new StringBuilder();
        for (GHIssueComment comment : comments) {
            //wenn mehr als ein Kommentar vorhanden, dann zusammen bauen, trennen mit Absatz
            //TODO: RegEx, damit Feedback für Feedback nicht geholt wird bei Programmstart
            result.append(comments.get(0).getBody());
            //27.11. auskommentiert. Es soll nur der erste Kommentar geholt werden, um dem ganzen Feedback für Feedback entgegen zu wirken
            //if (comments.indexOf(comment) > 0) {
               // result.append("\n");
            //}
           // result.append(comment.getBody());
        }
        //TODO: Images rauslöschen!! Das ist aber alles schon in der "Answer" Klasse gemacht worden...
        if(result.length() == 0) {
            result.append("Ohne Text");
        }
        answers.add(result.toString());
        return result.toString();
    }

    private String getTutorName(GHIssue issue) throws IOException {
        String result;
        try {
            System.out.println("Assignes: " + issue.getAssignees());
            //String assignee = getAssignee(issue);
            String assignee = issue.getAssignee().getLogin();
            result = mapGitHubUsernameWithRealName(assignee);

            if(issue.getAssignees().size() == 0) {
                result = "Anonyme*r Tutor*in";
            }
        } catch (Exception e) {
            result = "Anonyme*r Tutor*in";

        }
        return result;
    }

    private String mapGitHubUsernameWithRealName(String assignee) {
        String result = "";
        switch (assignee) {
            case "AndreaKlaraFischer":
                result = "Andrea Fischer";
                break;
            case "OOP-Feedback-Test":
                result = "Default";
                break;
            case "eichingertim":
                result = "Tim Eichinger";
                break;
            case "kappa4head":
                result = "Lukas Jackermeier";
                break;
            case "realdegrees" :
                result = "Fabian Schebera";
                break;
            case "msms":
                result = "Martina Emmert";
                break;
            case "hfhhf":
                result = "Erik Blank";
                break;
            case "hfehe":
                result = "Florin Schwappach";
                break;
            case "hgwgw":
                result = "Alexander Bazo";
                break;
        }
        return result;
    }

    private String getAssignee(GHIssue issue) {
        String result = "";
        for (int i = 0; i < issue.getAssignees().size(); i++) {
            result = issue.getAssignees().get(i).getLogin();
            System.out.println("In Forschleife mit Assignees: " + result);
        }
        return result;
    }
}
