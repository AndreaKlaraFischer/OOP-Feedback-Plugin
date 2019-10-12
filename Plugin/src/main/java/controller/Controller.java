//TODO Bei Programmstart: PopUp mit: Willst du dich neu registrieren oder die verwendete ID benutzen?
package controller;

import com.intellij.openapi.project.Project;
import config.SettingsService;
import gui.MailBoxScreen;
import org.kohsuke.github.GHIssue;
import requests.GitHubModel;
import requests.GitModel;
import requests.IDCreator;
import requests.StudentRequestModel;

import java.util.List;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private List<GHIssue> issueList;
    private Project project;
    private StudentRequestModel studentRequestModel;
    private IDCreator idCreator;
    private GitModel gitModel;
    public GitHubModel gitHubModel;
    private GitHubListener gitHubListener;
    private SettingsService settings = SettingsService.getInstance();
    public MailBoxScreen mailBoxScreen;

    public Controller(Project project) {
        studentRequestModel = new StudentRequestModel(project, this);
        idCreator = new IDCreator();
        gitModel = new GitModel(project);
        gitHubModel = new GitHubModel(this);
        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();

        try {
            //settings.setValue("Ich bin ein gespeicherter Text!");
            settings.setValue(gitHubModel.studentName);
        } catch (Exception e) {
            System.out.println("something something state" + e.toString());
        }


        System.out.println("Our state is: " + settings.getValue());

        this.project = project;
        System.out.println("Startklasse funktioniert!");

        try {
            gitModel.cloneRepo();
        } catch (Exception e) {
            System.out.println("failed cloning the repository");
        }

        //Das steht hier nur zu Testzwecken
        idCreator.createRequestID();
    }

    public void onNewAnswerData() {
        mailBoxScreen.refreshTable();
        System.out.println("onNewAnswerData");
    }

}