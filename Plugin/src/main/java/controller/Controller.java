//TODO Bei Programmstart: PopUp mit: Willst du dich neu registrieren oder die verwendete ID benutzen?
package controller;

import actions.BalloonPopup;
import actions.MailModel;
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
import java.util.Date;
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
    public String xmlFilePath;
    public boolean isInitialized;

    public File projectPath;

    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project) throws IOException, TransformerException, SAXException, ParserConfigurationException {
        this.project = project;
        //this.xmlFilePath = project.getBasePath()  + "/.idea/personalConfig.xml";

        loginManager = new LoginManager(this); //TODO: Das noch umschreiben, immer aus dem Controller aufrufen
        loginMenu = new LoginMenu(this);
        //5.11.
        isInitialized = false;

        createXMLFile = new CreateXMLFile(this);
        readFiles = new ReadFiles(this);

        gitHubModel = new GitHubModel(this);
        gitModel = new GitModel(project, this);
        screenshotModel = new ScreenshotModel(this);
        mailModel = new MailModel(this);
        balloonPopup = new BalloonPopup();

        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();

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
    public String getStudentName() {
        //return settings.getName();
        return settingScreen.getStudentNameInput();
    }

    public String getStudentMail() {
        return settingScreen.getStudentMailInput();
    }

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }
    //5.11. Wichtig. Hier noch die Show Methoden holen.
    public void showWelcomeMenu() throws IOException {
        System.out.println("Controller showWelcomeMenu");
        if(isInitialized) {
            loginMenu.showLoginMenu();
        } else {
            loginMenu.showRegistrationMenu();
            //loginMenu.showLoginMenu();
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
            sendRequestScreen.showNoNameWarning();
        } else {
            try {
                String title = gitHubModel.createIssueTitle(studentName, requestDate);
                String labelCategory = Constants.COMMON_LABEL_BEGIN + SendRequestScreen.problemCategory;
                String labelTask = Constants.COMMON_LABEL_BEGIN + getTaskNameFromFile();
                String labelBranchName = Constants.COMMON_LABEL_BEGIN + gitModel.createBranchName(studentName, requestCounter, requestDate);
                //String labelScreenshot = screenshotModel.getScreenshotLabel();
                //TODO: Das nicht hardcoden und Bedingung einbauen
                String labelScreenshot = "screenshot";
                gitModel.createAndPushBranch(gitModel.createBranchName(studentName, requestCounter, requestDate));
                gitHubModel.createIssue(title, requestMessage, labelCategory, labelTask, labelBranchName);
                mailModel.sendMailToTutors();

            } catch (IOException | GitAPIException | URISyntaxException e) {
                //} catch (Exception e) {
                //TODO Fehlermeldung (sinnvoll)
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
                return;
            }
            sendRequestScreen.showSentRequestInfo();
        }
    }

    public void onRegistrationButtonPressed() throws IOException {
        //String password = loginMenu.getPasswordLogin().toString();
        String password = loginMenu.getPasswordValidateInput().toString();
        String encryptedPassword = loginManager.encryptPassword(password);
        //String encryptedPassword = getEncryptedPassword();
        String token = getToken();
        //Das muss ich noch umschreiben! --> LoginManager
        if(loginMenu.getPasswordValidateInput().length >= 8 && loginMenu.getPasswordFirstInput().length >= 8) {
            if (loginMenu.getPasswordFirstInput().equals(loginMenu.getPasswordValidateInput())) {
                //if(loginManager.validatePassword(password)) {
                loginManager.createToken();
                loginManager.encryptPassword(password);
                readFiles.readAndModifyXML(token, encryptedPassword);
                //TODO: Das noch in HashMap speichern? Token ist die ID
                //Wichtig!
                //TODO: Das muss noch gespeichert werden --> Damit es beim Programmstart aufgerufen wird!
                isInitialized = true;
                loginMenu.showValidPasswordInfo();
            } else {
                loginMenu.showPasswordsNotEqualError();
            }
        } else {
            loginMenu.showPasswordTooShortError();
        }



    }

    public void onLoginButtonPressed() {
        readFiles.readEncryptedPasswordFromXML();
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
        return readFiles.readNameFile();
    }

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }

}
