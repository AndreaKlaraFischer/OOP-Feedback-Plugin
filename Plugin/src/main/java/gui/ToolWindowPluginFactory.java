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

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

public class ToolWindowPluginFactory implements ToolWindowFactory {
    // Tool window content gets created
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    private Content contentRequest;
    private Content contentTutorial;
    private Content contentSettings;
    private Content contentAssistance;
    private Content contentLogin;
    private Content contentMailBox;

    public ToolWindow toolWindow;
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Controller controller = null;
        //15.11.
        this.toolWindow = toolWindow;
        try {
            controller = new Controller(project, toolWindow);
            controller.toolWindowFactory = this;
        } catch (IOException | TransformerException | SAXException | ParserConfigurationException | BadLocationException e) {
            e.printStackTrace();
        }

        TutorialScreen tutorialScreen = null;
        try {
            tutorialScreen = new TutorialScreen(controller);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        SendRequestScreen sendRequestScreen = new SendRequestScreen(controller, toolWindow, tutorialScreen);
        MailBoxScreen mailBoxScreen = null;
        try {
            mailBoxScreen = new MailBoxScreen(controller, toolWindow);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SettingScreen settingScreen = new SettingScreen(controller);
        AssistanceScreen assistanceScreen = new AssistanceScreen(controller);
        LoginScreen loginScreen = new LoginScreen(controller);

        contentMailBox = contentFactory.createContent(mailBoxScreen.getContent(), "Antworten", false);
        //Content contentAnswerDetail = contentFactory.createContent(answerScreen.getContent(),"Antwort des Tutors", false);
       // assert tutorialScreen != null;
        contentTutorial = contentFactory.createContent(tutorialScreen.getContent(),"Tutorial", false);
        contentRequest = contentFactory.createContent(sendRequestScreen.getContent(), "Tutor fragen", false);
        System.out.println("Toolwindowpluginfactory, content Request wurde erstellt:  " + contentRequest);
        contentSettings = contentFactory.createContent(settingScreen.getContent(), "Einstellungen", false);
        //24.11.
        contentAssistance = contentFactory.createContent(assistanceScreen.getContent(), "Hilfestellung", false);
        //26.11.
        contentLogin = contentFactory.createContent(loginScreen.getContent(), "Login", false);

        //26.11. Neuer Test
        if(controller.isLoggedIn) {
            addDefaultContents();
            if(controller.isNewRegistered) {
                toolWindow.getContentManager().setSelectedContent(contentSettings);
                settingScreen.showWelcomeInfo(); //TODO: Das geht leider nicht
            }
        } else {
            //26.11. Plugin soll nicht benutzbar sein, wenn man nicht eingeloggt ist.
            toolWindow.getContentManager().addContent(contentLogin);
            toolWindow.getContentManager().addContent(contentTutorial);
            toolWindow.getContentManager().setSelectedContent(contentLogin);
        }

        //22.11.
        //Versuch, die tabs zu switchen
       //
        Controller finalController = controller;
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
                //TODO: Loggen
                finalController.logData("Tab gewechselt");
                toolWindow.getContentManager().getSelectedContent();
                finalController.logData("Aktueller Tab: " + toolWindow.getContentManager().getSelectedContent());
            }
        });

    }

    public void addDefaultContents() {
        toolWindow.getContentManager().addContent(contentRequest, 0);
        toolWindow.getContentManager().addContent(contentMailBox, 1);
        toolWindow.getContentManager().addContent(contentSettings, 2);
        toolWindow.getContentManager().addContent(contentAssistance, 3);
        toolWindow.getContentManager().addContent(contentTutorial, 4);
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
