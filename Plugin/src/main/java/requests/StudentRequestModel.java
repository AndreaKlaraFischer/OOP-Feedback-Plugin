package requests;

import com.intellij.openapi.project.Project;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

public class StudentRequestModel {
    String repoUrl = "https://github.com/OOP-Feedback/OOP-Feedback";
    File repoPath = new File("https://github.com/OOP-Feedback/OOP-Feedback");
    File clonedRepoPath = new File("");

    CreateBranchCommand createBranchCommand;
    CheckoutCommand checkoutCommand;
    Git git;

    //clonedRepoPath = project.getBasePath();
    //Konstruktor
    public StudentRequestModel(Project project) {

    }

    //TODO: Herausfinden: Wann wird das aufgerufen?
    // --> Startklasse
    public void cloneRepository() {
        try {
            Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(clonedRepoPath)// #1
                .call();

        } catch (Exception e){

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
    }

    //Diese Methode soll auf dem Button aufgerufen werden

    public void addRequest(AddRequestArgument args) {
        createAndPushBranch();
        createIssue();
    }


}
