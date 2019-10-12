package answers;

import org.kohsuke.github.GHCommit;

import java.util.Date;

public class Answer {
    private String answerMessage;
    private String tutorName;
    private Date answerDate;

    public Answer(String message, String tutor, Date date){
       answerMessage = message;
       tutorName = tutor;
       answerDate = date;
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


    //TODO: Das wird noch ein riesiges Todo! --> Wird dann im AnnotatedCodeModel geregelt
    public GHCommit.File getAnswerCode() {
        return null;
    }

    @Override
    public String toString() {
        return "Msg " + answerMessage + " tutorname " + tutorName + " answerdate" + answerDate;
    }
}
