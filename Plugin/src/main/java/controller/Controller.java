package controller;

import de.ur.mi.pluginhelper.logger.Log;
import de.ur.mi.pluginhelper.logger.LogDataType;
import de.ur.mi.pluginhelper.logger.LogManager;
import communication.MailModel;
import de.ur.mi.pluginhelper.logger.SyncProgressListener;
import fileReaders.ModifiedFilesReader;
import gui.AnnotatedCodeWindow;
import answers.Answer;
import answers.AnswerList;
import com.intellij.openapi.project.Project;
import config.Constants;
import gui.*;
import login.XMLFileCreator;
import fileReaders.XMLFileReader;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.github.GHIssue;
import communication.GitHubModel;
import communication.GitModel;
import org.xml.sax.SAXException;
import requests.BranchNameCreator;
import requests.NameGenerator;
import requests.OpenRequestsModel;
import requests.ScreenshotModel;
import login.LoginManager;
import fileReaders.TaskNameReader;

import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//zentrale Startklasse
public class Controller {
    private List<GHIssue> issueList;
    public Project project;
    public GitModel gitModel;
    public GitHubModel gitHubModel;
    private GitHubListener gitHubListener;
    public MailBoxScreen mailBoxScreen;
    public AnswerDetailScreen answerDetailScreen;
    public SendRequestScreen sendRequestScreen;
    public SettingScreen settingScreen;
    public ScreenshotModel screenshotModel;
    public MailModel mailModel;
    public LoginManager loginManager;
    public LoginMenu loginMenu;
    //24.11.
    public LoginMenu1 loginMenu1;
    public RegistrationMenu registrationMenu;
    //
    public XMLFileReader XMLFileReader;
    public TaskNameReader taskNameReader;
    public XMLFileCreator XMLFileCreator;
    public AnnotatedCodeWindow annotatedCodeWindow;
    public HashMap<String, String> loginData;
    public AnswerList answerList;
    //20.11.
    public ModifiedFilesReader modifiedFilesReader;
    public OpenRequestsModel openRequestsModel;

    //19.11.
    //public ArrayList<String> openRequests;
    //public int openRequestCounter;

    public BranchNameCreator branchNameCreator;
    public NameGenerator nameGenerator;
    //10.11. Test
    public boolean hasChanges;
    //
    public de.ur.mi.pluginhelper.User.User user;
    public Log log;

    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project) throws IOException, TransformerException, SAXException, ParserConfigurationException, BadLocationException {
        this.project = project;
        //this.xmlFilePath = project.getBasePath()  + "/.idea/personalConfig.xml";
        loginManager = new LoginManager(this);
        loginMenu = new LoginMenu(this);
        //loginMenu1 = new LoginMenu1(this);
        //registrationMenu = new RegistrationMenu(this);
        //
        //Create File: file wird nur einmal angelegt.
        XMLFileCreator = new XMLFileCreator(this);
        XMLFileReader = new XMLFileReader(this);
        //Bin ich initialisiert?
        XMLFileReader.checkIfInitialized();
        //22.11. Test ob richtige Stelle
        openRequestsModel= new OpenRequestsModel(this);
        //
        //9.11. Steht das an der richtigen Stelle?
        taskNameReader = new TaskNameReader(this);
        //6.11.
        loginData = new HashMap<>();
        // createXMLFile = new CreateXMLFile(this);
        //readFiles = new ReadFiles(this);
        //21.11.
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        Log log = LogManager.openLog(user.getID(), "MA-Fischer");
        log.log(user.getSessionID(), LogDataType.USER, "log", "Plugin gestartet");
        String serverUrl = Constants.SEAFILE_SERVER_URL;
        // Log synchronisieren
        LogManager.syncLog(log, user, serverUrl, new SyncProgressListener() {
            @Override
            public void onFinished() {
                System.out.println("Upload Finished");
            }

            @Override
            public void onFailed() {
                System.out.println("Upload Failed");
            }
        });
        //
        modifiedFilesReader = new ModifiedFilesReader(this);
        //15.11.
        answerList = new AnswerList();
        //hasChanges = false;
        //19.11.
        //openRequests = new ArrayList<>();
        //openRequestCounter = 0; //TODO: Übergeben und dann getterMethode oder so? vielleicht wieder mit XML File, siehe Requestcounter?
        //
        gitHubModel = new GitHubModel(this);
        branchNameCreator = new BranchNameCreator(this);
        gitModel = new GitModel(project, this);
        nameGenerator = new NameGenerator(this);
        screenshotModel = new ScreenshotModel(this);
        mailModel = new MailModel(this);
        annotatedCodeWindow = new AnnotatedCodeWindow(this);
        gitModel.gitInit();
        //24.11. Neu gemacht.
        showWelcomeMenu();
        //24.11 auskommentiert und in LoginButton gepackt.
        //createTutorCommentsFolder();
        //showAnswersOnProgramStart();
        //24.11. - Der Thread soll erst gestartet werden, nachdem die Antworten gesetzt sind.
        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();
    }

    private void showAnswersOnProgramStart() throws IOException {
       //GitHub "durchsuchen" nach IssueIDList, Diese Issues dann in einer Liste speichern und diese dann der IssueList gleichsetzen
        System.out.println("Programmstart issueList: " + gitHubModel.issueList);
        System.out.println("Programstart Issue Liste geholt von den numbers: " + gitHubModel.getIssuesBySavedIds(getSavedRequests()));
        //gitHubModel.issueList = gitHubModel.getIssuesBySavedIds(getSavedRequests());
        //issueList = gitHubModel.issueList;
        issueList = gitHubModel.updateIssueList();
        //Hier gleich die AnswerList befüllen
        //TODO: Das dann ein bisschen weniger duplicatre machen mit der matchAnswerAndRequest
        System.out.println("Bei Programmstart: issueList (nach zuweisen mit den numbers aus configFile): " + issueList);

        /*Mein Plan ist es, Bei Programmstart erstmal die Antwortnummern aus der XML auszulesen, analog reqeuest.
        * Dann möchte ich diese vergleichen, also die Issues, die ich in der gespeicherten RequestsListe gespeichert habe,
        * überprüfe ich auf die Antwort nummern.
        * Wenn was gleich ist, dann wird dieser Issue in eine Antwort umgewandelt und zur Liste hinzugefügt.
        * Dann möchte ich gleich die Tabelle zeigen mit diesen Antworten.*/
        List<Answer> alreadyAnsweredRequests = getAlreadyAnsweredRequests();
        System.out.println("listi: " + alreadyAnsweredRequests);
        for (Answer answer : alreadyAnsweredRequests) {
            System.out.println("Die ConcurrentModificationException passiert hier.");
            answerList.add(answer);
        }
        System.out.println("answerlist: " + answerList);

    }

    public List<GHIssue> getOwnClosedIssues() {
        return gitHubModel.filterOwnClosedIssues(gitHubModel.allClosedIssueList);
    }

    public List<Answer> getAlreadyAnsweredRequests() throws IOException {
        return gitHubModel.getAlreadyAnsweredRequestsOnProgramStart(getSavedAnswers(), gitHubModel.issueList);
    }

    private void createTutorCommentsFolder() {
        File tutorCommentsFolder = new File(project.getBasePath() + Constants.TUTOR_COMMENTS_FOLDER);
        tutorCommentsFolder.mkdir();
    }

    public void onNewAnswerData() throws IOException {
        XMLFileReader.modifyOpenRequestsCounter(Integer.parseInt(openRequestsModel.decrementOpenRequestsNumber()));
        mailBoxScreen.updateOpenRequest();
        mailBoxScreen.refreshTable();
        mailBoxScreen.showNotification();
        System.out.println("Nach Decrement: " + getOpenRequestsValue());
    }

    public List<GHIssue> getOpenRequestsList() {
        return openRequestsModel.openRequestList;
    }

    private String getStudentName() {
        return settingScreen.inputNameField.getText();
    }

    public String getStudentNameInXML() {
        return XMLFileReader.readNameFromXML();
    }

    public String getStudentMail() {
        return settingScreen.inputMailAddressField.getText();
    }

    public String getStudentMailInXML() {
        return XMLFileReader.readMailFromXML();
    }

    //TODO: Das funktioniert so nicht wegen des Datums, das sich dauernd ändert
    public String getBranchName() {
        return branchNameCreator.createBranchName(getStudentName(), getCounterValue(), getCurrentDate());
    }

    private String getProblemCategory() {
        return sendRequestScreen.saveSelectedCategoryAsString();
    }

    public ArrayList<Integer> getSavedRequests() {
        return XMLFileReader.readRequestIdsFromXML();
    }


    private ArrayList<Integer> getSavedAnswers() {
        return XMLFileReader.readAnswerIdsFromXML();
    }

    private String getCounterValue() {
        return String.valueOf(XMLFileReader.readCounterValueFromXML());
    }

    //22.11. Test --> Offene Anfragen anzeigen mit Zahl. Da kann ich das benutzen. Und dann addieren?
    //TODO: wo verwenden??
   public String getOpenRequestsValue() {
        return String.valueOf(XMLFileReader.readOpenRequestsValueFromXML());
    }

  /* public String getOpenRequestsValue() {
       return String.valueOf(XMLFileReader.readOpenRequestsValueFromXML());
   }
*/

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }

    private void showWelcomeMenu() throws UnsupportedEncodingException {
        System.out.println("Controller showWelcomeMenu");
        //Wenn da was bei <token> steht
        if (XMLFileReader.checkIfInitialized()) {
            System.out.println("initailisiert? Sollte true sein" + XMLFileReader.checkIfInitialized());
            loginMenu.showLoginMenu();
            //loginMenu1.showLoginMenu();
        } else {
            System.out.println("initailisiert? Sollte false sein" + XMLFileReader.checkIfInitialized());
            loginMenu.showRegistrationMenu();
            //registrationMenu.showRegistrationMenu();
        }
    }

    private String getAllImageStrings() {
        return screenshotModel.addScreenshotsToIssue(screenshotModel.chooseFiles());
    }

    public void onSubmitRequestButtonPressed() {
        //String requestCounter = gitModel.incrementRequestCounter();
        String requestCounter = branchNameCreator.incrementRequestCounter();
        String requestDate = getCurrentDate();
        //TODO: Hier die Methode mit den imageStrings? Beides zusammenbauen und mit Zeilenumbrüchen trennen
        //String requestMessage = getRequestMessage() + "\n" + "\n" + getAllImageStrings();
        String requestMessage = getRequestMessage();
        //String requestMessage = sendRequestScreen.getInputMessage() + "\n" + "\n" + screenshotModel.getAllImagesString();
        System.out.println("requestMessage: (Bildtest)" + requestMessage);
        String studentName = getStudentName();
        System.out.println(requestMessage + studentName);

        if (requestMessage.length() == 0) { //TODO: Die Images müssen hier noch rausgelöscht werden!
            sendRequestScreen.showEmptyMessageError();
        } else if (studentName.length() == 0) {
            sendRequestScreen.showNoNameError();
        } else {
            try {
                String title = gitHubModel.createIssueTitle(studentName, requestDate);
                String labelCategory = Constants.COMMON_LABEL_BEGIN + getProblemCategory();
                String labelTask = Constants.COMMON_LABEL_BEGIN + getTaskNameFromFile();
                String labelBranchName = Constants.COMMON_LABEL_BEGIN + getBranchName();
                String labelScreenshot = getScreenShotLabel();
                gitModel.createAndPushBranch(getBranchName());
                gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName, labelScreenshot);
                //9.11. auskommentiert zu Testzwecken
                //mailModel.sendMailToTutors();
                //23.11.
               // openRequestsModel.openRequestList.add( gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName, labelScreenshot));
                System.out.println("openRequestsModel.openRequestList: " + openRequestsModel.openRequestList);
                //26.11. Versuch.
                XMLFileReader.modifyOpenRequestsCounter(Integer.parseInt(openRequestsModel.incrementOpenRequestsNumber()));
                mailBoxScreen.updateOpenRequest();
                //
            } catch (IOException | GitAPIException | URISyntaxException e) {
                //Fehlermeldung (sinnvoll)
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
                return;
            }
            sendRequestScreen.showSentRequestInfo();
            screenshotModel.clearScreenshotFolder();

            logData("Anfrage abgeschickt");
        }
    }

    private String getScreenShotLabel() {
        String screenShotLabel = Constants.COMMON_LABEL_BEGIN + "Ohne Screenshot";
        if (screenshotModel.hasScreenShotAttached()) {
            screenShotLabel = Constants.COMMON_LABEL_BEGIN + "Mit Screenshot";
        }
        return screenShotLabel;
    }

    public void onRegistrationButtonPressed() throws IOException {
        String password = getPasswordValidateInput();
        String encryptedPassword = loginManager.encryptPassword(password);
        String token = getToken();
        //Hier wird das Passwort validiert
        if (getPasswordValidateInput().length() >= Constants.MINIMUM_PASSWORD_LENGTH && getPasswordFirstInput().length() >= Constants.MINIMUM_PASSWORD_LENGTH) {
            if (getPasswordValidateInput().equals(getPasswordFirstInput())) {
                //if (Arrays.equals(loginMenu.getPasswordFirstInput(), loginMenu.getPasswordValidateInput())) {
                loginManager.createToken();
                loginManager.encryptPassword(password);
                XMLFileReader.modifyXMLTokenAndPassword(token, encryptedPassword);
                System.out.println("HashMap: " + loginData);
                loginMenu.showValidPasswordInfo();
                //registrationMenu.showValidPasswordInfo();
                loginMenu.hideRegistrationMenu();
                //registrationMenu.hideRegistrationMenu();
                //sendRequestScreen.showWelcomeInfo(); //NPE
            } else {
                loginMenu.showPasswordsNotEqualError();
                //registrationMenu.showPasswordsNotEqualError();
            }
        } else {
            loginMenu.showPasswordTooShortError();
            //registrationMenu.showPasswordTooShortError();
        }
    }

    //Erstes Feld
    private String getPasswordFirstInput() {
        return Arrays.toString(loginMenu.passwordFieldFirstInput.getPassword());
        //return Arrays.toString(loginMenu.passwordFieldFirstInput.getPassword());
    }

    //Zweites Feld
    private String getPasswordValidateInput() {
        return Arrays.toString(loginMenu.passwordFieldValidateInput.getPassword());
        //return Arrays.toString(registrationMenu.passwordFieldValidateInput.getPassword());
    }

    private String getPasswordLogin() {
        //return Arrays.toString(loginMenu1.passwordFieldLogin.getPassword());
        return Arrays.toString(loginMenu.passwordFieldLogin.getPassword());
    }

    public void onLoginButtonPressed() throws IOException {
        //TODO: Daten holen, Liste erstellen oder so und Listen vergleichen
        //String password = loginMenu.getPasswordLogin();
        String password = getPasswordLogin();
        System.out.println("password");
        String encryptedPasswordLogin = loginManager.encryptPassword(password);
        System.out.println("encryptedPasswordLogin: " + encryptedPasswordLogin);
        String encryptedPasswordXML = XMLFileReader.readEncryptedPasswordFromXML();
        System.out.println("encryptedPasswordXML" + encryptedPasswordXML);
        if (encryptedPasswordLogin.equals(encryptedPasswordXML)) {
            loginMenu.hideLoginMenu();
            //24.11. Test
            createTutorCommentsFolder();
            showAnswersOnProgramStart();
            //mailBoxScreen.hideNotLoggedInLabel(); //TODO NPE beheben!
        } else {
           loginMenu.showWrongPasswordError();
            //loginMenu.showWrongPasswordError();
        }
    }

    public void onSaveSettingsButtonPressed() throws IOException {
        String studentName = getStudentName();
        String studentMail = getStudentMail();
        XMLFileReader.modifyXMLNameAndMail(studentName, studentMail);
    }

    public void onOpenCodeButtonPressed() {
        System.out.println("openCodeButton Methodenaufruf aus Controller");
    }

    public void sendFeedbackForFeedback() {
        gitHubModel.matchFeedbackAndRequest(gitHubModel.answerNumber);
        //Switch geht leider nicht mit Objekten
        if (answerDetailScreen.selectedHelpfulness == 1) {
            gitHubModel.setHelpfulFeedbackLabel();
        } else if (answerDetailScreen.selectedHelpfulness == 2) {
            gitHubModel.setNeutralLabel();
        } else if (answerDetailScreen.selectedHelpfulness == 3) {
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

    public List<String> getDiffEntries() throws IOException, GitAPIException, BadLocationException {
        return gitModel.compareBranchesForCodeChanges();
    }

    //TODO: Noch abfangen, dass wenn Screenshots angehängt wurden, ist der Input nicht mehr 0!
    //Idee: Gettermethode schreiben für die Bilderstrings und dann Länge minus die Bilder, wenn das größer 0 ist
    private String getRequestMessage() {
        return sendRequestScreen.inputMessageArea.getText().trim();
    }

    private String getToken() {
        return loginManager.createToken();
    }

    private String getTaskNameFromFile() throws FileNotFoundException {
        return taskNameReader.readNameOfTaskFromNameFile();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }

    //Der Einfachheit halber
    public void logData(String logMessage) {
        Log log = LogManager.openLog(user.getID(), "Ma-Fischer");
        log.log(user.getSessionID(), LogDataType.USER, "log", logMessage);
    }
}
