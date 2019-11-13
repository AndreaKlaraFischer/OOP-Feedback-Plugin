package communication;


import android.os.SystemPropertiesProto;
import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.errors.NoRemoteRepositoryException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.kohsuke.github.GHRepository;

import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class GitModel {
    public Controller controller;
    private Git git;
    public String clonedRepoPath;
    private static File directory;
    public String projectPath;
    public List<String> modifiedFiles;
    public List<String> diffEntries;

    public GitModel(Project project, Controller controller ) {
        projectPath = project.getBasePath();
        assert projectPath != null;
        directory = new File(projectPath);

        this.controller = controller;
        modifiedFiles = new ArrayList<>();
        diffEntries = new ArrayList<>();
    }

    public void gitInit() {
       try {
           //TODO: Noch wegen git.init und open schauen!
            git = Git.init().setDirectory(directory).call();

        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    public void createAndPushBranch (String branchName) throws IOException, GitAPIException, URISyntaxException {
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
        CheckoutCommand checkoutCommand = git.checkout();
        checkoutCommand.setCreateBranch(true);
        checkoutCommand.setName(branchName);
        try {
            checkoutCommand.call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }


    private void commitChanges() throws GitAPIException {
        AddCommand addCommand = git.add();
        addCommand.addFilepattern(".");
        addCommand.call();
        //TODO: Commitmessage vielleicht noch ein bisschen dynamisch / individuell gestalten
        git.commit().setMessage("Anfrage erstellt und geschickt von " + controller.getStudentNameInXML()).call();
    }

    private void pushToRemote() throws GitAPIException {
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD)).
                setRemote(Constants.REPO_URL).
                setPushAll().
                setForce(true).
                call();
        System.out.println("Wurde erfolgreich gepusht");
    }

    public void gitStatus() throws GitAPIException, BadLocationException {
        Status status = git.status().call();

        for(String changed:status.getChanged()) {
            System.out.println("Changed: " + changed);
        }

        for(String added:status.getAdded()) {
            System.out.println("Added: " + added);
        }

        for(String modified : status.getModified()) {
            System.out.println("Modified: " + modified);
            modifiedFiles.add(modified);
            controller.annotatedCodeModel.createWindow();
            System.out.println(modifiedFiles);
        }

        //TODO: Abfragen, ob die Liste workspace.xml enthält --> Wenn ja, dann raushauen.

    }

    //https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowBranchDiff.java
    public void compareBranchesForCodeChanges(String branchName) throws GitAPIException, IOException {
        FetchCommand fetchCommand = git.fetch();
        DiffCommand diffCommand = git.diff();
        Repository repository = git.getRepository();
        //repository.resolve("refs/heads/" + repository.getBranch() + "^{tree}");

        try {
            gitStatus();
        } catch (Exception e) {
            System.out.println("GITAP status: " + e.getMessage());
        }
        try {
            FetchResult result = fetchCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD))
                    .setRemote(Constants.REPO_URL)
                    //TODO: Da stimmt noch was nicht, es werden die Änderungen aus dem lokalen Projekt getrackt
                    .setRefSpecs(new RefSpec("refs/heads/" + repository.getBranch() + ":refs/remotes/origin" + repository.getBranch()))
                    .call();
            System.out.println(result);
            System.out.println("fetchi result: " + result.toString());
        } catch (Exception e) {
            System.out.println("GITAP fetch: " + e.getMessage());
        }
        try {
            //TODO: Das hier in ne Liste schreiben oder so?
            List<DiffEntry> diffs = diffCommand.setOutputStream(System.out).call();
            System.out.println("diffiListi: " + diffs);

            //TODO: Muss noch funktionieren
            for(DiffEntry entry : diffs) {
                System.out.println("entrytostring: " + entry.toString());
                diffEntries.add(entry.toString());
                System.out.println(diffEntries);
            }
        } catch (Exception b) {
            System.out.println("gitApiException diff: " + b.getMessage());
        }

      //git.diff().setOutputStream(System.out).call();

    /* diffCommand.setOldTree(oldTreeIterator)
              .setNewTree(newTreeIterator)
              .call();
     for(DiffEntry diffEntry : listDiffs) {
         DiffFormatter formatter = new DiffFormatter(System.out);
         formatter.setRepository();
         formatter.format(diffEntry);
     }*/
    }

    private void listDiffs(Repository repository, Git git, String oldCommit, String newCommit) {

    }

    //10.11. Das wird jetzt ein riesen TODO
    //Branches vergleichen, schauen ob Änderungen da sind, wenn Änderungen da sind - Button zeigen und enablen
    //Alle geänderten Files in einer Liste speichern
    //Alle geänderten Zeilen holen
    //File als String, geänderte Zeilen hinterlegen

    //TODO: Das wird wahrscheinlich an der falschen Stelle angerufen! Es muss zu Programmstart schon false gesetzt werden.
    public boolean hasCodeChanged() {
        return false;
    }

    public String createImageUrl() {
        return "OOP-Feedback-Test/OOP-Feedback/blob/" + controller.getBranchName() + "/screenshots/";
    }



}
