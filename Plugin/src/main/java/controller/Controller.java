package controller;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBColor;
import de.ur.mi.pluginhelper.logger.Log;
import de.ur.mi.pluginhelper.logger.LogDataType;
import de.ur.mi.pluginhelper.logger.LogManager;
import communication.MailModel;
import de.ur.mi.pluginhelper.logger.SyncProgressListener;
import fileHandler.GitIgnoreModifier;
import fileHandler.ModifiedFilesReader;
import gui.AnnotatedCodeWindow;
import answers.Answer;
import answers.AnswerList;
import com.intellij.openapi.project.Project;
import config.Constants;
import gui.*;
import login.QuestionnaireTimeCalculator;
import login.XMLFileCreator;
import fileHandler.XMLFileReader;
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
import fileHandler.TaskNameReader;


import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

/*
* In this class, the plugin is controlled */
public class Controller {
    private final ToolWindow toolWindow;
    public ToolWindowPluginFactory toolWindowFactory;
    private List<GHIssue> issueList;
    public Project project;
    public GitModel gitModel;
    public GitHubModel gitHubModel;
    public MailBoxScreen mailBoxScreen;
    public AnswerDetailScreen answerDetailScreen;
    public SendRequestScreen sendRequestScreen;
    public SettingScreen settingScreen;
    public ScreenshotModel screenshotModel;
    public MailModel mailModel;
    public LoginManager loginManager;
    //Between Questionnaire
    private QuestionnaireDialog questionnaireDialog;
    public QuestionnaireTimeCalculator questionnaireTimeCalculator;
    //Login
    private LoginDialog loginDialog;
    private RegistrationDialog registrationDialog;
    public XMLFileReader XMLFileReader;
    public XMLFileCreator XMLFileCreator;
    private TaskNameReader taskNameReader;
    private GitIgnoreModifier gitIgnoreModifier;
    private AnnotatedCodeWindow annotatedCodeWindow;
    public AnswerList answerList;
    public ModifiedFilesReader modifiedFilesReader;
    private OpenRequestsModel openRequestsModel;
    private BranchNameCreator branchNameCreator;
    public NameGenerator nameGenerator;
    public boolean hasChanges;
    public boolean isLoggedIn;
    public boolean isNewRegistered;
    public boolean isInitialized;
    private String srcFolderPath;
    //Logging function
    private de.ur.mi.pluginhelper.User.User user;
    public Log log;

    public Controller(Project project, ToolWindow toolWindow) throws IOException, TransformerException, SAXException, ParserConfigurationException, BadLocationException {
        this.project = project;
        this.toolWindow = toolWindow;
        loginManager = new LoginManager(this);
        registrationDialog = new RegistrationDialog(this);
        loginDialog = new LoginDialog(this);
        questionnaireTimeCalculator = new QuestionnaireTimeCalculator(this);
        questionnaireDialog = new QuestionnaireDialog(this);
        //Create XMLfile only once on programmstart
        XMLFileCreator = new XMLFileCreator(this);
        XMLFileReader = new XMLFileReader(this);
        isInitialized = XMLFileReader.checkIfInitialized();
        openRequestsModel = new OpenRequestsModel(this);
        taskNameReader = new TaskNameReader(this);
        srcFolderPath = project.getBasePath() + Constants.CLONED_SRC_FOLDER;
        //Log method for evaluation
        user = de.ur.mi.pluginhelper.User.User.getLocalUser();
        Log log = LogManager.openLog(user.getID(), "MA-Fischer");
        XMLFileReader.modifyUserID(user.getID()); //ID generated by logging gets saved in the configuration file
        gitIgnoreModifier = new GitIgnoreModifier(this);
        log.log(user.getSessionID(), LogDataType.USER, "log", "Plugin gestartet");
        String serverUrl = Constants.SEAFILE_SERVER_URL;
        // Log gets synchronised with Seafile
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

        modifiedFilesReader = new ModifiedFilesReader(this);
        answerList = new AnswerList();
        gitHubModel = new GitHubModel(this, toolWindow);
        branchNameCreator = new BranchNameCreator(this);
        gitModel = new GitModel(project, this);
        nameGenerator = new NameGenerator(this);
        screenshotModel = new ScreenshotModel(this);
        mailModel = new MailModel(this);
        annotatedCodeWindow = new AnnotatedCodeWindow(this);
        gitModel.gitInit();
        showWelcomeMenu();
    }

    //evaluation feature. On programm start it gets calculated if its time for filling out the questionnaire
    private void checkForQuestionnaire() throws ParseException {
        if(questionnaireTimeCalculator.calculateQuestionnaireDelta()) {
            questionnaireDialog.showQuestionnaireDialog();
        }
    }

    public void showWelcomeMenu() {
        //if there is a password saved in the xml file, the project is initizialized
        if (XMLFileReader.checkIfInitialized()) {
            loginDialog.showLoginDialog();
        } else {
            registrationDialog.showRegistrationMenu();
        }
    }

    public void onRegistrationButtonPressed() throws IOException {
        String password = getPasswordValidateInput();
        String encryptedPassword = loginManager.encryptPassword(password);
        String date = getCurrentDate();
        //password gets validated
        if (getPasswordValidateInput().length() >= Constants.MINIMUM_PASSWORD_LENGTH && getPasswordFirstInput().length() >= Constants.MINIMUM_PASSWORD_LENGTH) {
            if (getPasswordValidateInput().equals(getPasswordFirstInput())) {
                loginManager.encryptPassword(password);
                XMLFileReader.modifyPassword(encryptedPassword);
                XMLFileReader.modifyDate(date);
                registrationDialog.showValidPasswordInfo();
                registrationDialog.hideRegistrationMenu();
                isNewRegistered = true;
                isLoggedIn = true;
                createTutorCommentsFolder();
                gitIgnoreModifier.modifyGitignoreFile();
                //Thread which look for answers gets started
                startLookForAnswersThread();
            } else {
                registrationDialog.showPasswordsNotEqualError();
            }
        } else {
            registrationDialog.showPasswordTooShortError();
       }
    }

    //Callback for login button pressed
    public boolean onLoginButtonPressed() throws IOException, ParseException {
        if(getPasswordLogin().length() > 0) {
            if (loginManager.checkPassword()) {
                loginDialog.hideLoginMenu();
                showAnswersOnProgramStart();
                isLoggedIn = true;
                logData("Eingeloggt");
                checkForQuestionnaire();
                //Thread which look for answers gets started
                startLookForAnswersThread();
                return true;
            } else {
                loginDialog.showWrongPasswordError();
            }
        } else {
            loginDialog.showEmptyPasswordError();
        }
        return false;
    }

    public void updateToolWindow() {
            toolWindow.getContentManager().removeContent(toolWindowFactory.getContentLogin(), true);
            toolWindowFactory.addDefaultContents();
            toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(sendRequestScreen.getContent()));
    }

    public void onSubmitQuestionnaireButtonPressed() throws IOException {
        //Hier wird der Link zusammengebaut aus festem Link und der userID
        String urlString = XMLFileReader.readLinkFromXML() + XMLFileReader.readUserIDFromXML();
        //Datum wird angepasst, damit man wieder zwei Tage drauf rechnen kann
        String date = getCurrentDate();
        XMLFileReader.modifyDate(date);
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLookForAnswersThread() {
        GitHubListener gitHubListener = new GitHubListener(this);
        gitHubListener.start();
    }

    private void showAnswersOnProgramStart() throws IOException {
        //Already answered requests are searched in GitHub by number
        issueList = getIssueList();

        List<Answer> alreadyAnsweredRequests = getAlreadyAnsweredRequests();
        for (Answer answer : alreadyAnsweredRequests) {
            answerList.add(answer);
        }
    }

    private void createTutorCommentsFolder() {
        File tutorCommentsFolder = new File(project.getBasePath() + Constants.TUTOR_COMMENTS_FOLDER);
        tutorCommentsFolder.mkdir();
    }

    public void onNewAnswerData() throws IOException {
        if (XMLFileReader.readOpenRequestsValueFromXML() > 0) {
            XMLFileReader.modifyOpenRequestsCounter(Integer.parseInt(openRequestsModel.decrementOpenRequestsNumber()));
        }
        mailBoxScreen.updateOpenRequest();
        mailBoxScreen.refreshTableThread();
        mailBoxScreen.showNotification();
    }

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }

    class RequestAnswerThread extends Thread{
        String studentName, requestDate, requestMessage;
        RequestAnswerThread(String studentName, String requestDate, String requestMessage) {
            this.studentName = studentName;
            this.requestDate = requestDate;
            this.requestMessage = requestMessage;
        }

        /*This thread is for getting the correct order of actions: on click of the submit request button,
        * first, the button should turn grey and change its text in order to inform the user that the request is in process*/
        public void run() {
            String title = gitHubModel.createIssueTitle(studentName, requestDate);
            String labelCategory = getProblemCategory();
            //When there is no .name file, this has to be hardcoded for labeling the current project to the issues
            String labelTask = "WS1920 SL2";
            //only works, if there is a .name file
            /*String labelTask = null;
            try {

                labelTask = getTaskNameFromFile();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }*/
            String labelBranchName = Constants.COMMON_LABEL_BEGIN + getBranchName();
            String labelScreenshot = getScreenShotLabel();

            try {
                gitModel.createAndPushBranch(getBranchName());
            } catch (IOException | GitAPIException e) {
                e.printStackTrace();
            }
            gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName, labelScreenshot);

            try {
                XMLFileReader.modifyOpenRequestsCounter(Integer.parseInt(openRequestsModel.incrementOpenRequestsNumber()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            mailBoxScreen.updateOpenRequest();
            sendRequestScreen.showSentRequestInfo();
            screenshotModel.clearScreenshotFolder();

            try {
                gitModel.commitChanges();
            } catch (GitAPIException e) {
                e.printStackTrace();
            }
            logData("Anfrage abgeschickt");

            mailModel.sendMailToTutors();
            resetSubmitButton();
        }
    }

    private void resetSubmitButton() {
        sendRequestScreen.inputMessageArea.setText("");
        screenshotModel.deleteScreenshots();
        sendRequestScreen.submitRequestButton.setBackground(Constants.HEIDENELKENROT);
        sendRequestScreen.submitRequestButton.setEnabled(true);
        sendRequestScreen.submitRequestButton.setText("Hilfe anfragen");
    }

    public void onSubmitRequestButtonPressed() {

        String requestCounter = branchNameCreator.incrementRequestCounter();
        String requestDate = getCurrentDate();
        String requestMessage = getRequestMessage();

        String studentName = getStudentName();

        if (requestMessage.length() == 0) {
            sendRequestScreen.showEmptyMessageError();
        } else if (studentName.length() == 0) {
            sendRequestScreen.showNoNameError();
        } else {
            try {
                //turns grey to inform the users that the request is in process
                sendRequestScreen.submitRequestButton.setBackground(JBColor.LIGHT_GRAY);
                sendRequestScreen.submitRequestButton.setText("Anfrage wird verarbeitet");
                sendRequestScreen.submitRequestButton.setEnabled(false);
                sendRequestScreen.getContent().validate();
                sendRequestScreen.submitRequestButton.repaint();
                //Thread is startet for the correct order
                new RequestAnswerThread(studentName, requestDate, requestMessage).start();

            } catch (Exception e) {
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
            }

        }
    }

    public void onSaveSettingsButtonPressed() throws IOException {
        String studentName = getStudentName();
        String studentMail = getStudentMail();
        XMLFileReader.modifyXMLNameAndMail(studentName, studentMail);
        toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(sendRequestScreen.getContent()));
        sendRequestScreen.updateLabel();
    }

    public void onOpenCodeButtonPressed() {
        logData("Codefenster geöffnet");
        try {
            annotatedCodeWindow.createWindow(getModifiedFiles());
        } catch (BadLocationException | GitAPIException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendFeedbackForFeedback(int selectedHelpfulness) {
        gitHubModel.matchFeedbackAndRequest(gitHubModel.answerNumber);
        if (selectedHelpfulness == 1) {
            gitHubModel.setHelpfulFeedbackLabel();
        } else if (selectedHelpfulness == 2) {
           gitHubModel.setNeutralLabel();
        } else if(selectedHelpfulness == 3) {
            gitHubModel.setNotHelpfulFeedbackLabel();
        }
        if(selectedHelpfulness == 4) {
            gitHubModel.setProblemSolvedLabel();
        }
        answerDetailScreen.createFeedbackText();
        answerDetailScreen.showSentFeedbackBalloon();
    }


    public void onAnswerSelected(Answer answer) {
        logData("Antwort angeklickt");
        toolWindowFactory.addAnswerDetailContent();
        mailBoxScreen.showAnswerDetailContent(answer);
    }

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

    public List<GHIssue> getOwnClosedIssues() throws IOException {
        return gitHubModel.filterOwnClosedIssues();
    }

    private List<Answer> getAlreadyAnsweredRequests() throws IOException {
        return gitHubModel.getAlreadyAnsweredRequestsOnProgramStart(getSavedAnswers(), gitHubModel.issueList);
    }

    private String getRequestMessage() {
        return sendRequestScreen.inputMessageArea.getText().trim();
    }

    private String getTaskNameFromFile() throws FileNotFoundException {
        return taskNameReader.readNameOfTaskFromNameFile();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }

    //First field registration
    private String getPasswordFirstInput() {
        return Arrays.toString(registrationDialog.passwordFieldFirstInput.getPassword());
    }

    //Second field registration
    private String getPasswordValidateInput() {
        return Arrays.toString(registrationDialog.passwordFieldValidateInput.getPassword());
    }

    public String getPasswordLogin() {
        return Arrays.toString(loginDialog.passwordFieldLogin.getPassword());
    }

    public List<File> getModifiedFiles() throws IOException {
        return modifiedFilesReader.collectModifiedFiles();
    }

    public List<GHIssue> getIssueList() throws IOException {
        return gitHubModel.updateIssueList();
    }

    //Logmethod
    public void logData(String logMessage) {
        Log log = LogManager.openLog(user.getID(), "Ma-Fischer");
        log.log(user.getSessionID(), LogDataType.USER, "log", logMessage);
    }



}
