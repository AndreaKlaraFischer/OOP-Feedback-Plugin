package requests;

import com.intellij.openapi.project.Project;
import config.Constants;
import gui.SendRequestScreen;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;


//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private SendRequestScreen sendRequestScreen;
    private GitHubModel gitHubModel;
    private IDCreator idCreator;

   //private File repoPath;

    //Konstruktor
    public StudentRequestModel(Project project) {
        this.idCreator = new IDCreator();
        this.gitHubModel = new GitHubModel();
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
            System.out.println("Wurde erfolgreich gepusht");

        } catch (Exception e) {
            System.out.println("zweite Exception branch create" +  e.toString());
        }
    }



    public void sendRequest() {
        //idCreator.createRequestID();
        //createAndPushBranch();


        gitHubModel.createIssue();
        //requestCounter++;

    }


}