package gui;

import com.intellij.openapi.wm.ToolWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ToolWindowPlugin {
    private JButton testButton;
    private JPanel toolWindowContent;

    public ToolWindowPlugin(ToolWindow toolwindow) {
        testButton = new JButton("Test");

    }

    public JPanel getContent() {
        return toolWindowContent;
    }

}
