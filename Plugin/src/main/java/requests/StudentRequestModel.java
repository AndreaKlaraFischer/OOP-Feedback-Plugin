package requests;

import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
//GitHub API


//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private static final String REPO_URL = "https://github.com/OOP-Feedback/OOP-Feedback.git";
    private static final String CLONED_REPO_FOLDER ="/ClonedRepo";
    private IDCreator idCreator;
    private File REPO_PATH = new File("https://github.com/OOP-Feedback/OOP-Feedback.git");
    private CreateBranchCommand createBranchCommand;
    private CheckoutCommand checkoutCommand;
    private File repoPath;

    //Konstruktor
    public StudentRequestModel(Project project) {
        this.idCreator = new IDCreator();
        //TODO: zu speichernden Pfad festlegen!
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
                    .setCredentialsProvider( new UsernamePasswordCredentialsProvider( "OOP-Feedback", "FredFeedback1920" ) )
                    .setDirectory(repoPath)
                    .call();
            System.out.println("Repo wurde erfolgreich geklont");
        } catch (Exception e){
            System.out.println("Repo wurde nicht erfolgreich geklont" + e.toString());
        } finally {
            //TODO: Ordner schließen
        }
    }

    //Übernommen von diesem Codebeispiel
    //https:stackoverflow.com/questions/33168230/jgit-create-new-local-branch-and-push-to-remote-branch-does-not-exist-on-remote
    public void createAndPushBranch () {
        //Branchname: ID, das funktioniert schon mal
        String branchName =idCreator.createRequestID();
        System.out.println("Branchname" + branchName);
        try {
           //Das geht nicht!
            Git git = Git.open(repoPath);
            createBranchCommand = git.branchCreate();
            createBranchCommand.setName(branchName);
            createBranchCommand.call();
            
            checkoutCommand = git.checkout();
            checkoutCommand.setName(branchName);
            checkoutCommand.call();

            // push to remote:
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider( new UsernamePasswordCredentialsProvider( "OOP-Feedback", "FredFeedback1920" ));
            pushCommand.call();
            System.out.println("puschi");

        } catch (Exception e) {
            System.out.println("zweite Exception branch create" +  e.toString());
        }



    }

    private void createIssue() {
        //TODO: Use GitHub API here for creating issues
        //Github-Java API? TODO: Anschauen und schnell entscheiden
        //Kopiert aus GitHub API:
        //POST /repos/:owner/:repo/issues

        //TODO: Create Label
        try {
            GitHub github = GitHub.connectUsingPassword("OOP-Feedback", "FredFeedback1920");
            github.getRepository("OOP-Feedback-Plugin");
            GHIssue issue;

        } catch (IOException e) {

        }
    }

    //Diese Methode soll auf dem Button aufgerufen werden
    public void sendRequest() {
        idCreator.createRequestID();
        createAndPushBranch();
        createIssue();
    }


}
