package communication;


import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GitModel {
    //TODO: Den Branchnamen in eine eigene Klasse auslagern, das sind drei Methoden, die hier eigentlich nichts mit Git zu tun haben
    private String branchName;
    public Controller controller;
    private Git git;
    public String clonedRepoPath;
    private static File directory;
    private int counter;
    public String projectPath;

    public GitModel(Project project, Controller controller ) {
        projectPath = project.getBasePath();
        directory = new File(projectPath);

        counter = 0;
        controller.XMLFileReader.modifyCounter(counter);
        controller.XMLFileReader.readCounterValueFromXML();
        counter = controller.XMLFileReader.readCounterValueFromXML();

        this.controller = controller;
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

        checkoutBranch();
        commitChanges();
        pushToRemote();
    }

    private void checkoutBranch() {
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

    //10.11. Das wird jetzt ein riesen TODO
    //Branches vergleichen, schauen ob Änderungen da sind, wenn Änderungen da sind - Button zeigen und enablen

    //TODO: Das wird wahrscheinlich an der falschen Stelle angerufen! Es muss zu Programmstart schon false gesetzt werden.
    public boolean checkForCodeChanges() {
        return false;
    }

    //TODO: Gehört das hier her?
    public String createBranchName(String studentName, String requestCounter, String requestDate) {
        studentName = removeWhitespacesFromStudentName(studentName);
        requestDate  = formatDateForBranchName(requestDate);
        branchName = "Branch-" +
                studentName +
                Constants.HYPHEN +
                requestCounter +
                Constants.HYPHEN +
                requestDate;
        return branchName;
    }

    //TODO: Gehört das hier her?
    private String getBranchName() {
        return branchName;
    }

    public String createImageUrl() {
        return "OOP-Feedback-Test/OOP-Feedback/blob/" + getBranchName() + "/screenshots/";
    }

    //TODO: Gehört das hier her?
    private String removeWhitespacesFromStudentName(String studentName) {
        for(int i = 0; i < studentName.length(); i++ ){
            studentName = studentName.replaceAll("\\s+","");
        }
        return studentName;
    }

    //TODO: Gehört das hier her?
    //Hier werden alle Satzzeichen entfernt und nur die ersten vier Ziffern rausgeholt und dann noch die letzten 4.
    private String formatDateForBranchName(String requestDate) {
        for (int i = 0; i < requestDate.length(); i++) {
            requestDate = requestDate.replaceAll("\\p{Punct}", "");
        }
        String date = requestDate.substring(0,4);
        String day = requestDate.substring(requestDate.length() - 4);
        requestDate = date + Constants.HYPHEN + day;
        return requestDate;
    }

    //TODO: Gehört das hier her?
    public String incrementRequestCounter() {
        counter ++;
        controller.XMLFileReader.modifyCounter(counter);
        return String.valueOf(counter);
    }

}
