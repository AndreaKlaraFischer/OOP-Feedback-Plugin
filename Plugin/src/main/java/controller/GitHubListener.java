package controller;

import config.Constants;
import communication.GitHubModel;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.text.BadLocationException;
import java.io.IOException;

public class GitHubListener extends Thread {
    public Controller controller;
    public GitHubModel gitHubModel;

    public void run() {
        try {
            while (true) {
                gitHubModel.getClosedIssueList();
                gitHubModel.matchRequestAndAnswer();
                Thread.sleep(Constants.THREAD_MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }


    public  GitHubListener(Controller controller) {
        this.controller = controller;
        gitHubModel = controller.gitHubModel;
    }


}
