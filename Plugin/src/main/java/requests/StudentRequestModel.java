package requests;

import com.intellij.openapi.project.Project;
import config.Constants;
import gui.SendRequestScreen;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHIssue;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import static requests.GitModel.repoPath;


//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private SendRequestScreen sendRequestScreen;
    private GitHubModel gitHubModel;
    private IDCreator idCreator;
    private List<GHIssue> issueList;
    public static File[] screenshots;
    public File screenshotFolder;
    public String branchName;

    private GitModel gitModel;

    public static Git git;
    private String clonedRepoPath;


    //Konstruktor
    public StudentRequestModel(Project project) {
        this.idCreator = new IDCreator();
        this.gitHubModel = new GitHubModel();
        this.gitModel = new GitModel(project);
        //this.sendRequestScreen = new SendRequestScreen(project);
        /*String clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
        repoPath = new File(clonedRepoPath);*/
        clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;

    }

    public void chooseFiles() {
        try {
            //https://www.mkyong.com/swing/java-swing-jfilechooser-example/
            //Damit der FileChooser so aussieht wie im jeweiligen Betriebssystem und nicht wie der von IntelliJ
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser screenshotUploader = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            screenshotUploader.setDialogTitle("Screenshots auswählen");
            screenshotUploader.setMultiSelectionEnabled(true);
            screenshotUploader.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = screenshotUploader.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                screenshots = screenshotUploader.getSelectedFiles();
                System.out.println("Files Found\n");
                Arrays.asList(screenshots).forEach(file -> {
                    if (file.isFile()) {
                        System.out.println(file.getName());
                    }
                });
            }
            //Wenn es Screenshots gibt, dann Ordner erstellen und Screenshots hinzufügen
            if (screenshots.length > 0) {
                addScreenshotsToBranch();
            }
        } catch (Exception e) {

        }

    }


    //Angepasst an dieses Codebeispiel
    //https:stackoverflow.com/questions/33168230/jgit-create-new-local-branch-and-push-to-remote-branch-does-not-exist-on-remote
    public void createAndPushBranch () {
        branchName = idCreator.createRequestID();
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
            deleteScreenshotFolder();

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

    public void addScreenshotsToBranch(){
        try {
            screenshotFolder = new File(clonedRepoPath + Constants.SCREENSHOT_FOLDER);
            screenshotFolder.mkdir();

            for (int i = 0; i < screenshots.length; i++) {
                System.out.println(clonedRepoPath + Constants.SCREENSHOT_FOLDER + "/" + screenshots[i].getName());
                File screenshot = new File(clonedRepoPath + Constants.SCREENSHOT_FOLDER + "/" + screenshots[i].getName());
                org.apache.commons.io.FileUtils.copyFile(screenshots[i], screenshot);
                //FileUtils.copyFile(screenshots[i], screenshot);
                System.out.println("Schwurbel" + screenshot);
            }
        } catch (Exception e) {

        }
    }

    private void deleteScreenshotFolder() {
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(screenshotFolder);
            //FileUtils.delete(screenshotFolder, FileUtils.RECURSIVE);
            //FileUtils.deleteDirectory();
        } catch (Exception e) {
            System.out.println("Ordner löschen " + e.toString());
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