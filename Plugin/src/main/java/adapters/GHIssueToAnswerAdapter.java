package adapters;

import answers.Answer;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHLabel;

import java.io.IOException;
import java.util.Collection;

public class GHIssueToAnswerAdapter implements BaseAdapter<GHIssue, Answer> {
    @Override
    public Answer transform(GHIssue fromObject) {
        // System.out.println(fromObject.getLabe);
        Answer result = new Answer(fromObject.getBody(), getTutorName(fromObject), fromObject.getClosedAt());
        return result;
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
