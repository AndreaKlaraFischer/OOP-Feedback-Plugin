package gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManagerEvent;
import com.intellij.ui.content.ContentManagerListener;
import org.jetbrains.annotations.NotNull;

public class ToolWindowPluginFactory implements ToolWindowFactory {
    // Tool window content gets created
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //TODO: Hier wird geswitcht!
        //Entweder: Methoden ändern oder hier:
        SendRequestScreen sendRequestScreen = new SendRequestScreen(project);
        TutorialScreen tutorialScreen = new TutorialScreen();
        AnswerDetailScreen answerScreen = new AnswerDetailScreen();
        MailBoxScreen mailBoxScreen = new MailBoxScreen();

        Content contentMailBox = contentFactory.createContent(mailBoxScreen.getContent(),"Antworten", false);
        //Content contentAnswerDetail = contentFactory.createContent(answerScreen.getContent(),"Antwort des Tutors", false);
        Content contentTutorial = contentFactory.createContent(tutorialScreen.getContent(),"Tutorial", false);
        Content contentRequest = contentFactory.createContent(sendRequestScreen.getContent(), "Tutor fragen", false);

       //TODO: Hier noch die Namen anpassen!
        toolWindow.getContentManager().addContent(contentRequest);
        toolWindow.getContentManager().addContent(contentTutorial);
        toolWindow.getContentManager().addContent(contentMailBox);
        //contentAnswerDetail soll nur angezeigt werden, wenn auf ein Listen Item gedrückt wurde!Kein eigener Tab
        //toolWindow.getContentManager().addContent(contentAnswerDetail);

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
                System.out.println("Content selection changed");
            }
        });

    }


}
