package requests;

import config.Constants;
import gui.SendRequestScreen;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.util.List;

public class GitHubModel {
    private int requestCounter;
    private String studentName;
    private GitHub github;
    private GHRepository repo;
    private List<GHIssue> issueList;

    public GitHubModel() {
        //https://github-api.kohsuke.org/apidocs/org/kohsuke/github/GitHub.html
        try {
            github = GitHub.connectUsingPassword(Constants.REPO_LOGIN, Constants.REPO_PASSWORD);
            repo = github.getRepository(Constants.REPO_NAME);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //TODO: Woher bekomme ich den Studentennamen? --> Alex fragen
        requestCounter = 1;
        studentName = "Xaver Xantippe";
    }

    public void createIssue() {
        try {
            String issueTitle = Constants.ISSUE_TITLE_BEGINNING +
                    Constants.SEPARATOR +
                    requestCounter +
                    Constants.SEPARATOR +
                    Constants.ISSUE_TITLE_MIDDLE +
                    Constants.SEPARATOR +
                    studentName;
            String issueBody = SendRequestScreen.inputMessage;
            String issueLabel = SendRequestScreen.problemCategory;

            GHIssue issue = repo.createIssue(issueTitle).create();
            issue.setBody(issueBody);
            issue.addLabels(issueLabel);
        } catch (IOException e) {
            System.out.println("excepti + " + e.toString());
        }
    }
    //TODO Hier: state vom Issue
    //29.09. 18:15, das funktioniert schon mal.
    //Aufrufen auf Test-Button im MailBoxScreen
    public void getClosedIssueList() {
        try {
            issueList = repo.getIssues(GHIssueState.CLOSED);
            System.out.println(issueList);

        } catch (Exception e) {
            System.out.println("issue list" + e.toString());
        }
    }


}

