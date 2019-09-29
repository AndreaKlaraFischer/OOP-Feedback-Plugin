package requests;

import com.intellij.openapi.project.Project;
import config.Constants;
import gui.SendRequestScreen;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import java.io.IOException;


//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private SendRequestScreen sendRequestScreen;
    private IDCreator idCreator;
   //private File repoPath;

    //Konstruktor
    public StudentRequestModel(Project project) {
        this.idCreator = new IDCreator();
        //this.sendRequestScreen = new SendRequestScreen(project);
        /*String clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
        repoPath = new File(clonedRepoPath);*/
    }


    //Übernommen von diesem Codebeispiel
    //https:stackoverflow.com/questions/33168230/jgit-create-new-local-branch-and-push-to-remote-branch-does-not-exist-on-remote
    public void createAndPushBranch () {
        String branchName =idCreator.createRequestID();
        System.out.println("Branchname" + branchName);
        try {
            // create branch
            Git git = Git.open(GitModel.repoPath);
            CreateBranchCommand createBranchCommand = git.branchCreate();
            createBranchCommand.setName(branchName);
            createBranchCommand.call();

            //checkout branch
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(branchName);
            checkoutCommand.call();

            // push to remote
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( Constants.REPO_LOGIN, Constants.REPO_PASSWORD ));
            pushCommand.call();
            System.out.println("puschi");

        } catch (Exception e) {
            System.out.println("zweite Exception branch create" +  e.toString());
        }
    }

    private void createIssue() {
        try {
            //https://github-api.kohsuke.org/apidocs/org/kohsuke/github/GitHub.html
            GitHub github = GitHub.connectUsingPassword(Constants.REPO_LOGIN, Constants.REPO_PASSWORD);
            GHRepository repo = github.getRepository(Constants.REPO_NAME);
            //TODO: Namen dynamisch erstellen
            //TODO: Überlegen: Wie setzt sich der Titel zusammen? Soll man noch ein Betrefffeld einbauen oder werden die durchnummeriert? Zum Beispiel mit einem Counter?
            GHIssue issue = repo.createIssue("TITEL3").create();
            String issueBody = SendRequestScreen.inputMessage;
            issue.setBody(issueBody);
            String issueLabel = SendRequestScreen.problemCategory;
            issue.addLabels(issueLabel);
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