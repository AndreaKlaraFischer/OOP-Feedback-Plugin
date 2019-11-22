package communication;


import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import javax.swing.text.BadLocationException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
        clonedRepo = new File(projectPath + Constants.TUTOR_COMMENTS_FOLDER + Constants.CLONED_REPO_FOLDER);

    }

    public void gitInit() {
        try {
            //TODO: Noch wegen git.init und open schauen!
            git = Git.init().setDirectory(directory).call();
            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote", "origin", "url", Constants.REPO_URL);
            config.save();
            System.out.println("git init worked");

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }
    //TODO: Das muss mit Parametern funktionieren, da diese Methode nicht das komplette Repo klonen soll, sondern nur den jeweiligen Branch
    public void cloneBranch(String branchName)  {
        if(!clonedRepo.exists()) {
            try {
               CloneCommand cloneCommand =  Git.cloneRepository();
               cloneCommand.setURI(Constants.REPO_URL)
                       .setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD))
                        .setDirectory(clonedRepo)
                        .setBranchesToClone(Collections.singleton(branchName))
                        .setBranch(branchName)
                        .call();
            } catch (GitAPIException e) {
                System.out.println("clone Repo, hat nicht geklappt");
                e.printStackTrace();
            } finally  {
                System.out.println("clone Repo, hat geklappt!!!");
            }
        } else {
            System.out.println("clone Repo: Ordner existiert bereits");
        }
    }

    //TODO: Das gehört hier vermutlich nicht rein in s GitModel
    public void deleteClonedBranchFolder() {
        try {
            //FileUtils.cleanDirectory(clonedRepo);
            FileUtils.deleteDirectory(clonedRepo);
            System.out.println("deleteClonedRepoBranchFolder, hat geklappt.");
        } catch (Exception e) {
            System.out.println("Ordner löschen " + e.toString());
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

        //TODO: get id
    }

    private void pushToRemote() throws GitAPIException {
        PushCommand pushCommand = git.push();
        pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD)).
                //setRemote(Constants.REPO_URL). //18.11.
                setPushAll().
                setForce(true).
                call();
        System.out.println("Wurde erfolgreich gepusht");
    }

    //TODO
    public List<String> gitStatus() throws GitAPIException, BadLocationException, IOException {
        //22.11. Test, ob ich so mein remote Repo bekomme statt lokal. Das Repo wird erkannt, aber die Änderungen funktionieren dann nicht mehr...
        git = Git.open(clonedRepo);
        Repository repository = git.getRepository();
        System.out.println("repository: " + repository);

        List<String> modifiedFilesNames = new ArrayList<>();
        //Status status = git.status().call();
        Status status = new Git(repository).status().call();

        for (String changed : status.getChanged()) {
            System.out.println("Changed: " + changed);
        }

        for (String added : status.getAdded()) {
            System.out.println("Added: " + added);
            //Hier wird das dann doppelt aufgenommen, aber eigentlich werden ja keine Klassen erstellt. Da dann noch was überlegen
            //TODO Duplikate verhindern
            modifiedFilesNames.add(added);
        }

        for (String modified : status.getModified()) {
            System.out.println("Modified: " + modified);
            modifiedFilesNames.add(modified);
        }

        System.out.println("modified files: " + modifiedFilesNames);
        return modifiedFilesNames;
    }

    //https://github.com/centic9/jgit-cookbook/blob/master/src/main/java/org/dstadler/jgit/porcelain/ShowBranchDiff.java
    //public String compareBranchesForCodeChanges(String branchName) throws GitAPIException, IOException, BadLocationException {
    public List<String> compareBranchesForCodeChanges() throws GitAPIException, IOException, BadLocationException {
        List<String> diffEntries = new ArrayList<>();

        DiffCommand diffCommand = git.diff();
        repository = git.getRepository();
        //17.11.
        // wir brauchen eine Referenz auf das Repo
// *** Lokales Repo ***
// der Name des Branchs auf dem wir gerade sind - hast evtl auch schon
        String branch = repository.getBranch();
// wir brauchen den HEAD (also den aktuellsten Commit) des Branchs, das machen wir scheinbar über diese komische Konstruktion
        //ObjectId head = repository.resolve("refs/heads/"+branch+"^{tree}");
        ObjectId head = repository.resolve("HEAD^{tree}");
        //18.11. https://doc.nuxeo.com/blog/jgit-example/
        ObjectId previousHead = repository.resolve("HEAD~^{tree}");
// *** Remote-Repo ***
// fetchen, damit wir den Stand des Remote bekommen (!= pull - es werden hier keine Dateien geändert!)
        git.fetch();


        System.out.println("compareBranches, print branch: " + branch);
        Config storedConfig = repository.getConfig();
        Set<String> remotes = storedConfig.getSubsections("remote");
        System.out.println("compareBranches, print remotes: " + remotes);


// wie vorher, nur dass wir diesmal den HEAD des Remote holen
        ObjectId fetchHead = repository.resolve("FETCH_HEAD^{tree}");
        ObjectReader reader = repository.newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        //oldTreeIter.reset(reader, head);
        //18.11. NPE
        oldTreeIter.reset(reader, previousHead);
        //
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        //18.11.
        //newTreeIter.reset(reader, fetchHead);
        newTreeIter.reset(reader, head);

        diffs = diffCommand.setShowNameAndStatusOnly(false)
                //17.11. 22:14, vertauscht
                .setOldTree(oldTreeIter)
                .setNewTree(newTreeIter)
                //.setCached(false)
                .call();
        //System.out.println("diffiListi: " + diffs);
        for (DiffEntry entry : diffs) { //NPE

            //TODO: Das noch in der Schleife
            OutputStream outputStream = new ByteArrayOutputStream();
            DiffFormatter formatter = new DiffFormatter(outputStream);
            //TODO: Funktioniert das jetzt so als Membervariable?
            formatter.setRepository(repository);
            formatter.format(entry);
            //TODO: Das wird alles in den selben Stream geschrieben, wahrscheinlich auch nicht als Member Variable
            //System.out.println("outputStream: " + outputStream);
            gitDiffResult = outputStream.toString();
            diffEntries.add(gitDiffResult);

        }
        System.out.println("diffEntries: " + diffEntries);
        return diffEntries;
    }

}
