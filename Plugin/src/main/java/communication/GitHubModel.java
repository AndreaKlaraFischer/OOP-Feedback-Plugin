package communication;

import adapters.GHIssueToAnswerAdapter;
import answers.Answer;
import answers.AnswerList;
import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.github.*;

import javax.swing.text.BadLocationException;
import java.io.IOException;
import java.util.*;
import com.intellij.openapi.wm.ToolWindow;


public class GitHubModel {
    private GitHub github;
    private GHIssue issue;
    private GHIssueComment feedbackComment;
    private GHIssueToAnswerAdapter adapter;
    private GHRepository repo;
    public List<GHIssue> issueList;
    private List<Integer> issueIDList;
    private List<GHIssue> allClosedIssueList;
    public  ArrayList<GHIssue> closedIssueList;
    private AnswerList answerList;
    public Controller controller;
    private ToolWindow toolWindow;

    public int answerNumber;

    public GitHubModel(Controller controller, ToolWindow toolWindow) {
        this.controller = controller;
        this.toolWindow = toolWindow;
        issueList = new ArrayList<>();

        allClosedIssueList = new ArrayList<>();
        answerList = controller.answerList;
        this.adapter = new GHIssueToAnswerAdapter();
        connectWithRepo();
    }

    public List<GHIssue> updateIssueList() throws IOException {
        issueList = getIssuesBySavedIds(controller.getSavedRequests());
        return issueList;
    }

    private void connectWithRepo() {
        //https://github-api.kohsuke.org/apidocs/org/kohsuke/github/GitHub.html
        try {
            github = GitHub.connectUsingPassword(Constants.REPO_LOGIN, Constants.REPO_PASSWORD);
            repo = github.getRepository(Constants.REPO_NAME);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private List<GHIssue> getIssuesBySavedIds(List<Integer> issueIDList) throws IOException {
        List<GHIssue> sentIssues = new ArrayList<>();
        for (Integer integer : issueIDList) {
            GHIssue sentIssue = repo.getIssue(integer);
            sentIssues.add(sentIssue);
        }
        return sentIssues;
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

    //requests get transformed to an issue and added on GitHub
    public void createIssue(String title, String body, String labelCategory, String labelTask, String labelBranchName, String labelScreenshot) {
        try {
            issue = repo.createIssue(title).create();
            issue.setBody(body);
            issue.addLabels(labelCategory, labelTask, labelBranchName, labelScreenshot);
            controller.XMLFileReader.modifyXMLRequests(issue.getNumber());
            issueList.add(issue);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }


   public List<GHIssue> filterOwnClosedIssues() throws IOException {
       List<GHIssue> closedIssueList = new ArrayList<>();
       issueList = updateIssueList();
       for (GHIssue ghIssue : issueList) {
           if(ghIssue.getState() == GHIssueState.CLOSED) {
               closedIssueList.add(ghIssue);
           }
       }
       return closedIssueList;
   }


    //On programstart it has to be checked if there are answers to the sent requests (saved in config file) and show the table with corresponding answers without showing the notification
    public List<Answer> getAlreadyAnsweredRequestsOnProgramStart(ArrayList<Integer> savedAnswers, List<GHIssue> sentIssues) throws IOException {
        List<Answer> answeredRequests = new ArrayList<>();
        for (GHIssue ghIssue : sentIssues) {
            for (Integer savedAnswer : savedAnswers) {
                if (ghIssue.getNumber() == savedAnswer) {
                    Answer answer = adapter.transform(ghIssue);
                    answeredRequests.add(answer);
                }
            }
        }
        return answeredRequests;
    }

    //Check, if there are new answers to the sent requests, happens every 10 seconds
    public void matchRequestAndAnswer(List<GHIssue> issueList, List<GHIssue> closedIssueList) throws IOException, GitAPIException, BadLocationException {
        for (GHIssue ghIssue : issueList) {
            for (GHIssue closedIssue : closedIssueList) {
                long idxSent = ghIssue.getId();
                long idXAnswered = closedIssue.getId();
                //requests and closed issues get compared by ID
                if (idxSent == idXAnswered) {
                    //issue gets transformed to answer
                    Answer answer = adapter.transform(closedIssue);
                    //the answer should only be added once
                    //If a new answer is found, the answer should be added to the table and a mail with the answer content is sent to the student
                    if(!answerList.containsId(idxSent)) {
                        answerList.add(answer);
                        controller.mailModel.sendMailToStudent(answer.getAnswerMessage());
                        answerNumber = answer.getAnswerNumber();
                        controller.XMLFileReader.modifyAnswerList(answerNumber);
                        //Get Branch over issueLabel
                        matchIssueWithBranch(closedIssue);
                        controller.gitModel.cloneBranch(matchIssueWithBranch(closedIssue));
                        controller.onNewAnswerData();
                        controller.logData("Anfrage beantwortet, Nummer: " + answer.getAnswerNumber() );
                    }
                }
            }
        }
    }


    private String matchIssueWithBranch(GHIssue closedIssue) {
        Collection<GHLabel> labels;
        String branchName = "";
        try {
            labels = closedIssue.getLabels();
            for(GHLabel label : labels) {
                if(label.getName().charAt(0) == '_' ) {
                //Remove first character of label(_) to compare label with branchname
                    branchName = "refs/heads/" + label.getName().substring(1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return branchName;
    }

    public void matchFeedbackAndRequest(int answerNumber) {
        try {
            issue = repo.getIssue(answerNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNotHelpfulFeedbackLabel() {
        String notHelpfulLabel = Constants.FEEDBACK_LABEL_NOT_HELPFUL;
        try {
            issue.addLabels(notHelpfulLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNeutralLabel() {
        String neutralLabel = Constants.FEEDBACK_LABEL_NEUTRAL;
        try {
            issue.addLabels(neutralLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHelpfulFeedbackLabel() {
        String helpfulLabel = Constants.FEEDBACK_LABEL_HELPFUL;
        try {
            issue.addLabels(helpfulLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProblemSolvedLabel() {
        String problemSolvedLabel = Constants.PROBLEM_SOLVED_SUCCESSFULLY;
        try {
            issue.addLabels(problemSolvedLabel);
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
}
