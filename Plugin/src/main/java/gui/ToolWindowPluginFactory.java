package gui;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.module.Module;
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

    //public ToolWindow toolWindow;
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Controller controller = null;
        //15.11.
        //this.toolWindow = toolWindow;
        try {
            controller = new Controller(project);
        } catch (IOException | TransformerException | SAXException | ParserConfigurationException | BadLocationException e) {
            e.printStackTrace();
        }

        //Hier wird geswitcht!
        //Entweder: Methoden ändern oder hier:

    //}



        TutorialScreen tutorialScreen = null;
        try {
            tutorialScreen = new TutorialScreen(controller);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        SendRequestScreen sendRequestScreen = new SendRequestScreen(controller, toolWindow, tutorialScreen);
        //14.10. Versuch, das darüber zu regeln
        JPanel answerDetailScreen = new JPanel();
        MailBoxScreen mailBoxScreen = new MailBoxScreen(controller, toolWindow);
        SettingScreen settingScreen = new SettingScreen(controller);

        Content contentMailBox = contentFactory.createContent(mailBoxScreen.getContent(), "Antworten", false);
        //Content contentAnswerDetail = contentFactory.createContent(answerScreen.getContent(),"Antwort des Tutors", false);
       // assert tutorialScreen != null;
        Content contentTutorial = contentFactory.createContent(tutorialScreen.getContent(),"Tutorial", false);
        Content contentRequest = contentFactory.createContent(sendRequestScreen.getContent(), "Tutor fragen", false);
        Content contentSettings = contentFactory.createContent(settingScreen.getContent(), "Einstellungen", false);


        toolWindow.getContentManager().addContent(contentRequest);
        toolWindow.getContentManager().addContent(contentTutorial);

        //26.10. - Das wird dann beim Start angezeigt
        toolWindow.getContentManager().setSelectedContent(contentRequest);
        //

        toolWindow.getContentManager().addContent(contentMailBox);
        toolWindow.getContentManager().addContent(contentSettings);


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
                //System.out.println("Content selection changed");
            }
        });

    }
}
