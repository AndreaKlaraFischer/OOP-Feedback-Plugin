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

    //TODO: Vielleicht doch das benutzen?
    public void readAnswersFromHashMap() {
        System.out.println("readAnswersFromHashMap");
        try {
            answerList = new ArrayList<Answer>(gitHubModel.requestIdsAndAnswers.values());

            System.out.println(answerList.get(0).getAnswerMessage());
        } catch (Exception e) {
            return;
        }

    }
}
