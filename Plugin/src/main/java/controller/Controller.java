package controller;

import de.ur.mi.pluginhelper.logger.Log;
import de.ur.mi.pluginhelper.logger.LogDataType;
import de.ur.mi.pluginhelper.logger.LogManager;
import gui.BalloonPopup;
import communication.MailModel;
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
    private BalloonPopup balloonPopup;
    public SendRequestScreen sendRequestScreen;
    public SettingScreen settingScreen;
    public ScreenshotModel screenshotModel;
    public MailModel mailModel;
    public LoginManager loginManager;
    public LoginMenu loginMenu;
    public XMLFileReader XMLFileReader;
    public TaskNameReader taskNameReader;
    public XMLFileCreator XMLFileCreator;
    public AnnotatedCodeWindow annotatedCodeWindow;
    public HashMap<String, String> loginData;
    public AnswerList answerList;
    //20.11.
    public ModifiedFilesReader modifiedFilesReader;

    //19.11.
    public ArrayList<String> openRequests;
    public int openRequestCounter;

    public BranchNameCreator branchNameCreator;
    public NameGenerator nameGenerator;
    //TODO: Alle unbenutzen Variablen in allen Klassen raushauen und TODOS ein bisschen abarbeiten
    //10.11. Test
    public boolean hasChanges;
    public de.ur.mi.pluginhelper.User.User user;

    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project) throws IOException, TransformerException, SAXException, ParserConfigurationException, BadLocationException {
        this.project = project;
        //this.xmlFilePath = project.getBasePath()  + "/.idea/personalConfig.xml";
        loginManager = new LoginManager(this);
        loginMenu = new LoginMenu(this);
        //Create File: file wird nur einmal angelegt.
        XMLFileCreator = new XMLFileCreator(this);
        XMLFileReader = new XMLFileReader(this);
        //Bin ich initialisiert?
        XMLFileReader.checkIfInitialized();
        //9.11. Steht das an der richtigen Stelle?
        taskNameReader = new TaskNameReader(this);
        //6.11.
        loginData = new HashMap<>();
        // createXMLFile = new CreateXMLFile(this);
        //readFiles = new ReadFiles(this);
        //21.11.
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        Log log = LogManager.openLog(user.getID());
        log.log(user.getSessionID(), LogDataType.USER, "log", "Plugin gestartet");

        //20.11.
        modifiedFilesReader = new ModifiedFilesReader(this);
        //15.11.
        answerList = new AnswerList();
        //hasChanges = false;
        //19.11.
        openRequests = new ArrayList<>();
        openRequestCounter = 0; //TODO: Übergeben und dann getterMethode oder so? vielleicht wieder mit XML File, siehe Requestcounter?
        //
        gitHubModel = new GitHubModel(this);
        //11.11. Ist das hier richtig?
        branchNameCreator = new BranchNameCreator(this);
        //
        gitModel = new GitModel(project, this);
        //11.11. Ist das hier richtig?
        nameGenerator = new NameGenerator(this);
        //
        screenshotModel = new ScreenshotModel(this);
        mailModel = new MailModel(this);
        balloonPopup = new BalloonPopup();

        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();

        annotatedCodeWindow = new AnnotatedCodeWindow(this);
        gitModel.gitInit();
        //showWelcomeMenu();
        createTutorCommentsFolder();
    }

    private void createTutorCommentsFolder() {
        File tutorCommentsFolder = new File(project.getBasePath() + Constants.TUTOR_COMMENTS_FOLDER);
        tutorCommentsFolder.mkdir();
    }

    public void onNewAnswerData() {
        //19.11. Test, ob das funktioniert. Wenn eine neue Antwort da ist, soll der Ordner wieder gelöscht werden.
        gitModel.deleteClonedBranchFolder();
        mailBoxScreen.refreshTable();
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

    public ArrayList<Long> getSavedRequest() {
        return XMLFileReader.readRequestIdsFromXML();
    }

    private String getCounterValue() {
        return String.valueOf(XMLFileReader.readCounterValueFromXML());
    }

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }

    private void showWelcomeMenu() throws UnsupportedEncodingException {
        System.out.println("Controller showWelcomeMenu");
        //Wenn da was bei <token> steht
        if (XMLFileReader.checkIfInitialized()) {
            System.out.println("initailisiert? Sollte true sein" + XMLFileReader.checkIfInitialized());
            loginMenu.showLoginMenu();
        } else {
            System.out.println("initailisiert? Sollte false sein" + XMLFileReader.checkIfInitialized());
            loginMenu.showRegistrationMenu();
        }
    }

    public void onSubmitRequestButtonPressed() {
        //String requestCounter = gitModel.incrementRequestCounter();
        String requestCounter = branchNameCreator.incrementRequestCounter();
        String requestDate = getCurrentDate();
        //TODO: Hier die Methode mit den imageStrings? Beides zusammenbauen und mit Zeilenumbrüchen trennen
        String requestMessage = getRequestMessage();
        //String requestMessage = sendRequestScreen.getInputMessage() + "\n" + "\n" + screenshotModel.getAllImagesString();
        System.out.println(requestMessage);
        String studentName = getStudentName();
        System.out.println(requestMessage + studentName);

        if (requestMessage.length() == 0) {
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

            } catch (IOException | GitAPIException | URISyntaxException e) {
                //Fehlermeldung (sinnvoll)
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
                return;
            }
            sendRequestScreen.showSentRequestInfo();
            screenshotModel.clearScreenshotFolder();
            //19.11. Die Anfrage wird der Liste hinzugefügt, welche Anfragen denn noch offen sind. Bzw wie viele.
            openRequestCounter++;
            openRequests.add(requestMessage);
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
                //TODO: Das noch in HashMap speichern? Token ist die ID --> Alex fragen
                //Wichtig!
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

    //Erstes Feld
    private String getPasswordFirstInput() {
        return Arrays.toString(loginMenu.passwordFieldFirstInput.getPassword());
    }

    //Zweites Feld
    private String getPasswordValidateInput() {
        return Arrays.toString(loginMenu.passwordFieldValidateInput.getPassword());
    }

    public void onLoginButtonPressed() {
        //TODO: Daten holen, Liste erstellen oder so und Listen vergleichen
        String password = loginMenu.getPasswordLogin();
        String encryptedPasswordLogin = loginManager.encryptPassword(password);
        System.out.println("encryptedPasswordLogin: " + encryptedPasswordLogin);
        String encryptedPasswordXML = XMLFileReader.readEncryptedPasswordFromXML();
        System.out.println("encryptedPasswordXML" + encryptedPasswordXML);
        if (encryptedPasswordLogin.equals(encryptedPasswordXML)) {
            loginMenu.hideLoginMenu();
        } else {
            loginMenu.showWrongPasswordError();
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

    private String getRequestMessage() {
        return sendRequestScreen.getInputMessage();
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
}
