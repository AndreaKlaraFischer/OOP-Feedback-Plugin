//TODO Bei Programmstart: PopUp mit: Willst du dich neu registrieren oder die verwendete ID benutzen?
package controller;

import actions.BalloonPopup;
import actions.MailModel;
import answers.Answer;
import com.intellij.openapi.project.Project;
import config.Constants;
import config.SettingsService;
import gui.AnswerDetailScreen;
import gui.MailBoxScreen;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.github.GHIssue;
import communication.GitHubModel;
import communication.GitModel;
import requests.CodeModel;
import requests.IDCreator;
import requests.ScreenshotModel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private List<GHIssue> issueList;
    private Project project;
    public IDCreator idCreator;
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
    private MailModel mailModel;
    public CodeModel codeModel;

    public File projectPath;

    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project) throws IOException {
        idCreator = new IDCreator();
        gitHubModel = new GitHubModel(this);
        gitModel = new GitModel(project, this);
        codeModel = new CodeModel(this);
        screenshotModel = new ScreenshotModel(this);
        //gitHubModel = new GitHubModel(this);
        mailModel = new MailModel();
        balloonPopup = new BalloonPopup();

        //Hier wird der Thread aufgerufen
        gitHubListener = new GitHubListener(this);
        gitHubListener.start();

        //TODO: Hier das Projekt holen!


        try {
            //settings.setValue("Ich bin ein gespeicherter Text!");
            settings.setValue(gitHubModel.studentName);
        } catch (Exception e) {
            System.out.println("something something state" + e.toString());
        }

        System.out.println("Our state is: " + settings.getValue());

        this.project = project;
        gitModel.gitInit();

    }

    public void onNewAnswerData() {
        mailBoxScreen.refreshTable();
    }

    //TODO: Das muss noch aufgerufen werden!
    public String getStudentName() {
        //return settings.getName();
        return settingScreen.getStudentNameInput();
    }

    public void onSelectFileButtonPressed() {
        screenshotModel.chooseFiles();
    }

    public void onOpenCodeButtonPressed() {
        System.out.println("openCodeButton Methodenaufruf aus Controller");
    }

    public void sendFeedbackForFeedback() {
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

    public void onAnswerSelected(Answer answer) {
        mailBoxScreen.showAnswerDetailContent(answer);
    }

    public void onSubmitRequestButtonPressed() {
       String requestCounter = gitModel.requestCounter();
        String requestDate = getCurrentDate();
        //TODO: Hier die Methode mit den imageStrings? Beides zusammenbauen und mit Zeilenumbrüchen trennen
        String requestMessage = sendRequestScreen.getInputMessage();

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
                //TODO: getBranchName umschreiben!
               String labelBranchName = Constants.COMMON_LABEL_BEGIN + gitModel.createBranchName(studentName, requestCounter, requestDate);
               gitModel.createAndPushBranch(gitModel.createBranchName(studentName, requestCounter, requestDate));
               gitHubModel.createIssue(title, requestMessage, labelCategory, labelBranchName);

               // mailModel.sendMailToTutors();

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

    private String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date currentTime = new Date();
        return dateFormat.format(currentTime);
    }


}
