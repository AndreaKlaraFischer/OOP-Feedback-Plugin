package controller;

import config.Constants;
import communication.GitHubModel;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class GitHubListener extends Thread {
    public Controller controller;
    private GitHubModel gitHubModel;

    public void run() {
        try {
            while (true) {
                //28.11. Test.
                gitHubModel.matchRequestAndAnswer(controller.getIssueList(), controller.getOwnClosedIssues());
                Thread.sleep(Constants.THREAD_MILLISECONDS);
            }
        } catch (InterruptedException | BadLocationException | IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }


    public  GitHubListener(Controller controller) {
        this.controller = controller;
        gitHubModel = controller.gitHubModel;
    }


}
