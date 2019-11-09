package login;

import com.intellij.openapi.project.Project;
import config.Constants;
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
    private File file;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;

    public ReadFiles(Controller controller) throws IOException {
        project = controller.project;
        this.controller = controller;
        //String xmlFilePath = controller.xmlFilePath;
        //TODO: Duplicate Code?
        xmlFilePath = project.getBasePath() + Constants.CONFIG_FILE_PATH;
        nameFilePath = project.getBasePath() + "/.idea/.name";
        FileInputStream fileInputStream = new FileInputStream(xmlFilePath);
        /*file = new File(xmlFilePath);
        fileWriter = new FileWriter(file.getAbsoluteFile());*/
       // bufferedWriter = new BufferedWriter(fileWriter);
        doc = Jsoup.parse(fileInputStream, null, "", Parser.xmlParser());
    }

    //Bekommt token und password übergeben aus dem controller, 9.11. - Das funktioniert. Beim Start wird das da reingeschrieben.

    public void modifyXMLTokenAndPassword(String token, String password) throws IOException {
        for (Element e : doc.getElementsByTag("password")) {
            if(e.text().length() == 0) {
                e.appendText(password);
            }
        }
        for (Element e : doc.getElementsByTag("token")) {
            if(e.text().length() == 0) {
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

        /*String content = doc.toString();
        // Write in file, overwrites file
        fileWriter = new FileWriter(file.getAbsoluteFile());
        bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(content);
        bufferedWriter.close();*/
    }

    public boolean checkIfInitialized() {
        boolean isInitialized = false;
        for(Element e : doc.getElementsByTag("password")) {
            if(e.text().length() != 0) {
                isInitialized = true;
            }
        }
        System.out.println("initalisiert?" + isInitialized);
        return isInitialized;
    }

    //TODO: Das geht leider nicht, aber sollte in zwei aufgedröselten Methoden stehen, damit ich die richtigen Parameter übergeben kann
    //09.10. TODO: Das muss heute funktionieren!
    public void modifyXMLNameAndMail(String name, String mail) throws IOException {
        for (Element e : doc.getElementsByTag("name")) {
            //Geht das so? Es muss erst rausgelöscht werden und dann neu geschrieben --> Das geht nicht so.
            e.text(name);
            /*String newName = e.text().replace(e.text(), name);
                e.appendText(newName);*/
        }
        //Es wird reingeschrieben, aber nicht angezeigt in der XML. Auch hier muss überschrieben werden
        for (Element e : doc.getElementsByTag("mail")) {
            e.text(mail);
                //e.appendText(mail);
        }

        String content = doc.toString();
        System.out.println("content: " + content);
        File file = new File(xmlFilePath);
        FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        PrintWriter pw = new PrintWriter(fw);
        System.out.println("fw: " + fw);
        BufferedWriter bw = new BufferedWriter(fw);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //bufferedWriter.write(content);
        //bufferedWriter.close();
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
        String encryptedPasswordXML = "";
        for (Element e : doc.getElementsByTag("password")) {
            encryptedPasswordXML += e.text();
        }
        System.out.println("Aus der readFiles Methode beim Login: " + encryptedPasswordXML);
        return encryptedPasswordXML;
    }

   //TODO: Namen holen, getter Methode
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
        for(Element e : doc.getElementsByTag("mail")) {
            studentMailInXML = e.text();
        }
        return studentMailInXML;
    }

    //TODO: Das entweder in eine eigene Klasse oder die Datei umbenennen und in ein anderes package, passt nicht zu login
    //TODO: Das kann auch einfacher gestaltet werden bestimmt ohne so viel geklauten Code
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


