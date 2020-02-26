package fileHandler;

import com.intellij.openapi.project.Project;
import config.Constants;
import controller.Controller;
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

    //https://jsoup.org/
    public XMLFileReader(Controller controller) throws IOException {
        Project project = controller.project;
        this.controller = controller;
        xmlFilePath = project.getBasePath() + Constants.CONFIG_FILE_PATH;
        FileInputStream fileInputStream = new FileInputStream(xmlFilePath);
        doc = Jsoup.parse(fileInputStream, null, "", Parser.xmlParser());
        docString = doc.toString();
    }

    public void modifyPassword(String password) throws IOException {
        for (Element e : doc.getElementsByTag("password")) {
            if (e.text().length() == 0) {
                e.appendText(password);
            }
        }

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(content);
        bw.close();
    }

    //If there is a password saved in the configuration file, the user is already initialized
    public boolean checkIfInitialized() {
        boolean isInitialized = false;
        for (Element e : doc.getElementsByTag("password")) {
            if (e.text().length() != 0) {
                isInitialized = true;
            }
        }
        return isInitialized;
    }

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

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
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
        //System.out.println("content: " + content);
        File file = new File(xmlFilePath);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(content);
        bw.close();
    }

    public void modifyXMLRequests(long id) throws IOException {
        for (Element e : doc.getElementsByTag("requests")) {
            Element newElement = e.appendElement("request");
            newElement.text(String.valueOf(id));
        }

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        // Write in file, overwrites file
        bw.write(content);
        bw.close();
    }

    public void modifyAnswerList(int id) throws IOException {
        for (Element e : doc.getElementsByTag("answers")) {
            Element newElement = e.appendElement("answer");
            newElement.text(String.valueOf(id));
        }
        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(content);
        bw.close();
    }

    public void modifyDate(String currentDate) throws IOException {
        //saves one day (substring instead of"SimpleDateFormat")
        String date = currentDate.substring(0,10);
        for (Element e : doc.getElementsByTag("date")) {
            e.text(date);
        }

        File file = new File(xmlFilePath);
        String content = doc.toString();

        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fileWriter);
        bw.write(content);
        bw.close();
    }

    public void modifyUserID(String userID) {
        for(Element e : doc.getElementsByTag("userID")) {
            e.text(userID);
        }
    }

    public String readUserIDFromXML() {
        String userID = "";
        for(Element e : doc.getElementsByTag("userID")) {
            userID = e.text();
        }
        return userID;
    }

    public String readDateFromXML() {
        String date = "";
        for (Element e : doc.getElementsByTag("date")) {
            date = e.text();
        }
        return date;
    }

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
        ArrayList<Integer> requestIdsFromXMl = new ArrayList<>();
        for (Element e : doc.getElementsByTag("request")) {
            requestIdsFromXMl.add(Integer.valueOf(e.text()));
        }
        return requestIdsFromXMl;
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

    public String readLinkFromXML() {
        String link = "";
        for(Element e : doc.getElementsByTag("link")) {
            link = e.text();
        }
        return link;
    }
}
