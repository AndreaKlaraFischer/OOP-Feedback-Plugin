//TODO Bei Programmstart: PopUp mit: Willst du dich neu registrieren oder die verwendete ID benutzen?
package controller;

import actions.BalloonPopup;
import actions.MailModel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import config.Constants;
import config.SettingsService;
import gui.MailBoxScreen;
import gui.SendRequestScreen;
import gui.SettingScreen;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.github.GHIssue;
import communication.GitHubModel;
import communication.GitModel;
import requests.IDCreator;
import requests.ScreenshotModel;
import requests.StudentRequestModel;

import java.io.IOException;
import java.util.List;

//zentrale Startklasse
//beim Ausführen der Entwicklungsumgebung muss das Repo geklont werden
public class Controller {
    private List<GHIssue> issueList;
    private Project project;
    public StudentRequestModel studentRequestModel;
    public IDCreator idCreator;
    public GitModel gitModel;
    public GitHubModel gitHubModel;
    private GitHubListener gitHubListener;
    private SettingsService settings = SettingsService.getInstance();
    public MailBoxScreen mailBoxScreen;
    private BalloonPopup balloonPopup;
    public SendRequestScreen sendRequestScreen;
    public SettingScreen settingScreen;

    public ScreenshotModel screenshotModel;
    public String studentName;


    //18.10.
    public MailModel mailModel;
    //Hier ist die Reihenfolge wichtig!
    public Controller(Project project) {
        idCreator = new IDCreator();

        gitModel = new GitModel(project);
        gitHubModel = new GitHubModel(this);
        //18.10.
        mailModel = new MailModel();
        balloonPopup = new BalloonPopup();
        screenshotModel = new ScreenshotModel();

        studentRequestModel = new StudentRequestModel(project, this);

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

        this.project = project;
        System.out.println("Startklasse funktioniert!");

        try {
            gitModel.cloneRepo();
        } catch (Exception e) {
            System.out.println("failed cloning the repository");
        }

        //Das steht hier nur zu Testzwecken
        idCreator.createRequestID();
    }

    public void onNewAnswerData() {
        mailBoxScreen.refreshTable();
        System.out.println("onNewAnswerData");
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

    public void onSubmitRequestButtonPressed() {
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
                String title = gitHubModel.createIssueTitle(getStudentName());
                String label = Constants.COMMON_LABEL_BEGIN + SendRequestScreen.problemCategory;
                studentRequestModel.createAndPushBranch();
                gitHubModel.createIssue(title, requestMessage, label);
                mailModel.sendMailToTutors();

            } catch (IOException | GitAPIException e) {
                //TODO Fehlermeldung (sinnvoll)
                e.printStackTrace();
                sendRequestScreen.showErrorMessage(e.getMessage());
                return;
            }
            sendRequestScreen.showSentRequestInfo();
        }
    }

}
