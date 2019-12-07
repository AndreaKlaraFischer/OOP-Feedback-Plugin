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
    public GHRepository repo;
    //23.11. Versuch, es nicht statisch zu machen
    public List<GHIssue> issueList;
    private List<Integer> issueIDList;
    public List<GHIssue> allClosedIssueList;
    public  ArrayList<GHIssue> closedIssueList;
    public AnswerList answerList;
    public Controller controller;
    private ToolWindow toolWindow;

    public int answerId;
    public int answerNumber;

    public GitHubModel(Controller controller, ToolWindow toolWindow) throws IOException {
        this.controller = controller;
        this.toolWindow = toolWindow;
        issueList = new ArrayList<>();

        //11.11. und 23.11.
        allClosedIssueList = new ArrayList<>();
        //15.11.
        answerList = controller.answerList;
        this.adapter = new GHIssueToAnswerAdapter();
        //TODO: Steht dad da an der richtigen Stelle?
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
            System.out.println(e.toString());
            //controller.sendRequestScreen.showNoInternetWarning(); //TODO NPE beheben!
            //TODO: Hier irgendwie noch einen sinnvollereren Catch, vielleicht einen Balloon mit "Bitte verbinde dich mit dem Internet"
        }
    }

    //23.11.
    public List<GHIssue> getIssuesBySavedIds(List<Integer> issueIDList) throws IOException {
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

    public void createIssue(String title, String body, String labelCategory, String labelTask, String labelBranchName, String labelScreenshot) {
        try {
            issue = repo.createIssue(title).create();
            issue.setBody(body);
            System.out.println(labelCategory + labelTask + labelBranchName);
            issue.addLabels(labelCategory, labelTask, labelBranchName, labelScreenshot);
            controller.XMLFileReader.modifyXMLRequests(issue.getNumber());
            System.out.println("issue-getId(): " + issue.getNumber());
            issueList.add(issue);
        } catch (IOException e) {
            System.out.println("excepti + " + e.toString());
        }
    }

   //(Sicherheitskopie ist in VSCode)
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


    //Das hier ist die abgespeckte Version von "matchRequestAndAnswer" --> Das soll bei Programmstart aufgerufen werden
    public List<Answer> getAlreadyAnsweredRequestsOnProgramStart(ArrayList<Integer> savedAnswers, List<GHIssue> sentIssues) throws IOException {
        List<Answer> answeredRequests = new ArrayList<>();
        for (GHIssue ghIssue : sentIssues) {
            for (Integer savedAnswer : savedAnswers) {
                if (ghIssue.getNumber() == savedAnswer) {
                    //sentIssues.add(ghIssue);
                    Answer answer = adapter.transform(ghIssue);
                    answeredRequests.add(answer);
                }
            }
        }
        return answeredRequests;
    }

    public void matchRequestAndAnswer(List<GHIssue> issueList, List<GHIssue> closedIssueList) throws IOException, GitAPIException, BadLocationException {
        for (GHIssue ghIssue : issueList) {
            for (GHIssue closedIssue : closedIssueList) {
                long idxSent = ghIssue.getId();
                long idXAnswered = closedIssue.getId();
                if (idxSent == idXAnswered) {
                    System.out.println("Found a match!");
                    //Hier wird das Issue in eine Antwort verwandelt
                    //TODO: Falls ich die Liste anzeigen lasse mit den offenen Anfragen, muss an dieser Stelle die Liste verkleinert werden
                    Answer answer = adapter.transform(closedIssue);

                    if(!answerList.containsId(idxSent)) {

                        answerList.add(answer);
                        controller.mailModel.sendMailToStudent(answer.getAnswerMessage());
                        //18.10. Wichtig! Im Repo kann man nach einem Issue nur nur mit der Nummer, also dem fortlaufendem Index suchen
                        answerNumber = answer.getAnswerNumber();
                        controller.XMLFileReader.modifyAnswerList(answerNumber);

                        //Get Branch über issueLabel
                        matchIssueWithBranch(closedIssue);
                        controller.gitModel.cloneBranch(matchIssueWithBranch(closedIssue));
                        controller.onNewAnswerData();
                        //28.11. auskommentiert wegen NullpointerException
                        //02.12. BUG - für Studienzwecke sollen die Tutoren bitte immer was kommentieren
                       // controller.answerDetailScreen1.activateOpenCodeButton();
                        //controller.openRequestsModel.decrementOpenRequestsNumber();

                        controller.logData("Anfrage beantwortet, Nummer: " + answer.getAnswerNumber() );
                    }
                }
            }
        }
    }

    //27.11.
    //getBranchName kann ich nicht verwenden, weil die Uhrzeit sich da immer ändert.
    private String matchIssueWithBranch(GHIssue closedIssue) {
        Collection<GHLabel> labels;
        String branchName = "";
        try {
            labels = closedIssue.getLabels();
            for(GHLabel label : labels) {
                if(label.getName().charAt(0) == '_' ) {
                //Remove first character
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

    //TODO verknüpfen, reparieren!!
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
