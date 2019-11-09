package login;

import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;


import java.io.*;

public class XMLFileReader {
    public Controller controller;
    private Project project;
    private Document doc;
    private String xmlFilePath;

    public XMLFileReader(Controller controller) throws IOException {
        project = controller.project;
        this.controller = controller;
        xmlFilePath = project.getBasePath() + Constants.CONFIG_FILE_PATH;
        FileInputStream fileInputStream = new FileInputStream(xmlFilePath);
        doc = Jsoup.parse(fileInputStream, null, "", Parser.xmlParser());
    }

    public void modifyXMLTokenAndPassword(String token, String password) throws IOException {
        for (Element e : doc.getElementsByTag("password")) {
            if (e.text().length() == 0) {
                e.appendText(password);
            }
        }
        for (Element e : doc.getElementsByTag("token")) {
            if (e.text().length() == 0) {
                e.appendText(token);
            }
        }

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        // Write in file, overwrites file
        bw.write(content);
        bw.close();
    }

    public boolean checkIfInitialized() {
        boolean isInitialized = false;
        for (Element e : doc.getElementsByTag("password")) {
            if (e.text().length() != 0) {
                isInitialized = true;
            }
        }
        System.out.println("initalisiert?" + isInitialized);
        return isInitialized;
    }

    public void modifyXMLNameAndMail(String name, String mail) throws IOException {
        for (Element e : doc.getElementsByTag("name")) {
            e.text(name);
        }
        for (Element e : doc.getElementsByTag("mail")) {
            e.text(mail);
        }

        String content = doc.toString();
        System.out.println("content: " + content);
        File file = new File(xmlFilePath);
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        PrintWriter pw = new PrintWriter(fw);
        System.out.println("fw: " + fw);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(content);
        bw.close();
    }

    public void modifyXMLRequests(String id) {
        //TODO: jede RequestID dort speichern!
        //TODO: Neue Tags erstellen: <request> </request>
        Element newElement = doc.createElement("request");
        newElement.appendText(id);
    }

    public String readEncryptedPasswordFromXML() {
        StringBuilder encryptedPasswordXML = new StringBuilder();
        for (Element e : doc.getElementsByTag("password")) {
            encryptedPasswordXML.append(e.text());
        }
        return encryptedPasswordXML.toString();
    }

    public String readNameFromXML() {
        String studentNameInXML = "";
        for (Element e : doc.getElementsByTag("name")) {
            studentNameInXML = e.text();
        }
        System.out.println("studentNameInXML: " + studentNameInXML);
        return studentNameInXML;
    }

    public String readMailFromXML() {
        String studentMailInXML = "";
        for (Element e : doc.getElementsByTag("mail")) {
            studentMailInXML = e.text();
        }
        return studentMailInXML;
    }

}


