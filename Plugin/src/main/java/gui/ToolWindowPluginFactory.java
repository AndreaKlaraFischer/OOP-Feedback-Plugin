package gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import controller.Controller;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.text.ParseException;

public class ToolWindowPluginFactory implements ToolWindowFactory {
    // Tool window content gets created
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    private Content contentRequest;
    private Content contentTutorial;
    private Content contentSettings;
    private Content contentAssistance;
    private Content contentLogin;
    private Content contentMailBox;
    private Content contentAnswerDetail;
    private ToolWindow toolWindow;

    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Controller controller = null;
        this.toolWindow = toolWindow;
        try {
            controller = new Controller(project, toolWindow);
            controller.toolWindowFactory = this;
        } catch (IOException | TransformerException | SAXException | ParserConfigurationException | BadLocationException e) {
            e.printStackTrace();
        }

        TutorialScreen tutorialScreen = new TutorialScreen();

        //MailBoxScreen mailBoxScreen = null;
        //mailBoxScreen = new MailBoxScreen(controller, toolWindow);
        MailBoxScreen mailBoxScreen = new MailBoxScreen(controller);
        SettingScreen settingScreen = null;
        try {
            settingScreen = new SettingScreen(controller);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        AssistanceScreen assistanceScreen = new AssistanceScreen(controller);
        SendRequestScreen sendRequestScreen = new SendRequestScreen(controller, toolWindow, assistanceScreen, settingScreen);
        LoginScreen loginScreen = new LoginScreen(controller);
        AnswerDetailScreen answerDetailScreen = new AnswerDetailScreen(controller);

        contentRequest = contentFactory.createContent(sendRequestScreen.getContent(), "Hilfe Anfragen", false);
        contentMailBox = contentFactory.createContent(mailBoxScreen.getContent(), "Antworten", false);
        assert settingScreen != null;
        contentSettings = contentFactory.createContent(settingScreen.getContent(), "Einstellungen", false);
        contentAssistance = contentFactory.createContent(assistanceScreen.getContent(), "Hilfestellung", false);
        contentTutorial = contentFactory.createContent(tutorialScreen.getContent(), "Tutorial", false);
        contentLogin = contentFactory.createContent(loginScreen.getContent(), "Login", false);
        contentAnswerDetail = contentFactory.createContent(answerDetailScreen.getContent(), "Detailansicht", false);

        //Angezeigte Inhalte werden hier gemanaged
        if (controller.isLoggedIn) {
            addDefaultContents();
            if (controller.isNewRegistered) {
                toolWindow.getContentManager().setSelectedContent(contentSettings);
                settingScreen.showWelcomeInfo();
            }
        } else {
            //Plugin soll nicht benutzbar sein, wenn man nicht eingeloggt ist.
            addNotLoggedInContents();
        }

        Controller finalController = controller;
        Controller finalController1 = controller;
        toolWindow.getComponent().addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                finalController1.logData("ToolWindow geöffnet");
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                finalController.logData("ToolWindow geschlossen");
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                // finalController.logData("ToolWindow verschoben");
            }
        });

        toolWindow.getContentManager().addContentManagerListener(new ContentManagerListener() {
            @Override
            public void contentAdded(@NotNull ContentManagerEvent event) {

            }

            @Override
            public void contentRemoved(@NotNull ContentManagerEvent event) {

            }

            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent event) {

            }

            @Override
            public void selectionChanged(@NotNull ContentManagerEvent event) {
                finalController.logData("Tab gewechselt");
                toolWindow.getContentManager().getSelectedContent();
                finalController.logData("Aktueller Tab: " + toolWindow.getContentManager().getSelectedContent());
            }
        });

    }

    private void addNotLoggedInContents() {
        toolWindow.getContentManager().addContent(contentLogin, 0);
        toolWindow.getContentManager().addContent(contentTutorial, 1);
        toolWindow.getContentManager().setSelectedContent(contentLogin);
    }

    public void addDefaultContents() {
        toolWindow.getContentManager().addContent(contentRequest, 0);
        toolWindow.getContentManager().addContent(contentMailBox, 1);
        toolWindow.getContentManager().addContent(contentSettings, 2);
        toolWindow.getContentManager().addContent(contentAssistance, 3);
        toolWindow.getContentManager().addContent(contentTutorial, 4);
    }

    public void addAnswerDetailContent() {
        toolWindow.getContentManager().removeContent(contentMailBox, false);
        toolWindow.getContentManager().addContent(contentAnswerDetail, 1);
        toolWindow.getContentManager().setSelectedContent(contentAnswerDetail);
    }

    public void navigateBackToMailBoxScreen() {
        toolWindow.getContentManager().removeContent(contentAnswerDetail, false);
        toolWindow.getContentManager().addContent(contentMailBox, 1);
        toolWindow.getContentManager().setSelectedContent(contentMailBox);
    }

    public Content getContentRequest() {
        return contentRequest;
    }

    public Content getContentTutorial() {
        return contentTutorial;
    }

    public Content getContentSettings() {
        return contentSettings;
    }

    public Content getContentAssistance() {
        return contentAssistance;
    }

    public Content getContentMailBox() {
        return contentMailBox;
    }

    public Content getContentLogin() {
        return contentLogin;
    }

}
