package controller;

import com.intellij.openapi.wm.ToolWindow;
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
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

//zentrale Startklasse
public class Controller {
    //26.11.
    private final ToolWindow toolWindow;
    public ToolWindowPluginFactory toolWindowFactory;
    //
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
    //private LoginMenu loginMenu;
    //28.11.
    private QuestionnaireDialog questionnaireDialog;
    //24.11.
    //29.11.
    public LoginDialog loginDialog;
    public RegistrationDialog registrationDialog;
    //
    public XMLFileReader XMLFileReader;
    public TaskNameReader taskNameReader;
    public XMLFileCreator XMLFileCreator;
    public AnnotatedCodeWindow annotatedCodeWindow;
    public AnswerList answerList;
    //20.11.
    public ModifiedFilesReader modifiedFilesReader;
    public OpenRequestsModel openRequestsModel;
    //19.11.
    //public ArrayList<String> openRequests;

    public BranchNameCreator branchNameCreator;
    public NameGenerator nameGenerator;
    //10.11. Test
    public boolean hasChanges;
    //
    public boolean isLoggedIn;
    public boolean isNewRegistered;
    public boolean isInitialized;
    //
    //27.11.
    private String srcFolderPath;
    //
    private de.ur.mi.pluginhelper.User.User user;
    public Log log;

    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project, ToolWindow toolWindow) throws IOException, TransformerException, SAXException, ParserConfigurationException, BadLocationException {
        this.project = project;
        //26.11.
        this.toolWindow = toolWindow;
        loginManager = new LoginManager(this);
        //loginMenu = new LoginMenu(this);
        registrationDialog = new RegistrationDialog(this);
        loginDialog = new LoginDialog(this);
        questionnaireDialog = new QuestionnaireDialog(this);
        //
        //Create File: file wird nur einmal angelegt.
        XMLFileCreator = new XMLFileCreator(this);
        XMLFileReader = new XMLFileReader(this);
        isInitialized = XMLFileReader.checkIfInitialized();
        //
        openRequestsModel = new OpenRequestsModel(this);
        taskNameReader = new TaskNameReader(this);
        //27.11. Test
        srcFolderPath = project.getBasePath() + Constants.CLONED_SRC_FOLDER;
        //21.11.
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        Log log = LogManager.openLog(user.getID(), "MA-Fischer");
        //28.11. saveToXMLFile
        XMLFileReader.modifyUserID(user.getID());
        //
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
        //19.11.
        //openRequests = new ArrayList<>();
        //
        gitHubModel = new GitHubModel(this, toolWindow);
        branchNameCreator = new BranchNameCreator(this);
        gitModel = new GitModel(project, this);
        nameGenerator = new NameGenerator(this);
        screenshotModel = new ScreenshotModel(this);
        mailModel = new MailModel(this);
        annotatedCodeWindow = new AnnotatedCodeWindow(this);
        //27.11. Versuch
        //if(isInitialized = false) {
        gitModel.gitInit();
        //}
        showWelcomeMenu();
    }

    //TODO: Nur Datum ohne Uhrzeit reinspeichern!
//https://stackoverflow.com/questions/12087419/adding-days-to-a-date-in-java
    private void checkForQuestionnaire() {
        //TODO: Das noch richtig machen!
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
        //Das mache ich jetzt erstmal total gefaket
        String currentDate = "28.11.2019 15:50";
        String expectedQuestionnaireDate = "28.11.2019 15:50";

        if (expectedQuestionnaireDate.equals(currentDate)) {
            questionnaireDialog.showQuestionnaireDialog();
        }
        //TODO: Hier festen Zeitabstand und den dann irgendwie draufrechnen! (2Tage) --> Konstante, als Test 2 Minuten.
    }

    public void showWelcomeMenu() {
        System.out.println("Controller showWelcomeMenu");
        //Wenn da was bei <token> steht
        if (XMLFileReader.checkIfInitialized()) {
            System.out.println("initailisiert? Sollte true sein" + XMLFileReader.checkIfInitialized());
            //loginMenu.showLoginMenu();
            loginDialog.showLoginDialog();

        } else {
            System.out.println("initialisiert? Sollte false sein" + XMLFileReader.checkIfInitialized());
            registrationDialog.showRegistrationMenu();
            //loginMenu.showRegistrationMenu();
        }
    }

    public void onRegistrationButtonPressed() throws IOException {
        String password = getPasswordValidateInput();
        String encryptedPassword = loginManager.encryptPassword(password);
        String date = getCurrentDate();
        System.out.println("FirstInput: " + getPasswordFirstInput());
        //Hier wird das Passwort validiert
        //30.11. auskommentiert, weil Inhalt leider nicht ausgelesen wird TODO: Fixen!
        if (getPasswordValidateInput().length() >= Constants.MINIMUM_PASSWORD_LENGTH && getPasswordFirstInput().length() >= Constants.MINIMUM_PASSWORD_LENGTH) {
            if (getPasswordValidateInput().equals(getPasswordFirstInput())) {
                System.out.println("Nach erster Ifbedingung??!!!");
                loginManager.encryptPassword(password);
                XMLFileReader.modifyPassword(encryptedPassword);
                //28.11. Test, ob das geht.
                XMLFileReader.modifyDate(date);
                //loginMenu.showValidPasswordInfo();
                registrationDialog.showValidPasswordInfo();
                //loginMenu.hideRegistrationMenu();
                registrationDialog.hideRegistrationMenu();
                //sendRequestScreen.showWelcomeInfo(); //NPE
                isNewRegistered = true;
                isLoggedIn = true;
                createTutorCommentsFolder();
                //Thread starten
                startLookForAnswersThread();
            } else {
                //loginMenu.showPasswordsNotEqualError();
                registrationDialog.showPasswordsNotEqualError();
            }
        } else {
            //loginMenu.showPasswordTooShortError();
            registrationDialog.showPasswordTooShortError();
       }
    }

    public void onLoginButtonPressed() throws IOException {
        System.out.println("Bin hier rein gekommen!");
        if (loginManager.checkPassword()) {
            //loginMenu.hideLoginMenu();
            loginDialog.hideLoginMenu();
            showAnswersOnProgramStart();
            isLoggedIn = true;
            //28.11.
            checkForQuestionnaire();
            //26.11. Thread starten
            startLookForAnswersThread();
        } else {
            //loginMenu.showWrongPasswordError();
            loginDialog.showWrongPasswordError();
        }
    }

    public void updateToolWindow() {
        toolWindow.getContentManager().removeContent(toolWindowFactory.getContentLogin(), true);
        toolWindowFactory.addDefaultContents();
        toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(sendRequestScreen.getContent()));
    }

    private void startLookForAnswersThread() {
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();
    }

    private void showAnswersOnProgramStart() throws IOException {
        //GitHub "durchsuchen" nach IssueIDList, Diese Issues dann in einer Liste speichern und diese dann der IssueList gleichsetzen
        System.out.println("Programmstart issueList: " + gitHubModel.issueList);
        System.out.println("Programstart Issue Liste geholt von den numbers: " + gitHubModel.getIssuesBySavedIds(getSavedRequests()));
        issueList = getIssueList();
        System.out.println("Bei Programmstart: issueList (nach zuweisen mit den numbers aus configFile): " + issueList);


        List<Answer> alreadyAnsweredRequests = getAlreadyAnsweredRequests();
        System.out.println("listi: " + alreadyAnsweredRequests);
        for (Answer answer : alreadyAnsweredRequests) {
            answerList.add(answer);
            //TODO
            if (answerList.getAnswerList().size() > 0) {
                // mailBoxScreen.refreshTable();
            }
        }
    }

    private void createTutorCommentsFolder() {
        File tutorCommentsFolder = new File(project.getBasePath() + Constants.TUTOR_COMMENTS_FOLDER);
        tutorCommentsFolder.mkdir();
    }

    public void onNewAnswerData() throws IOException {
        //TODO: Das noch richtig machen
        if (XMLFileReader.readOpenRequestsValueFromXML() > 0) {
            XMLFileReader.modifyOpenRequestsCounter(Integer.parseInt(openRequestsModel.decrementOpenRequestsNumber()));
        }
        mailBoxScreen.updateOpenRequest();
        mailBoxScreen.refreshTable();
        mailBoxScreen.showNotification();
        System.out.println("Nach Decrement: " + getOpenRequestsValue());
    }

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }

    public void onSubmitRequestButtonPressed() {
        String requestCounter = branchNameCreator.incrementRequestCounter();
        String requestDate = getCurrentDate();
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
                String labelCategory = getProblemCategory();
                String labelTask = getTaskNameFromFile();
                String labelBranchName = Constants.COMMON_LABEL_BEGIN + getBranchName();
                String labelScreenshot = getScreenShotLabel();
                //28.11. auskommentiert wegen Fehlermeldung.
                gitModel.createAndPushBranch(getBranchName());
                gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName, labelScreenshot);
                mailModel.sendMailToTutors();
                //23.11.
                // openRequestsModel.openRequestList.add( gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName, labelScreenshot));
                System.out.println("openRequestsModel.openRequestList: " + openRequestsModel.openRequestList);
                //26.11. Versuch.
                XMLFileReader.modifyOpenRequestsCounter(Integer.parseInt(openRequestsModel.incrementOpenRequestsNumber()));
                mailBoxScreen.updateOpenRequest();
                //29.11. Hierher verschoben.
                sendRequestScreen.showSentRequestInfo();
                screenshotModel.clearScreenshotFolder();
                logData("Anfrage abgeschickt");
            } catch (IOException e) {
                //Fehlermeldung (sinnvoll)
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
                return;
            } catch (GitAPIException | URISyntaxException e) {
                e.printStackTrace();
            }

        }
    }

    public void onSaveSettingsButtonPressed() throws IOException {
        String studentName = getStudentName();
        String studentMail = getStudentMail();
        XMLFileReader.modifyXMLNameAndMail(studentName, studentMail);
    }

    //TODO: Hier die Tutorialscreen funktionalität reinhauen!
    public void onOpenCodeButtonPressed() {
        System.out.println("openCodeButton Methodenaufruf aus Controller");
        logData("Codefenster geöffnet");

        try {
            //TODO: Hier muss noch was anderes übergeben werden! gitStatus gibt modifiedFileNames zurück.
            annotatedCodeWindow.createWindow(getModifiedFiles());
        } catch (BadLocationException | GitAPIException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendFeedbackForFeedback() {
        gitHubModel.matchFeedbackAndRequest(gitHubModel.answerNumber);
        //Switch geht leider nicht mit Objekten
        if (answerDetailScreen.selectedHelpfulness == 1) {
            gitHubModel.setHelpfulFeedbackLabel();
        } else if (answerDetailScreen.selectedHelpfulness == 2) {
           gitHubModel.setNeutralLabel(); //TODO: Button dafür noch machen
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

    //Getter-Methoden
    public List<GHIssue> getOpenRequestsList() {
        return openRequestsModel.openRequestList;
    }

    private String getScreenShotLabel() {
        String screenShotLabel = "Ohne Screenshot";
        if (screenshotModel.hasScreenShotAttached()) {
            screenShotLabel = "Mit Screenshot";
        }
        return screenShotLabel;
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
    private String getBranchName() {
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

    private String getOpenRequestsValue() {
        return String.valueOf(XMLFileReader.readOpenRequestsValueFromXML());
    }

    public List<String> getDiffEntries() throws IOException, GitAPIException, BadLocationException {
        return gitModel.compareBranchesForCodeChanges();
    }

    public List<GHIssue> getOwnClosedIssues() throws IOException {
        return gitHubModel.filterOwnClosedIssues();
    }

    private List<Answer> getAlreadyAnsweredRequests() throws IOException {
        return gitHubModel.getAlreadyAnsweredRequestsOnProgramStart(getSavedAnswers(), gitHubModel.issueList);
    }

    public String getRequestMessage() {
        return sendRequestScreen.inputMessageArea.getText().trim();
    }

    private String getTaskNameFromFile() throws FileNotFoundException {
        return taskNameReader.readNameOfTaskFromNameFile();
    }
//TODO: Hier vllt auch noch anpassen, welches DateFormat übergeebn werden soll
    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }

    //Erstes Feld
    private String getPasswordFirstInput() {
        System.out.println("1: " + Arrays.toString(registrationDialog.passwordFieldFirstInput.getPassword()));
        //return Arrays.toString(loginMenu.passwordFieldFirstInput.getPassword());
        return Arrays.toString(registrationDialog.passwordFieldFirstInput.getPassword());
    }

    //Zweites Feld
    private String getPasswordValidateInput() {
        System.out.println("2: " + Arrays.toString(registrationDialog.passwordFieldValidateInput.getPassword()));
        //return Arrays.toString(loginMenu.passwordFieldValidateInput.getPassword());
        return Arrays.toString(registrationDialog.passwordFieldValidateInput.getPassword());
    }

    public String getPasswordLogin() {
        return Arrays.toString(loginDialog.passwordFieldLogin.getPassword());
        //return Arrays.toString(loginMenu.passwordFieldLogin.getPassword());
    }

    //LogMethode
    public void logData(String logMessage) {
        Log log = LogManager.openLog(user.getID(), "Ma-Fischer");
        log.log(user.getSessionID(), LogDataType.USER, "log", logMessage);
    }


    //27.11. testt
    public List<File> getModifiedFiles() throws IOException, GitAPIException, BadLocationException {
        return modifiedFilesReader.matchFiles(modifiedFilesReader.listAllFiles(srcFolderPath), gitModel.gitStatus());
    }

    public List<GHIssue> getIssueList() throws IOException {
        return gitHubModel.updateIssueList();
    }

        public void onSubmitQuestionnaireButtonPressed() {
        System.out.println("onSubmitQuestionnaireButtonPressed");
        //Hier wird der Link zusammengebaut aus festem Link und der userID
        String urlString = XMLFileReader.readLinkFromXML() + XMLFileReader.readUserIDFromXML();
        //Datum wird angepasst, damit man wieder zwei Tage drauf rechnen kann
        String date = getCurrentDate();
        XMLFileReader.modifyDate(date);
        try {
            System.out.println("tryyyy");
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
