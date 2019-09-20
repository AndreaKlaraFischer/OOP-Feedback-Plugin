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
    // TODO: mehrere Tabs!
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ToolWindowPlugin toolWindowPlugin = new ToolWindowPlugin(toolWindow) ;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        //TODO: Hier wird geswitcht!
        //Entweder: Methoden ändern oder hier:

        TutorialScreen tutorialScreen = new TutorialScreen();
        AnswerDetailScreen answerScreen = new AnswerDetailScreen();
        MailBoxScreen mailBoxScreen = new MailBoxScreen();
        //Content contentMailBox = contentFactory.createContent(mailBoxScreen.getContent());
        Content contentAnswerDetail = contentFactory.createContent(answerScreen.getContent(),"Antworten", false);
        Content contentTutorial = contentFactory.createContent(tutorialScreen.getContent(),"FAQs", false);
        Content contentRequest = contentFactory.createContent(toolWindowPlugin.getContent(), "Tutor fragen", false);
        //Content contentFAQ = contentFactory.createContent(toolWindowPlugin.getContent(), "FAQs", false);
        //Content contentAnswers = contentFactory.createContent(toolWindowPlugin.getContent(), "Antworten", false);
       //TODO: Hier noch die Namen anpassen!
        toolWindow.getContentManager().addContent(contentRequest);
        toolWindow.getContentManager().addContent(contentTutorial);
        toolWindow.getContentManager().addContent(contentAnswerDetail);
        //contentAnswerDetail soll nur angezeigt werden, wenn auf ein Listen Item gedrückt wurde!Kein eigener Tab
        toolWindow.getContentManager().addContent(contentAnswerDetail);

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



    //TODO: Herausfinden, welcher Tab angeklickt wurde! Wo passiert das?
    //Contentmanager Listener? --> siehe Alex' eigener Branch

}
