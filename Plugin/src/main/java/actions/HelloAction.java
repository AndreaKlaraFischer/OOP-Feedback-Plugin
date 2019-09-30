package actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;

public class HelloAction extends AnAction {
        public HelloAction() {
            super("Hello");
        }

        public void actionPerformed(AnActionEvent event) {
            Project project = event.getProject();
            //ToolWindow öffnen? An sich macht das ja keinen Sinn hier
            Messages.showMessageDialog(project, "Liste mit allen erhaltenen Antworten", "Posteingang", Messages.getInformationIcon());
        }
    }

