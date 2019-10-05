package requests;

import org.kohsuke.github.GHCommit;

import java.util.Date;

public class Answer {
    private String answerMessage;
    private String tutorName;
    private Date answerDate; //closed at --> Das würde ich mir auch noch woanders herholen!

    private GitHubModel gitHubModel;

    public Answer (){
        this.gitHubModel = new GitHubModel();
        //answerMessage
        tutorName = gitHubModel.tutorName;
        answerDate = gitHubModel.answeredAt;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }
    public String  getTutorName() {
        return tutorName;
    }
    public Date getAnswerDate() {
        return answerDate;
    }


    //TODO: Das wird noch ein riesiges Todo!
    public GHCommit.File getAnswerCode() {
        return null;
    }

}
