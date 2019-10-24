package communication;


import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHIssue;
import requests.IDCreator;
import requests.ScreenshotModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GitModel {
    private GitHubModel gitHubModel;
    private IDCreator idCreator;
    private List<GHIssue> issueList;

    public String branchName;

    public Controller controller;
    public static Git git;
    public String clonedRepoPath;
    public static File repoPath;
    public String projectPath;
    public File projectFolder;
    public String srcFolderPath;

    public GitModel(Project project, Controller controller ) {
        //23.10.
        projectPath = project.getBasePath();
        projectFolder = new File(projectPath);
        srcFolderPath = projectPath + Constants.SRC_FOLDER;
        //
        clonedRepoPath = projectPath + Constants.CLONED_REPO_FOLDER;
        //23.10.
        //String clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
        clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
        repoPath = new File(clonedRepoPath);

        this.controller = controller;
        idCreator = controller.idCreator;
        gitHubModel = controller.gitHubModel;
    }

    /* However the destination location is chosen, explicitly through your code or by JGit,
    the designated directory must either be empty or must not exist.
    Otherwise, an exception will be thrown.*/
    public void cloneRepo() throws IOException {
        try {
            //TODO: Das vllt in eine While-Schleife packen?
            if(!repoPath.exists()) {
                //TODO: Hier noch sagen, dass nur der jeweilige Branch geklont werden muss! --> Das noch der AnswerKlasse hinzufügen?
                Git.cloneRepository()
                        .setURI(Constants.REPO_URL)
                        .setCredentialsProvider( new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD ) )
                        .setDirectory(repoPath)
                        .call();
                System.out.println("Repo wurde erfolgreich geklont");
            } else {
                System.out.println("Ordner exisitiert bereits");
            }
        } catch (Exception e){
            System.out.println("Repo wurde nicht erfolgreich geklont" + e.toString());
            FileUtils.deleteDirectory(repoPath);
        }
    }

    public void createAndPushBranch () throws IOException, GitAPIException {
        // branchName = idCreator.createRequestID();
        //TODO: Das geht hier irgendwie noch nicht --> Woher kommt jetzt der StudentName?
        //String studentName = gitHubModel.studentName;
        //branchName = idCreator.createUUID() + "-"+ studentName;
        branchName = idCreator.createUUID();
        controller.codeModel.addCodeToBranch();
        System.out.println("Branchname" + branchName);

        //git = Git.open(repoPath);
        git = Git.open(repoPath);
        CreateBranchCommand createBranchCommand = git.branchCreate();
        createBranchCommand.setName(branchName);
        createBranchCommand.call();

        checkoutBranch();
        commitChanges();
        pushToRemote();

        //TODO: Hier funktioniert irgendetwas noch nicht richtig.
        //Ordner wieder löschen: Pro Anfrage nur die jeweiligen Screenshots
        controller.screenshotModel.deleteScreenshotFolder();
    }

    private void pushToRemote() {
        try {
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD));
            pushCommand.call();
            System.out.println("Wurde erfolgreich gepusht");
        } catch (Exception e) {

        }
    }


    private void checkoutBranch() {
        try {
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(branchName);
            checkoutCommand.call();
        } catch (Exception e) {


        }
    }

    //TODO: Der Code muss auch komplett mitgeschickt werden!
    private void commitChanges() {
        try {
            AddCommand addCommand = git.add();
            addCommand.addFilepattern(".");

            //TODO: Hier src folder adden

            addCommand.call();
            Status status = git.status().call();
            for (String added : status.getAdded()) {
                System.out.println("+ " + added);
            }
            git.commit().setMessage("Anfrage erstellt und geschickt").call();
        } catch (Exception e) {

        }
    }
}
