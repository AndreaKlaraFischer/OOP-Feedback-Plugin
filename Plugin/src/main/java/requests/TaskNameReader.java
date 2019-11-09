package requests;

import com.intellij.openapi.project.Project;
import controller.Controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

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
    public String readNameOfTaskFromNameFile() {
        StringBuilder taskNameBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(nameFilePath))) {
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                taskNameBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(taskNameBuilder.toString());
        return taskNameBuilder.toString();
    }
}
