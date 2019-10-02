package requests;

import config.Constants;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.kohsuke.github.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GitHubModel {
    private String studentName;
    private GitHub github;
    private GHIssue issue;
    private GHIssueComment ghIssueComment;
    private GHRepository repo;
    private static List<GHIssue> issueList;
    private static List<GHIssue> allClosedIssueList;
    public static List<GHIssue> closedIssueList;
    public static ArrayList<String> answers;

    public GitHubModel() {
        issueList = new ArrayList<>();
        allClosedIssueList = new ArrayList<>();
        answers = new ArrayList<>();


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
            String issueLabel = SendRequestScreen.problemCategory;

            issue = repo.createIssue(issueTitle).create();
            issue.setBody(issueBody);
            issue.addLabels(issueLabel, Long.toString((issue.getId())));
            //Test 30.09. --> Alle erstellten Issues in einer Liste speichern, funktioniert
            //01.10. --> Brauche ich das überhaupt? --> Das könnte ich noch für Status Änderungen verwenden
            issueList.add(issue);
            System.out.println("issueList" + issueList);
            //TODO: Alle Issues an sich noch in eine Liste speichern und Status abfragen?

        } catch (IOException e) {
            System.out.println("excepti + " + e.toString());
        }

        System.out.println("Issue List after add "+issueList);
    }



    //TODO: Wo rufe ich das auf?
    public void getClosedIssueList() {
        try{
            allClosedIssueList = repo.getIssues(GHIssueState.CLOSED);
            filterOwnClosedIssues();
            //closedIssueList = new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Das hat leider nicht funktioniert" + e.toString());
        }
        getAnswers();
    }
    private void getAnswers() {
        //Muss leer sein am Anfang.
        answers = new ArrayList<>();
        for (GHIssue ghIssue : closedIssueList) {
            try {
                List<GHIssueComment> comments = ghIssue.getComments();
                for (GHIssueComment comment : comments) {
                    //TODO: Hier noch die zugehörigen Kommentare zu einem String hinzugefügt --> dritte Schleife?
                    answers.add(comment.getBody());
                }
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
        for(int i = 0; i < allClosedIssueList.size(); i++) {
            for(int j = 0; j< issueList.size(); j++) {
                long idxClosed = allClosedIssueList.get(i).getId();
                long idxIssueList = issueList.get(j).getId();
                if(idxClosed == idxIssueList) {
                    temp.add(allClosedIssueList.get(i));
                }
            }
        }
        closedIssueList = temp;
        System.out.println("My closed issues " + closedIssueList);
    }
}

