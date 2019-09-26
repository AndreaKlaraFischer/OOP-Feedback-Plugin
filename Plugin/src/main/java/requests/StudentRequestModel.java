package requests;

import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;

//GitHub API


//This class handles the requests from Plugin to GitHub
public class StudentRequestModel {
    private static final String REPO_URL = "https://github.com/OOP-Feedback/OOP-Feedback";
    private static final String CLONED_REPO_FOLDER ="/ClonedRepo";
   // File repoPath = new File("https://github.com/OOP-Feedback/OOP-Feedback");
    //TODO: Richtig befüllen!

    //File clonedRepoPath = new File(;
    Project project;

    private IDCreator idCreator;

    CreateBranchCommand createBranchCommand;
    CheckoutCommand checkoutCommand;
    Git git;

    File repoPath;


    //clonedRepoPath = project.getBasePath();
    //Konstruktor
    public StudentRequestModel(Project project) {
        this.idCreator = new IDCreator();
        this.project = project;
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
                    .setDirectory(repoPath)// #1
                .call();
            System.out.println("Repo wurde erfolgreich geklont");

        } catch (Exception e){
            //?
        } finally {
            //TODO: Ordner schließen
        }
    }

    //Übernommen von diesem Codebeispiel
    //https:stackoverflow.com/questions/33168230/jgit-create-new-local-branch-and-push-to-remote-branch-does-not-exist-on-remote
    public void createAndPushBranch () {
        String branchName ="";
        try {
            Repository repo = new FileRepositoryBuilder().readEnvironment().findGitDir(repoPath).build();
            git = new Git(repo);

            createBranchCommand = git.branchCreate();
            checkoutCommand = git.checkout();
        } catch (IOException e) {

        }

        try {
            createBranchCommand.setName(branchName)
                    .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.SET_UPSTREAM)
                    .setStartPoint("origin/" + branchName)
                    .setForce(true)
                    .call();

            checkoutCommand.setName(branchName);
            checkoutCommand.call();
        } catch (Exception e) {

        }

        // push to remote:

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
