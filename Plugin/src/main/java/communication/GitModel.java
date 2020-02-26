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
    private String projectPath;
    private File clonedRepo;

    public GitModel(Project project, Controller controller) {
        projectPath = project.getBasePath();
        assert projectPath != null;
        directory = new File(projectPath);
        this.controller = controller;
    }

    public void gitInit() {
        try {
            git = Git.init().setDirectory(directory).call();
            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote", "origin", "url", Constants.REPO_URL);
            config.save();

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }


        public void cloneBranch(String branchName)  {
        //For every cloned branch an own subfolder gets created
        clonedRepo = new File(controller.project.getBasePath() + Constants.TUTOR_COMMENTS_FOLDER + "/" + branchName);
        //add cloned branches to .gitignore
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
                e.printStackTrace();
            }
        } else {
            System.out.println("clone Repo: Ordner existiert bereits");
        }
    }

    public void createAndPushBranch(String branchName) throws IOException, GitAPIException {
        git = Git.open(directory);
        CreateBranchCommand createBranchCommand = git.branchCreate();
        createBranchCommand.setName(branchName);
        checkoutBranch(branchName);
        commitChanges();
        pushToRemote();
    }

    private void checkoutBranch(String branchName) {
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
        git.commit().setMessage("Anfrage erstellt und geschickt von " + controller.getStudentNameInXML()).call();
    }

    private void pushToRemote() throws GitAPIException {
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD)).
                setPushAll().
                setForce(true).
                call();
    }
}
