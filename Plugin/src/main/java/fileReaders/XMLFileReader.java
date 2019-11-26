package fileReaders;

import android.util.BinaryLogEntry;
import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
import gherkin.lexer.El;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;


import java.io.*;
import java.util.ArrayList;

public class XMLFileReader {
    public Controller controller;
    private Document doc;
    private String xmlFilePath;
    private String docString;

    public XMLFileReader(Controller controller) throws IOException {
        Project project = controller.project;
        this.controller = controller;
        xmlFilePath = project.getBasePath() + Constants.CONFIG_FILE_PATH;
        FileInputStream fileInputStream = new FileInputStream(xmlFilePath);
        doc = Jsoup.parse(fileInputStream, null, "", Parser.xmlParser());
        docString = doc.toString();
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


    //TODO: Warum ist das so einfach? Geht das bei den anderen Sachen auch so leicht?
    public void modifyCounter(int counter) {
        for (Element e : doc.getElementsByTag("counter")) {
            e.text(String.valueOf(counter));
        }
    }

    public void modifyOpenRequestsCounter(int counter) throws IOException {
        for (Element e : doc.getElementsByTag("openRequestsNumber")) {
            e.text(String.valueOf(counter));
        }

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        // Write in file, overwrites file
        bw.write(content);
        bw.close();
    }

    public void modifyXMLNameAndMail(String name, String mail) throws IOException {
        for (Element e : doc.getElementsByTag("name")) {
            e.text(name);
        }
        for (Element e : doc.getElementsByTag("mail")) {
            e.text(mail);
        }

        String content = doc.toString(); //Das hole ich mir, damit ich den TexteditorTab testen kann
        System.out.println("content: " + content);
        File file = new File(xmlFilePath);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        System.out.println("fw: " + fileWriter);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(content);
        bw.close();
    }

    public void modifyXMLRequests(long id) throws IOException {
        for (Element e : doc.getElementsByTag("requests")) {
            Element newElement = e.appendElement("request");
            newElement.text(String.valueOf(id));
            System.out.println(newElement);
        }
        System.out.println(doc);

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        // Write in file, overwrites file
        bw.write(content);
        bw.close();
    }

    public void modifyAnswerList(int id) throws IOException {
        System.out.println("modifyAnswerList, wurde aufgerufen");
        for (Element e : doc.getElementsByTag("answers")) {
            Element newElement = e.appendElement("answer");
            newElement.text(String.valueOf(id));
        }
        System.out.println("Nach modify: " + doc);

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        // Write in file, overwrites file
        bw.write(content);
        bw.close();
    }

    //TODO: Das noch weniger duplicate Code machen?
    public String readEncryptedPasswordFromXML() {
        String encryptedPasswordXML = "";
        for (Element e : doc.getElementsByTag("password")) {
            encryptedPasswordXML = e.text();
        }
        return encryptedPasswordXML;
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

    public int readCounterValueFromXML() {
        String counterValue = "";
        for (Element e : doc.getElementsByTag("counter")) {
            counterValue = e.text();
        }
        return Integer.parseInt(counterValue);
    }

    //23.11. Versuch, dort die number reinzuspeichern statt die id.
    public ArrayList<Integer> readRequestIdsFromXML() {
        ArrayList<Integer> requestIdsFromXYMl = new ArrayList<>();
        for (Element e : doc.getElementsByTag("request")) {
            requestIdsFromXYMl.add(Integer.valueOf(e.text()));
        }
        return requestIdsFromXYMl;
    }

    public ArrayList<Integer> readAnswerIdsFromXML() {
        ArrayList<Integer> answerIdsFromXML = new ArrayList<>();
        for (Element e : doc.getElementsByTag("answer")) {
            answerIdsFromXML.add(Integer.valueOf(e.text()));
        }
        return answerIdsFromXML;
    }

    //22.11. Test. Ob ich so die Anfragen zählen kann
    public int readOpenRequestsValueFromXML() {
        String openRequestsValue = "";
        for (Element e : doc.getElementsByTag("openRequestsNumber")) {
            openRequestsValue = e.text();
        }
        return Integer.parseInt(openRequestsValue);
    }
}
