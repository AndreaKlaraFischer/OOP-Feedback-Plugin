
package controller;

import com.intellij.openapi.project.Project;
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
    public Controller(Project project) {
        studentRequestModel = new StudentRequestModel(project);
        idCreator = new IDCreator();
        gitModel = new GitModel(project);
        this.project = project;
        System.out.println("Startklasse funktioniert!");
        System.out.println(project.getBasePath());
        gitModel.cloneRepo();
        //Das steht hier nur zu Testzwecken
        idCreator.createRequestID();
    }

}