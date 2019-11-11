package login;

import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class XMLFileCreator {
    private Project project;
    public Controller controller;
    private BufferedWriter bufferedWriter;

    public XMLFileCreator(Controller controller) {
        project = controller.project;
        createFile();
        this.controller = controller;
    }

    public void createFile() {
        try {
            String xmlFilePath = project.getBasePath() + Constants.CONFIG_FILE_PATH;
            System.out.println(xmlFilePath);

            File file = new File(xmlFilePath);
            System.out.println(file);

            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile(); //boolean
                FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
                bufferedWriter = new BufferedWriter(fileWriter);
                writeXMLStructure();
                bufferedWriter.close();
            }
        } catch (Exception e) {
            System.out.println("Hier kommt die NPE");
            System.out.println(e);
        }
    }

    //"\\s" = space
    private void writeXMLStructure() throws IOException {
       bufferedWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" +
               "<configuration>" + "\n" +
                "\t" + "<component name='config'>" + "\n" +
                "\t" + "\t" + "<name>" + "</name>" + "\n" +
                "\t" + "\t" + "<mail>" + "</mail>" + "\n" +
                "\t" + "\t" + "<token>" + "</token>" + "\n" +
                "\t" + "\t" + "<password>" + "</password>" + "\n" +
                "\t" + "</component>" + "\n" +
                "\t" + "<requests>" + "\n" +
                "\t" + "\t" + "<counter>" + "</counter>" +
                "\t" + "</requests>" + "\n" +
               "</configuration>");
    }
}
