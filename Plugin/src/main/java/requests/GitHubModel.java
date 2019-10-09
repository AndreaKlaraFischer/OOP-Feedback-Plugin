package requests;

import adapters.GHIssueToAnswerAdapter;
import answers.Answer;
import config.Constants;
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
    public HashMap<String, Answer> requestIdsAndAnswers = new HashMap<>();

    public Date answeredAt;
    public String tutorName;

    public GitHubModel() {
        issueList = new ArrayList<>();
        System.out.println("Hund und Sau\n\n\n");
        allClosedIssueList = new ArrayList<>();
        answers = new ArrayList<>();
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
        //Das steht hier auch nur zu Testzwecken
        //TODO: An die richtige Stelle
        getAnswers();
    }

    public void getAnswers() {
        //Muss leer sein am Anfang.
        answers = new ArrayList<>();

        for (GHIssue ghIssue : closedIssueList) {
            try {
                //TODO: das auch noch als Liste?
                //Wann wurde geantwortet? Das kommt zur Antwortklasse
                answeredAt = ghIssue.getClosedAt();
                System.out.println(ghIssue.getClosedAt());
                //Hier wird der Name des Tutors abgefragt
                tutorName = "";
                List<GHLabel> labels = (List<GHLabel>) ghIssue.getLabels();
                for(GHLabel label : labels) {
                    if(label.getName().charAt(0) != '_' ) {
                        tutorName = label.getName();
                    }
                }
                System.out.println(tutorName);

                //Hier werden die Kommentare geholt
                List<GHIssueComment> comments = ghIssue.getComments();
                StringBuilder result = new StringBuilder();
                for (GHIssueComment comment : comments) {
                    //wenn mehr als ein Kommentar vorhanden, dann zusammen bauen, trennen mit Absatz
                   if(comments.indexOf(comment) > 0) {
                      result.append("\n");
                   }
                   result.append(comment.getBody());
                }
                answers.add(result.toString());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        System.out.println(answers);
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
                    System.out.println(valueAnswer);
                    requestIdsAndAnswers.put(keyId, valueAnswer);
                    System.out.println(requestIdsAndAnswers);
                }
            }
        }
    }
}

