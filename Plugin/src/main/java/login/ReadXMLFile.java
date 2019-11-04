package login;

import com.intellij.openapi.project.Project;
import controller.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;


import java.io.*;

public class ReadXMLFile {
    public Controller controller;
    public Project project;
    private Document doc;
    //TODO: Getter Methoden!
    String token = "ddhthetzekrurzu";
    String password = "dhghdth";
    String nameFilePath;
    String xmlFilePath;

    public ReadXMLFile(Controller controller) throws IOException {
        project = controller.project;
        this.controller = controller;
        //String xmlFilePath = controller.xmlFilePath;
        //TODO: Duplicate Code
        xmlFilePath = project.getBasePath() + "/.idea/personalConfig.xml";
        nameFilePath = project.getBasePath() + "/.idea/.name";
        FileInputStream fis = new FileInputStream(xmlFilePath);

        doc = Jsoup.parse(fis, null, "", Parser.xmlParser());

        readAndModifyXML();
    }

    public void readAndModifyXML() throws IOException {
        for (Element e : doc.getElementsByTag("password")) {
            e.appendText(password);
        }

        for (Element e : doc.getElementsByTag("token")) {
            e.appendText(token);
        }

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        // Write in file, overwrites file
        bw.write(content);
        bw.close();
    }

    //TODO: Das entweder in eine eigene Klasse oder die Datei umbenennen und in ein anderes package, passt nicht zu login
    //https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
    //Hier hole ich den Namen der Studienleistung
    public String readNameFile() {
        {
            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(nameFilePath))) {
                String sCurrentLine;
                while ((sCurrentLine = br.readLine()) != null) {
                    contentBuilder.append(sCurrentLine).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(contentBuilder.toString());
            return contentBuilder.toString();
        }

        //TODO: Konstanten. Methode dann aufrufen, wenn noch nicht initialisiert
    }
}


