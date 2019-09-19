package gui;

import com.intellij.openapi.project.Project;
//importiert gleichzeitig auch Toolwindow und ToolwindowFactory
import com.intellij.openapi.wm.*;
//Importiert Contentmanager etc.
import com.intellij.ui.content.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ToolWindowPluginFactory implements ToolWindowFactory {
    // Tool window content gets created
    // TODO: mehrere Tabs!
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ToolWindowPlugin toolWindowPlugin = new ToolWindowPlugin(toolWindow) ;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        FAQScreen faqScreen = new FAQScreen();
        Content contentRequest = contentFactory.createContent(toolWindowPlugin.getContent(), "Tutor fragen", false);
        Content contentFAQ = contentFactory.createContent(faqScreen.getContent(), "FAQs", false);
        Content contentAnswers = contentFactory.createContent(toolWindowPlugin.getContent(), "Antworten", false);
        toolWindow.getContentManager().addContent(contentRequest);
        toolWindow.getContentManager().addContent(contentFAQ);
        toolWindow.getContentManager().addContent(contentAnswers);
        toolWindow.getContentManager().addContentManagerListener(new ContentManagerListener() {
            @Override
            public void contentAdded(@NotNull ContentManagerEvent contentManagerEvent) {

            }

            @Override
            public void contentRemoved(@NotNull ContentManagerEvent contentManagerEvent) {

            }

            @Override
            public void contentRemoveQuery(@NotNull ContentManagerEvent contentManagerEvent) {

            }

            @Override
            public void selectionChanged(@NotNull ContentManagerEvent contentManagerEvent) {
                System.out.println("Content Selection Changed");
            }
        });
    }




}
