package answers;

import org.kohsuke.github.GHCommit;

import java.util.Date;

public class Answer {
    private int answerNumber;
    private long answerId;
    private String answerMessage;
    private String tutorName;
    private Date answerDate;

    public Answer(int number, long id, String message, String tutor, Date date) {
        answerNumber = number;
        answerId = id;
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
    //TODO: Datum muss noch ordentlich angezeigt werden wieder
    public Date getAnswerDate() {
        return answerDate;
    }
    public long getAnswerId() {
        return answerId;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    //TODO: Das wird noch ein riesiges Todo! --> Wird dann im AnnotatedCodeModel geregelt
    public GHCommit.File getAnswerCode() {
        return null;
    }

    @Override
    public String toString() {
        return "Msg " + answerId + answerMessage + " tutorname " + tutorName + " answerdate" + answerDate;
    }
}
