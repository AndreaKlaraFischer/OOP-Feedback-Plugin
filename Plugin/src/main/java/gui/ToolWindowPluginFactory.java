package gui;

import com.intellij.openapi.project.Project;
//importiert gleichzeitig auch Toolwindow und ToolwindowFactory
import com.intellij.openapi.wm.*;
//Importiert Contentmanager etc.
import com.intellij.ui.content.*;

public class ToolWindowPluginFactory implements ToolWindowFactory {
    // Tool window content gets created
    //https://github.com/JetBrains/intellij-sdk-docs/blob/master/code_samples/tool_window/src/myToolWindow/MyToolWindowFactory.java
    @Override
    public void createToolWindowContent(Project project, ToolWindow toolWindow) {
        ToolWindowPlugin toolWindowPlugin = new ToolWindowPlugin(toolWindow) ;
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(toolWindowPlugin.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
