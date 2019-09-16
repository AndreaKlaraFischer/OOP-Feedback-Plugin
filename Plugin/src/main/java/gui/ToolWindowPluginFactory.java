package gui;

import com.intellij.openapi.project.Project;
//importiert gleichzeitig auch Toolwindow und ToolwindowFactory
import com.intellij.openapi.wm.*;
//Importiert Contentmanager etc.
import com.intellij.ui.content.*;

public class ToolWindowPluginFactory implements ToolWindowFactory {
    // Tool window content gets created
    // TODO: mehrere Tabs!
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ToolWindowPlugin toolWindowPlugin = new ToolWindowPlugin(toolWindow) ;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content contentRequest = contentFactory.createContent(toolWindowPlugin.getContent(), "Tutor fragen", false);
        Content contentFAQ = contentFactory.createContent(toolWindowPlugin.getContent(), "FAQs", false);
        Content contentAnswers = contentFactory.createContent(toolWindowPlugin.getContent(), "Antworten", false);
        toolWindow.getContentManager().addContent(contentRequest);
        toolWindow.getContentManager().addContent(contentFAQ);
        toolWindow.getContentManager().addContent(contentAnswers);
    }
}
