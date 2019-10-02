
package controller;

import com.intellij.openapi.project.Project;
import config.SettingsService;
import org.kohsuke.github.GHIssue;
import requests.*;

import java.util.List;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private List<GHIssue> issueList;
    private Project project;
    private StudentRequestModel studentRequestModel;
    private IDCreator idCreator;
    private GitModel gitModel;
    private GitHubModel gitHubModel;
    private GitHubListener gitHubListener;
    private SettingsService settings = SettingsService.getInstance();

    public Controller(Project project) {
        studentRequestModel = new StudentRequestModel(project);
        idCreator = new IDCreator();
        gitModel = new GitModel(project);
        gitHubModel = new GitHubModel();
        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener();


        /*try {
            settings.setValue("Ich bin ein gespeicherter Text!");
        }
        catch (Exception e)
        {
            System.out.println("something something state" + e.toString());
        }*/


        System.out.println("Our state is: " + settings.getValue());

        this.project = project;
        System.out.println("Startklasse funktioniert!");
        gitModel.cloneRepo();
        //TODO: Alle 5 Minuten oder so soll geschaut werden, ob sich der State von einem Issue aus der Liste von OPEN zu CLOSED geändert hat

        //gitHubModel.getIssueList();
        //Das steht hier nur zu Testzwecken
        idCreator.createRequestID();
    }

}