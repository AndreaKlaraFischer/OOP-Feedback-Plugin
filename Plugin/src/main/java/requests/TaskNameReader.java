package requests;

import com.intellij.openapi.project.Project;
import controller.Controller;

import java.io.*;
import java.util.Scanner;

public class TaskNameReader {
    public Controller controller;
    private Project project;
    private String nameFilePath;

    public TaskNameReader(Controller controller) {
        project = controller.project;
        nameFilePath = project.getBasePath() + "/.idea/.name";
    }

    //https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
    //Hier hole ich den Namen der Studienleistung
    public String readNameOfTaskFromNameFile() throws FileNotFoundException {
        Scanner taskNameScanner = new Scanner(new File(nameFilePath)).useDelimiter("\n");
        String taskName;
        taskName = taskNameScanner.nextLine().trim();
        taskNameScanner.close();
        return taskName;
    }
}
