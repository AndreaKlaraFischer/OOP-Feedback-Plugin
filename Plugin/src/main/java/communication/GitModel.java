package communication;


import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class GitModel {
    public Controller controller;
    private Git git;
    private static File directory;
    public String projectPath;
    public String gitDiffResult;
    private List<DiffEntry> diffs;
    private Repository repository;
    private File clonedRepo;

    public GitModel(Project project, Controller controller) {
        projectPath = project.getBasePath();
        assert projectPath != null;
        directory = new File(projectPath);

        this.controller = controller;
        //19.11. Das steht hier, damit ich beim löschen auch darauf zugreifen kann? Oder in den Controller packen sonst.
        //clonedRepo = new File(projectPath + Constants.TUTOR_COMMENTS_FOLDER + Constants.CLONED_REPO_FOLDER);

    }

    //TODO: Git init nur ganz am Anfang machen, oder?
    public void gitInit() {
        try {
            //TODO: Noch wegen git.init und open schauen!
            git = Git.init().setDirectory(directory).call();
            StoredConfig config = git.getRepository().getConfig();
            //config.setInt("http", "origin","postBuffer", 1024*1024); //30.11. Will ich größer haben (*100 --> wären 100 MB)
            config.setString("remote", "origin", "url", Constants.REPO_URL);
            config.save();
            System.out.println("git init worked");

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }

    //Rufe ich das auf bei einer neuen Antwort oder beim Buttonklick?
        public void cloneBranch(String branchName)  {
        //Für jeden Branch, der geklont wird, soll ein eigener Ordner erstellt werden.
        clonedRepo = new File(controller.project.getBasePath() + Constants.TUTOR_COMMENTS_FOLDER + "/" + branchName);
        //gitIgnore wird geändert - die geclonten Repos stehen da drin
        if(!clonedRepo.exists()) {
            try {
               CloneCommand cloneCommand =  Git.cloneRepository();
               cloneCommand.setURI(Constants.REPO_URL)
                       .setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD))
                        .setDirectory(clonedRepo)
                        .setBranchesToClone(Collections.singleton(branchName))
                        .setBranch(branchName)
                        .call();
               controller.modifiedFilesReader.saveFilesInHashMaps(branchName);

            } catch (GitAPIException | IOException e) {
                System.out.println("clone Repo, hat nicht geklappt");
                e.printStackTrace();
            } finally  {
                System.out.println("clone Repo, hat geklappt!!!");
            }
        } else {
            System.out.println("clone Repo: Ordner existiert bereits");
        }
    }

    public void createAndPushBranch(String branchName) throws IOException, GitAPIException, URISyntaxException {
        git = Git.open(directory);
        System.out.println("createAndPushBranch ");

        CreateBranchCommand createBranchCommand = git.branchCreate();
        createBranchCommand.setName(branchName);
        //NPE:
        //createBranchCommand.setNewName( branchName).call();
        //createBranchCommand.call();

        checkoutBranch(branchName);
        commitChanges();
        pushToRemote();
    }

    private void checkoutBranch(String branchName) {
        System.out.println("checkoutBranch: " + branchName);
        CheckoutCommand checkoutCommand = git.checkout();
        checkoutCommand.setCreateBranch(true);
        checkoutCommand.setName(branchName);
        try {
            checkoutCommand.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void commitChanges() throws GitAPIException {
        AddCommand addCommand = git.add();
        addCommand.addFilepattern(".");
        addCommand.call();
        //TODO: Commitmessage vielleicht noch ein bisschen dynamisch / individuell gestalten
        git.commit().setMessage("Anfrage erstellt und geschickt von " + controller.getStudentNameInXML()).call();
    }

    private void pushToRemote() throws GitAPIException {
        PushCommand pushCommand = git.push();
        System.out.println("LoginDaten: " + Constants.REPO_LOGIN + Constants.REPO_PASSWORD);
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD)).
                //setRemote(Constants.REPO_URL). //18.11.
                setPushAll().
                setForce(true).
                call();

        System.out.println("Wurde erfolgreich gepusht");
    }
}
