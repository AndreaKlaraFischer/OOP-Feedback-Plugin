package requests;

import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHIssue;

import java.util.List;

import static requests.GitModel.repoPath;


//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private SendRequestScreen sendRequestScreen;
    private GitHubModel gitHubModel;
    private IDCreator idCreator;
    private SettingScreen settingScreen;
    private ScreenshotModel screenshotModel;
    private List<GHIssue> issueList;

    public String branchName;

    private GitModel gitModel;
    public Controller controller;
    public static Git git;
    public String clonedRepoPath;


    //TODO: Klassen, die Daten als Container abbilden und nicht verändert werden können --> Daten in der Anwendung rumreichen
    //Konstruktor
    public StudentRequestModel(Project project, Controller controller) {
        this.idCreator = new IDCreator();
        this.gitModel = new GitModel(project);
        //this.settingScreen = new SettingScreen();
        this.controller = controller;
        gitHubModel = controller.gitHubModel;
        clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
    }

    //Angepasst an dieses Codebeispiel
    //https:stackoverflow.com/questions/33168230/jgit-create-new-local-branch-and-push-to-remote-branch-does-not-exist-on-remote
    public void createAndPushBranch () {
       // branchName = idCreator.createRequestID();
        //TODO: Das geht hier irgendwie noch nicht
        String studentName = gitHubModel.studentName;
       // branchName = idCreator.createUUID() + "-"+ studentName;
        //10.10. Test
        branchName = idCreator.requestId;
        System.out.println("Branchname" + branchName);
        try {
            // create branch
            git = Git.open(repoPath);
            CreateBranchCommand createBranchCommand = git.branchCreate();
            createBranchCommand.setName(branchName);
            createBranchCommand.call();

            checkoutBranch();
            commitChanges();
            pushToRemote();

            //TODO: Hier funktioniert irgendetwas noch nicht richtig.
            //Ordner wieder löschen: Pro Anfrage nur die jeweiligen Screenshots
            screenshotModel.deleteScreenshotFolder();
            System.out.println("Ich komme hierhin nach dem Deleten");

        } catch (Exception e) {
            System.out.println("zweite Exception branch create" +  e.toString());
        }
    }

    private void pushToRemote() {
        try {
            // push to remote
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD));
            pushCommand.call();
            System.out.println("Wurde erfolgreich gepusht");
        } catch (Exception e) {

        }
    }

    private void checkoutBranch() {
        // checkout branch
        try {
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(branchName);
            checkoutCommand.call();
        } catch (Exception e) {

        }

    }

    private void commitChanges() {
        try {
            // commit
            AddCommand addCommand = git.add();
            addCommand.addFilepattern(".");
            addCommand.call();
            Status status = git.status().call();
            for (String added : status.getAdded()) {
                System.out.println("+ " + added);
            }
            git.commit().setMessage("Anfrage erstellt und geschickt").call();
        } catch (Exception e) {

        }
    }




    //TODO: Hier warten, bis die Files kopiert wurden
    public void sendRequest() {
        //idCreator.createRequestID();
        createAndPushBranch();
        //TODO (Alex): als geschlossenen Task betrachten und in eigene Klassen kapseln
        gitHubModel.createIssue();
    }
}