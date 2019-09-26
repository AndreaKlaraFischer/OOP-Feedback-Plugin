package controller;

import com.intellij.openapi.project.Project;
import requests.StudentRequestModel;
import requests.IDCreator;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private Project project;
    private StudentRequestModel studentRequestModel;
    private IDCreator idCreator;
    public Controller(Project project) {
        studentRequestModel = new StudentRequestModel(project);
        idCreator = new IDCreator();
        this.project = project;
        System.out.println("Startklasse funktioniert!");
        System.out.println(project.getBasePath());
        studentRequestModel.cloneRepo();
        idCreator.createRequestID();
    }

}
