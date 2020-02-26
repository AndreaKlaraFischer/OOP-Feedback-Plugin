package adapters;

import answers.Answer;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GHIssueToAnswerAdapter implements BaseAdapter<GHIssue, Answer> {

    //issue gets transformed to an answer
    @Override
    public Answer transform(GHIssue issue) {
        return new Answer(issue.getNumber(), issue.getId(), getAnswerText(issue), getTutorName(issue), issue.getClosedAt(), issue.getBody());
    }

    /*Get the first comment on the issue to get the answer to the request
    Only the first one, because the following one should be feedback for feedback
    Could be adapted so the answer could consist of more than one comment */
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
            result.append(comments.get(0).getBody());
        }
        if(result.length() == 0) {
            result.append("Ohne Text");
        }
        answers.add(result.toString());
        return result.toString();
    }

    //The name of the tutor is added to his answer, it gets created here
    private String getTutorName(GHIssue issue) {
        String result;
        try {
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

    //Here, the usernames and the real names of the tutors get mapped, coded especially for this semester
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
            case "tina-e":
                result = "Martina Emmert";
                break;
            case "erikblank":
                result = "Erik Blank";
                break;
            case "FlorinSchwappach":
                result = "Florin Schwappach";
                break;
            case "alexanderbazo":
                result = "Alexander Bazo";
                break;
            default:
                result = "Tutorenteam";
        }
        return result;
    }
}
