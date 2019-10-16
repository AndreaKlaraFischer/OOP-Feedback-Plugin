package answers;

import requests.GitHubModel;

import java.util.ArrayList;


//http://www2.hawaii.edu/~takebaya/ics111/jtable_custom/jtable_custom.html
public class AnswerList {
    private GitHubModel gitHubModel;
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

    public boolean containsId(String keyId) {
        for (Answer answer : answerList) {
            if (answer.getAnswerId() == Long.parseLong(keyId)) {
                return true;
            }
        }
        return false;
    }
}
