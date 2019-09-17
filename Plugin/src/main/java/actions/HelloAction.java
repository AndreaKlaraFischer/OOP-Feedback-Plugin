package actions;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ui.JBUI;

public class HelloAction extends AnAction {
        public HelloAction() {
            super("Hello");
        }

        public void actionPerformed(AnActionEvent event) {
            Project project = event.getProject();
            Messages.showMessageDialog(project, "Liste mit allen erhaltenen Antworten", "Posteingang", Messages.getInformationIcon());
        }
    }

