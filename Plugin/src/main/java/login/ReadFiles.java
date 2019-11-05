package login;

import com.intellij.openapi.project.Project;
import controller.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;


import java.io.*;

public class ReadFiles {
    public Controller controller;
    private Project project;
    private Document doc;
    private String nameFilePath;
    private String xmlFilePath;

    public ReadFiles(Controller controller) throws IOException {
        project = controller.project;
        this.controller = controller;
        //String xmlFilePath = controller.xmlFilePath;
        //TODO: Duplicate Code
        xmlFilePath = project.getBasePath() + "/.idea/personalConfig.xml";
        nameFilePath = project.getBasePath() + "/.idea/.name";
        FileInputStream fis = new FileInputStream(xmlFilePath);

        doc = Jsoup.parse(fis, null, "", Parser.xmlParser());

    }

    //Bekommt token und password übergeben aus dem controller
    public void readAndModifyXML(String token, String password) throws IOException {
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

    //Das wird beim Login aufgerufen dann! Würde ich doch wieder als String machen und dann ne MEthode schreiben, die das überprüft.
    //TODO: Da funktioniert die Logik nochnicht ganz
    public boolean readEncryptedPasswordFromXML() {
        boolean isCorrectPassword = false;
        for (Element e : doc.getElementsByTag("password")) {
            String encryptedPassword = ""; //Das soll übergeben werden
            if (e.text().equals(encryptedPassword)) {
                isCorrectPassword = true;
            }
        }
        return isCorrectPassword;
    }

    //TODO: Das entweder in eine eigene Klasse oder die Datei umbenennen und in ein anderes package, passt nicht zu login
    //https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
    //Hier hole ich den Namen der Studienleistung
    public String readNameFile() {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new java.io.FileReader(nameFilePath))) {
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
}


