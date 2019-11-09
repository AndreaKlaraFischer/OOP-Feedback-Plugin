//TODO Bei Programmstart: PopUp mit: Willst du dich neu registrieren oder die verwendete ID benutzen?
package controller;

import actions.BalloonPopup;
import actions.MailModel;
import answers.AnnotatedCodeModel;
import answers.Answer;
import com.intellij.openapi.project.Project;
import config.Constants;
import config.SettingsService;
import gui.*;
import login.CreateXMLFile;
import login.ReadFiles;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.github.GHIssue;
import communication.GitHubModel;
import communication.GitModel;
import org.xml.sax.SAXException;
import requests.ScreenshotModel;
import login.LoginManager;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//zentrale Startklasse
public class Controller {
    private List<GHIssue> issueList;
    public Project project;
    public GitModel gitModel;
    public GitHubModel gitHubModel;
    private GitHubListener gitHubListener;
    private SettingsService settings = SettingsService.getInstance();
    public MailBoxScreen mailBoxScreen;
    public AnswerDetailScreen answerDetailScreen;
    private BalloonPopup balloonPopup;
    public SendRequestScreen sendRequestScreen;
    public SettingScreen settingScreen;
    public ScreenshotModel screenshotModel;
    public MailModel mailModel;
    public LoginManager loginManager;
    public LoginMenu loginMenu;
    public ReadFiles readFiles;
    public CreateXMLFile createXMLFile;
    public AnnotatedCodeModel annotatedCodeModel;
    public String xmlFilePath;
    public HashMap<String, String> loginData;
    //TODO: Alle unbenutzen Variablen in allen Klassen raushauen und TODOS ein bisschen abarbeiten
    public File projectPath;

    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project) throws IOException, TransformerException, SAXException, ParserConfigurationException {
        this.project = project;
        //this.xmlFilePath = project.getBasePath()  + "/.idea/personalConfig.xml";
        loginManager = new LoginManager(this); //TODO: Das noch umschreiben, immer aus dem Controller aufrufen
        loginMenu = new LoginMenu(this);
        //TODO: Wenn ich das mit dem login und so mache, dann muss readFiles vor loginManager stehen
        //Create File: file wird nur einmal angelegt.
        createXMLFile = new CreateXMLFile(this);
        //dann schauen, was so drin steht
        readFiles = new ReadFiles(this);
        //Bin ich initialisiert?
         readFiles.checkIfInitialized();

        //6.11.
        loginData = new HashMap<>();
       // createXMLFile = new CreateXMLFile(this);
        //readFiles = new ReadFiles(this);

        gitHubModel = new GitHubModel(this);
        gitModel = new GitModel(project, this);
        screenshotModel = new ScreenshotModel(this);
        mailModel = new MailModel(this);
        balloonPopup = new BalloonPopup();

        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();

        annotatedCodeModel = new AnnotatedCodeModel(this);

        try {
            //settings.setValue("Ich bin ein gespeicherter Text!");
            settings.setValue(gitHubModel.studentName);
        } catch (Exception e) {
            System.out.println("something something state" + e.toString());
        }

        System.out.println("Our state is: " + settings.getValue());
        gitModel.gitInit();
        showWelcomeMenu();

    }

    public void onNewAnswerData() {
        mailBoxScreen.refreshTable();
    }

    //TODO: Das muss noch aufgerufen werden!
    //TODO: Mit SettingScreen und ReadFiles noch kombinieren
    public String getStudentName() {
        //return settings.getName();
        return settingScreen.inputNameField.getText();
    }

    public String getStudentNameInXML() {
        return readFiles.readNameFromXML();
    }

    public String getStudentMail() {
        return settingScreen.inputMailAddressField.getText();
    }

    public String getStudentMailInXML() {
        return readFiles.readMailFromXML();
    }

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }

    public void showWelcomeMenu() {
        System.out.println("Controller showWelcomeMenu");
        //Wenn da was bei <token> steht
        if(readFiles.checkIfInitialized()) {
            System.out.println("initailisiert? Sollte true sein" + readFiles.checkIfInitialized());
            loginMenu.showLoginMenu();
        } else {
            System.out.println("initailisiert? Sollte false sein" + readFiles.checkIfInitialized());
            loginMenu.showRegistrationMenu();
        }
    }

    public void onSubmitRequestButtonPressed() {
        String requestCounter = gitModel.requestCounter();
        String requestDate = getCurrentDate();
        //TODO: Hier die Methode mit den imageStrings? Beides zusammenbauen und mit Zeilenumbrüchen trennen
        String requestMessage = getRequestMessage();
        //String requestMessage = sendRequestScreen.getInputMessage() + "\n" + "\n" + screenshotModel.getAllImagesString();
        System.out.println(requestMessage);

        //String studentName = settings.getName();
        String studentName = getStudentName();
        System.out.println(requestMessage + studentName);

        if(requestMessage.length() == 0) {
            sendRequestScreen.showEmptyMessageError();
        } else if (studentName.length() == 0) {
            sendRequestScreen.showNoNameError();
        } else {
            try {
                String title = gitHubModel.createIssueTitle(studentName, requestDate);
                String labelCategory = Constants.COMMON_LABEL_BEGIN + SendRequestScreen.problemCategory;
                String labelTask = Constants.COMMON_LABEL_BEGIN + getTaskNameFromFile();
                String labelBranchName = Constants.COMMON_LABEL_BEGIN + gitModel.createBranchName(studentName, requestCounter, requestDate);
                //String labelScreenshot = screenshotModel.getScreenshotLabel();
                //TODO: Das nicht hardcoden und Bedingung einbauen !
                String labelScreenshot = "screenshot";
                gitModel.createAndPushBranch(gitModel.createBranchName(studentName, requestCounter, requestDate));
                gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName);
                mailModel.sendMailToTutors();

            } catch (IOException | GitAPIException | URISyntaxException e) {
                //} catch (Exception e) {
                //Fehlermeldung (sinnvoll)
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
                return;
            }
            //TODO: Hier die XML File dann verändern? Issue Id kriegen - Kriege ich die an dieser Stelle schon? Wenn nein, wo rufe änder ich dann die XMl File?
            //readFiles.modifyXMLRequests();
            sendRequestScreen.showSentRequestInfo();
        }
    }

    public void onRegistrationButtonPressed() throws IOException {
        String password = loginMenu.getPasswordValidateInput().toString();
        String encryptedPassword = loginManager.encryptPassword(password);
        //String encryptedPassword = getEncryptedPassword();
        String token = getToken();
        //Hier wird das Passwort validiert
        if(loginMenu.getPasswordValidateInput().length >= 8 && loginMenu.getPasswordFirstInput().length >= 8) {
            if (Arrays.equals(loginMenu.getPasswordFirstInput(), loginMenu.getPasswordValidateInput())) {
                loginManager.createToken();
                loginManager.encryptPassword(password);
                //TODO: Die noch umschreiben, also aufdröseln in zwei Methoden
                readFiles.modifyXMLTokenAndPassword(token, encryptedPassword);
                //TODO: Das noch in HashMap speichern? Token ist die ID --> Alex fragen
                //Wichtig!
                //TODO: Das muss noch gespeichert werden --> Damit es beim Programmstart aufgerufen wird!
                loginData.put(token, encryptedPassword);
                System.out.println("HashMap: " + loginData);
                loginMenu.showValidPasswordInfo();
                loginMenu.hideRegistrationMenu();
                //sendRequestScreen.showWelcomeInfo(); //NPE
            } else {
                loginMenu.showPasswordsNotEqualError();
            }
        } else {
            loginMenu.showPasswordTooShortError();
        }
    }

    public void onLoginButtonPressed() {
        //TODO: Daten holen
        String password = Arrays.toString(loginMenu.passwordFieldLogin.getPassword());
        //String encryptedPasswordLogin = loginManager.encryptPassword(password);
        String encryptedPasswordLogin = Arrays.toString(loginMenu.getPasswordLogin());
        String encryptedPasswordXML = readFiles.readEncryptedPasswordFromXML();
        if(encryptedPasswordLogin.equals(encryptedPasswordXML)) {
            loginMenu.hideLoginMenu();
        } else {
            loginMenu.showWrongPasswordError();
        }
        //TODO Hier: Zusammenpassen von Token und Password überprüfen --> Wie?
    }

    //TODO: Was genau soll hier passieren? Die eingegeben Daten sollen in die XML File gespeichert werden
    //Ich glaube, das passt jetzt so schon.
    public void onSaveSettingsButtonPressed() throws IOException {
        String studentName = getStudentName();
        String studentMail = getStudentMail();
        readFiles.modifyXMLNameAndMail(studentName, studentMail);
    }

    public void onOpenCodeButtonPressed() {
        System.out.println("openCodeButton Methodenaufruf aus Controller");
    }

    public void sendFeedbackForFeedback() {
        //TODO: Hier muss das gemacht werden mit dem Label!
        gitHubModel.matchFeedbackAndRequest(gitHubModel.answerNumber);
        //Switch geht leider nicht mit Objekten
       if(answerDetailScreen.selectedHelpfulness == 1) {
           gitHubModel.setHelpfulFeedbackLabel();
       } else if (answerDetailScreen.selectedHelpfulness == 2) {
           gitHubModel.setNeutralLabel();
       } else if(answerDetailScreen.selectedHelpfulness == 3) {
           gitHubModel.setNotHelpfulFeedbackLabel();
       }
        answerDetailScreen.createFeedbackText();
        answerDetailScreen.showSentFeedbackBalloon();
    }

    public void sendProblemSolved() {
        gitHubModel.matchFeedbackAndRequest(gitHubModel.answerNumber);
        gitHubModel.setProblemSolvedLabel();
        answerDetailScreen.showSolvedSuccessfullySentBalloon();
    }

    public void onAnswerSelected(Answer answer) {
        mailBoxScreen.showAnswerDetailContent(answer);
    }

    public String getRequestMessage() {
        return sendRequestScreen.getInputMessage();
    }

    public String getToken() {
        return loginManager.createToken();
    }

    private String getTaskNameFromFile() {
        return readFiles.readNameOfTaskFromNameFile();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }
}
