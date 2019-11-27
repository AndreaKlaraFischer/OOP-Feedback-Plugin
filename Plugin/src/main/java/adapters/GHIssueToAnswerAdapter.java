package adapters;

import android.os.SystemPropertiesProto;
import answers.Answer;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHLabel;
import org.kohsuke.github.GHUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
            if (comments.indexOf(comment) > 0) {
                result.append("\n");
            }
            result.append(comment.getBody());
        }
        answers.add(result.toString());
        return result.toString();
    }

    private String getTutorName(GHIssue issue) throws IOException {
        System.out.println("Closed or not? - " + issue.getState());
        String result = "";

        try {
            System.out.println("Assignes: " + issue.getAssignees());
            //result = getAssignee(issue);
            String assignee = getAssignee(issue);
            result = mapGitHubUsernameWithRealName(assignee);
            System.out.println("closedBy: " + issue.getClosedBy().getLogin());
        } catch (java.lang.NullPointerException npe) {
            if (getAssignee(issue).length() != 0) {
                result = getAssignee(issue);
            } else {
                result = "Anonyme*r Tutor*in";
            }
        }
        return result;
    }

    //TODO: Das weniger hardcoden
    private String mapGitHubUsernameWithRealName(String assignee) {
        String result = "";
        switch (assignee) {
            case "AndreaKlaraFischer":
                result = "Andrea Fischer";
                break;
            case "OOP-Feedback-Test":
                result = "Default";
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
