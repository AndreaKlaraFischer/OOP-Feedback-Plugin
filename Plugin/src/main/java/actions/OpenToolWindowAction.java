package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;

public class OpenToolWindowAction extends AnAction {
    public OpenToolWindowAction() {
    }

    //https://intellij-support.jetbrains.com/hc/en-us/community/posts/360000121430-How-can-I-open-a-toolWindow-programmatically-
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        ToolWindowManager.getInstance(project).getToolWindow("Feedback Plugin").show(null);
    }
}
