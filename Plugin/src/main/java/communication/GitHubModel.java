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
    private GitHub github;
    private GHIssue issue;
    private GHIssueComment feedbackComment;
    private GHIssueToAnswerAdapter adapter;
    public GHRepository repo;
    //11.11. Versuch
    private static List<GHIssue> issueList;
    private List<Long> issueIDList;
    private static List<GHIssue> allClosedIssueList;
    public static ArrayList<GHIssue> closedIssueList;
    public AnswerList answerList;
    public Controller controller;

    public int answerId;
    public int answerNumber;

    public GitHubModel(Controller controller) {
        this.controller = controller;
        issueList = new ArrayList<>();
        //11.11.
        issueIDList = controller.getSavedRequest();
        allClosedIssueList = new ArrayList<>();
        //TODO: Controller
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
        return Constants.ISSUE_TITLE_BEGINNING +
                Constants.SEPARATOR +
                Constants.SEPARATOR +
                studentName +
                Constants.SEPARATOR +
                Constants.ISSUE_TITLE_DATE +
                Constants.SEPARATOR +
                requestDate;
    }

    public void createIssue(String title, String body, String labelCategory, String labelTask, String labelBranchName, String labelScreenshot) {
    //public void createIssue(String title, String body, String labelCategory, String labelBranchName, String labelScreenshot) {
        try {
            issue = repo.createIssue(title).create();
            issue.setBody(body);
            //09.11. Das geht nicht mehr! TODO: Beheben!!!!!!!!!
            //Das geht
           // issue.addLabels("A", "B");
            //Das wird ausgegeben
            System.out.println(labelCategory + labelTask + labelBranchName);
            issue.addLabels(labelCategory, labelTask, labelBranchName, labelScreenshot);
            //issue.addLabels(labelCategory, labelBranchName, labelScreenshot);
            //31.10. Test --> Das geht nicht! Überschreibt die anderen
            //issue.addLabels("testiiiii");
            controller.XMLFileReader.modifyXMLRequests(issue.getId());
            System.out.println("issue-getId(): " + issue.getId());
            issueList.add(issue);

            System.out.println("issueList" + issueList);
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
            //11.11. Versuch, das mit der Liste zu ersetzen, damit es gespeichert und weiterhin angezeigt wird
            //for (Long aLong : issueIDList) {
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

    public void showOwnAnswersOnProgramStart() {

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
                        //30.10.
                        controller.mailModel.sendMailToStudent(answer.getAnswerMessage());
                        //
                        answerList.add(answer);
                        //18.10. Wichtig! Im Repo kann man nach einem Issue nur nur mit der Nummer, also dem fortlaufendem Index suchen
                        answerNumber = answer.getAnswerNumber();
                        controller.onNewAnswerData();
                        controller.answerDetailScreen.activateOpenCodeButton();
                    }
                }
            }
        }
    }

    public void matchFeedbackAndRequest(int answerNumber) {
        try {
            issue = repo.getIssue(answerNumber);
        } catch (IOException e) {
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

    public void setFeedbackText(String prefix, String feedbackMessage) {
        try {
            feedbackComment = issue.comment(prefix + Constants.SEPARATOR + feedbackMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Wenn das beim Screenshot funktioniert, also nur bei Bedarf gelabelt wird, dann sich daran orientieren!
    public void setProblemSolvedLabel() {
        //TODO: Das hier holen! Also irgendwie übergeben oder so.
        //Aber das funktioniert soweit schon mal, dass es an die bestehenden Labels noch mitangehängt wird.
        String problemSolvedLabel = "Problem erfolgreich gelöst!";
        try {
            issue.addLabels(problemSolvedLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
