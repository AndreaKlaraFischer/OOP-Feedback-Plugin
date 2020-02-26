package answers;

import java.util.ArrayList;


//http://www2.hawaii.edu/~takebaya/ics111/jtable_custom/jtable_custom.html
public class AnswerList {
    private ArrayList<Answer> answerList;
    public AnswerList() {
        answerList = new ArrayList<>();
    }

    public void add(Answer answer) {
       answerList.add(answer);
    }

    public ArrayList<Answer> getAnswerList() {
        return answerList;
    }

    public boolean containsId(Long idxSent) {
        for (Answer answer : answerList) {
            if (answer.getAnswerId() == idxSent) {
                return true;
            }
        }
        return false;
    }
}
