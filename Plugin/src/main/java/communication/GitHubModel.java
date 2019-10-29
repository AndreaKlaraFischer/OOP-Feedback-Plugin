package communication;

import actions.BalloonPopup;
import adapters.GHIssueToAnswerAdapter;
import answers.Answer;
import answers.AnswerList;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import config.Constants;
import controller.Controller;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.*;


public class GitHubModel {
    public  String studentName;
    private GitHub github;
    private GHIssue issue;
    private GHIssueComment feedbackComment;
    private GHIssueToAnswerAdapter adapter;
    public static GHRepository repo;
    private static List<GHIssue> issueList;
    private static List<GHIssue> allClosedIssueList;
    public static ArrayList<GHIssue> closedIssueList;
    public AnswerList answerList;
    public Controller controller;

    public int answerId;
    public int answerNumber;
    private String requestDate;

    public GitHubModel(Controller controller) {
        this.controller = controller;
        issueList = new ArrayList<>();
        allClosedIssueList = new ArrayList<>();
        answerList = new AnswerList();
        this.adapter = new GHIssueToAnswerAdapter();

        connectWithRepo();
    }

    private void connectWithRepo() {
        //https://github-api.kohsuke.org/apidocs/org/kohsuke/github/GitHub.html
        try {
            github = GitHub.connectUsingPassword(Constants.REPO_LOGIN, Constants.REPO_PASSWORD);
            repo = github.getRepository(Constants.REPO_NAME);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public String createIssueTitle(String studentName, String requestDate) {
        //TODO: Woher bekomme ich die Uhrzeit? Das muss wahrscheinlich beim Buttonklick passieren
        return Constants.ISSUE_TITLE_BEGINNING +
                Constants.SEPARATOR +
                Constants.SEPARATOR +
                studentName +
                Constants.SEPARATOR +
                Constants.ISSUE_TITLE_DATE +
                Constants.SEPARATOR +
                requestDate;
    }

    public void createIssue(String title, String body, String labelCategory, String labelTask, String labelBranchName) {
        try {
            issue = repo.createIssue(title).create();
            issue.setBody(body);
            issue.addLabels(labelCategory, labelTask, labelBranchName);
            issueList.add(issue);
            System.out.println("issueList" + issueList);
            //TODO: Hier Methode aus ScreenshotModel aufrufen! - ne quatsch, dem body hinzufügen!
        } catch (IOException e) {
            System.out.println("excepti + " + e.toString());
        }
    }

    //TODO: Das noch versuchen, ob das anders geht! Ohne alle zu holen!
    public void getClosedIssueList() {
        try{
            allClosedIssueList = repo.getIssues(GHIssueState.CLOSED);
            filterOwnClosedIssues();
            //closedIssueList = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Das hat leider nicht funktioniert" + e.toString());
        }
    }

    //TODO: Noch asynchron und persistent machen!
    //TODO: Notiz: Funktioniert erst, wenn man mit dem Repo verbunden ist. Also vllt woanders hinpacken?
    private void filterOwnClosedIssues() {
        ArrayList<GHIssue> temp = new ArrayList<>();
        System.out.println("allClosedIssueList");
        System.out.println(allClosedIssueList);
        System.out.println("issueList");
        System.out.println(issueList);
        for (GHIssue ghIssue : allClosedIssueList) {
            for (GHIssue value : issueList) {
                long idxClosed = ghIssue.getId();
                long idxIssueList = value.getId();
                if (idxClosed == idxIssueList) {
                    temp.add(ghIssue);
                }
            }
        }
        closedIssueList = temp;
        System.out.println("My closed issues " + closedIssueList);
    }

    public void matchRequestAndAnswer() {
        for (GHIssue ghIssue : issueList) {
            for (GHIssue closedIssue : closedIssueList) {
                long idxSent = ghIssue.getId();
                long idXAnswered = closedIssue.getId();
                if (idxSent == idXAnswered) {
                    System.out.println("Found a match!");
                    int issueNumber = closedIssue.getNumber();
                    //Hier wird das Issue in eine Antwort verwandelt
                    Answer answer = adapter.transform(closedIssue);
                    System.out.println(answer.getAnswerMessage());

                    if(!answerList.containsId(idxSent)) {
                        answerList.add(answer);
                        //18.10. Wichtig! Im Repo kann man nach einem Issue nur nur mit der Nummer, also dem fortlaufendem Index suchen
                        answerNumber = answer.getAnswerNumber();
                        controller.onNewAnswerData();
                    }
                }
            }
        }
    }

    public void matchFeedbackAndRequest(int answerNumber) {
        try {
            System.out.println(answerId);
            issue = repo.getIssue(answerNumber);
            System.out.println(repo.getIssue(answerNumber));
            //repo.getIssue(answerId);
        } catch (IOException e) {
            System.out.println("Das hat nicht funktioniert, matchFeedbackAndRequest");
            e.printStackTrace();
        }
    }

    public void setNotHelpfulFeedbackLabel() {
        String notHelpfulLabel = Constants.COMMON_LABEL_BEGIN + Constants.FEEDBACK_LABEL_NOT_HELPFUL;
        try {
            issue.addLabels(notHelpfulLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO: Überlegen, ob ich das überhaupt brauche
    public void setNeutralLabel() {
        String neutralLabel = Constants.COMMON_LABEL_BEGIN + Constants.FEEDBACK_LABEL_NEUTRAL;
        try {
            issue.addLabels(neutralLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHelpfulFeedbackLabel() {
        String helpfulLabel = Constants.COMMON_LABEL_BEGIN + Constants.FEEDBACK_LABEL_HELPFUL;
        try {
            issue.addLabels(helpfulLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //18.10. Versuch, das mit Parameter zu lösen. --> Funktioniert soweit.
    public void setFeedbackText(String prefix, String feedbackMessage) {
        try {
            feedbackComment = issue.comment(prefix + Constants.SEPARATOR + feedbackMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO (Test mit false funktioniert noch nicht)
    public boolean checkIfChangesInCode() {
        return false;
    }

}
