package controller;

import com.intellij.openapi.project.Project;
import requests.StudentRequestModel;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private final Project project;
    private StudentRequestModel studentRequestModel;
    public Controller(Project project) {
        studentRequestModel = new StudentRequestModel(project);
        this.project = project;
        System.out.println("Startklasse funktioniert!");
        System.out.println(project.getBasePath());
        studentRequestModel.cloneRepo();
    }

}
