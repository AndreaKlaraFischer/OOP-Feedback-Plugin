package adapters;

import answers.Answer;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;
import org.kohsuke.github.GHLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GHIssueToAnswerAdapter implements BaseAdapter<GHIssue, Answer> {
    @Override
    public Answer transform(GHIssue fromObject) {
            return new Answer(fromObject.getId(), getAnswer(fromObject), getTutorName(fromObject), fromObject.getClosedAt());
    }

    private String getAnswer(GHIssue issue) {
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
            if(comments.indexOf(comment) > 0) {
                result.append("\n");
            }
            result.append(comment.getBody());
        }
        answers.add(result.toString());
        return result.toString();
    }

    private String getTutorName(GHIssue issue) {
        Collection<GHLabel> labels = null;
        String result = null;
        try {
            labels = issue.getLabels();
            for(GHLabel label : labels) {
                if(label.getName().charAt(0) != '_' ) {
                    result = label.getName();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
