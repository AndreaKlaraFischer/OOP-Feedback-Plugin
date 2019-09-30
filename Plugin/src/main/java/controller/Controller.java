
package controller;

import com.intellij.openapi.project.Project;
import config.SettingsService;
import requests.GitHubModel;
import requests.GitModel;
import requests.IDCreator;
import requests.StudentRequestModel;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private Project project;
    private StudentRequestModel studentRequestModel;
    private IDCreator idCreator;
    private GitModel gitModel;
    private GitHubModel gitHubModel;
    private SettingsService settings = SettingsService.getInstance();

    public Controller(Project project) {
        studentRequestModel = new StudentRequestModel(project);
        idCreator = new IDCreator();
        gitModel = new GitModel(project);
        gitHubModel = new GitHubModel();


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
        //gitHubModel.getIssueList();
        //Das steht hier nur zu Testzwecken
        idCreator.createRequestID();
    }

}