package requests;

import com.intellij.openapi.project.Project;
import gui.SendRequestScreen;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.*;

import java.io.File;
import java.io.IOException;



//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private static final String REPO_URL = "https://github.com/OOP-Feedback/OOP-Feedback.git";
    private static final String CLONED_REPO_FOLDER ="/ClonedRepo";
    private static final String REPO_LOGIN = "OOP-Feedback";
    private static final String REPO_PASSWORD = "FredFeedback1920";
    private SendRequestScreen sendRequestScreen;
    private IDCreator idCreator;
    private File repoPath;

    //Konstruktor
    public StudentRequestModel(Project project) {
        this.idCreator = new IDCreator();
        this.sendRequestScreen = new SendRequestScreen(project);
        String clonedRepoPath = project.getBasePath() + CLONED_REPO_FOLDER;
        repoPath = new File(clonedRepoPath);
    }


   /* However the destination location is chosen, explicitly through your code or by JGit,
   the designated directory must either be empty or must not exist.
   Otherwise, an exception will be thrown.*/
    public void cloneRepo() {
        try {
            System.out.println("Repo Methodenaufruf funktioniert!");
            Git.cloneRepository()
                    .setURI(REPO_URL)
                    .setCredentialsProvider( new UsernamePasswordCredentialsProvider( REPO_LOGIN, REPO_PASSWORD ) )
                    .setDirectory(repoPath)
                    .call();
            System.out.println("Repo wurde erfolgreich geklont");
        } catch (Exception e){
            System.out.println("Repo wurde nicht erfolgreich geklont" + e.toString());
        } finally {
            //TODO: Ordner schließen/löschen
        }
    }

    //Übernommen von diesem Codebeispiel
    //https:stackoverflow.com/questions/33168230/jgit-create-new-local-branch-and-push-to-remote-branch-does-not-exist-on-remote
    public void createAndPushBranch () {
        String branchName =idCreator.createRequestID();
        System.out.println("Branchname" + branchName);
        try {
            // create branch
            Git git = Git.open(repoPath);
            CreateBranchCommand createBranchCommand = git.branchCreate();
            createBranchCommand.setName(branchName);
            createBranchCommand.call();

            //checkout branch
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(branchName);
            checkoutCommand.call();

            // push to remote
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( REPO_LOGIN, REPO_PASSWORD ));
            pushCommand.call();
            System.out.println("puschi");

        } catch (Exception e) {
            System.out.println("zweite Exception branch create" +  e.toString());
        }
    }

    private void createIssue() {
        try {
            //https://github-api.kohsuke.org/apidocs/org/kohsuke/github/GitHub.html
            GitHub github = GitHub.connectUsingPassword(REPO_LOGIN, REPO_PASSWORD);
            GHRepository repo = github.getRepository("OOP-Feedback/OOP-Feedback");
            //TODO: Namen dynamisch erstellen
            GHIssue issue = repo.createIssue("TITEL3").create();
            //TODO: Body dynamisch erstellen
            issue.setBody("TEST");
            //TODO: Labels dynamisch erstellen
            issue.addLabels("Hans", "Wurscht");
        } catch (IOException e) {
            System.out.println("excepti + " + e.toString());
        }
    }

    public void sendRequest() {
        //idCreator.createRequestID();
        //createAndPushBranch();
        createIssue();
    }


}
