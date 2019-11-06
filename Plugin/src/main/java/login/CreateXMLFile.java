package login;

import com.intellij.openapi.project.Project;
import controller.Controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class CreateXMLFile {
    private Project project;
    public Controller controller;

    public CreateXMLFile(Controller controller) {
        //TODO: Das vielleicht aus dem Controller holen?!
        project = controller.project;
        //Das wird dann nicht hier aufgerufen, sondern im LoginManager bzw Controller
        //bzw eigentlich kann es doch stehen bleiben?
        createFile();
        this.controller = controller;
    }

    public void createFile() {
        try {
            String xmlFilePath = project.getBasePath()  + "/.idea/personalConfig.xml";
            System.out.println(xmlFilePath);

            File file = new File(xmlFilePath);
            System.out.println(file);

            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile(); //boolean
                System.out.println("Bin hierher gekommen beim File");
                FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                //Hier wird die XML Struktur erstellt
                bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + "<component>" + "\n" + "<token>" + "</token>" + "\n" + "<password>" + "</password>" + "\n" + "</component>" );
                // Close connection
                bw.close();
            }
        } catch (Exception e) {
            System.out.println("Hier kommt die NPE");
            System.out.println(e);
        }
    }
}
