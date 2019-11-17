package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;

public class OpenToolWindowAction extends AnAction {
    public OpenToolWindowAction() {
    }

    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        ToolWindowManager.getInstance(project).getToolWindow("Feedback Plugin").show(null);
    }
}
