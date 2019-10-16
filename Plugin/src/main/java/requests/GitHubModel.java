package requests;

import adapters.GHIssueToAnswerAdapter;
import answers.Answer;
import answers.AnswerList;
import config.Constants;
import controller.Controller;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class GitHubModel {
    public  String studentName;
    private GitHub github;
    private GHIssue issue;
    private GHIssueComment ghIssueComment;
    private GHIssueToAnswerAdapter adapter;
    public static GHRepository repo;
    private static List<GHIssue> issueList;
    private static List<GHIssue> allClosedIssueList;
    public static ArrayList<GHIssue> closedIssueList;
    public static ArrayList<String> answers;
    public AnswerList answerList;
    public HashMap<String, Answer> requestIdsAndAnswers = new HashMap<>();

    public Date answeredAt;
    public String tutorName;
    public Controller controller;

    public GitHubModel(Controller controller) {
        this.controller = controller;
        issueList = new ArrayList<>();
        System.out.println("Hund und Sau\n\n\n");
        allClosedIssueList = new ArrayList<>();
        answers = new ArrayList<>();
        answerList = new AnswerList();
        this.adapter = new GHIssueToAnswerAdapter();

        //https://github-api.kohsuke.org/apidocs/org/kohsuke/github/GitHub.html
        try {
            github = GitHub.connectUsingPassword(Constants.REPO_LOGIN, Constants.REPO_PASSWORD);
            repo = github.getRepository(Constants.REPO_NAME);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void createIssue() {
        try {
            studentName = SettingScreen.studentNameInput;
            String issueTitle = Constants.ISSUE_TITLE_BEGINNING +
                    Constants.SEPARATOR +
                    Constants.SEPARATOR +
                    studentName;
            String issueBody = SendRequestScreen.inputMessage;
            String issueLabel = Constants.COMMON_LABEL_BEGIN + SendRequestScreen.problemCategory;

            issue = repo.createIssue(issueTitle).create();
            issue.setBody(issueBody);
            issue.addLabels(issueLabel);
            issueList.add(issue);
            System.out.println("issueList" + issueList);
        } catch (IOException e) {
            System.out.println("excepti + " + e.toString());
        }

    }

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
            for (GHIssue value : closedIssueList) {
                long idxSent = ghIssue.getId();
                long idXAnswered = value.getId();
                if (idxSent == idXAnswered) {
                    System.out.println("Found a match!");
                    String keyId = Long.toString(idxSent);
                    Answer valueAnswer = adapter.transform(value);
                    System.out.println(valueAnswer.getAnswerMessage());

                    // containsID() gibts noch gar nicht! schadi :(
                    // dann werd ich sie wohl schreiben müssen, kann ja nicht so schwer sein ¯\_(ツ)_/¯
                    if(!answerList.containsId(keyId)) {
                        requestIdsAndAnswers.put(keyId, valueAnswer);
                        answerList.add(valueAnswer);
                        controller.onNewAnswerData();
                        System.out.println(requestIdsAndAnswers);
                    }
                }
            }
        }
    }
}

