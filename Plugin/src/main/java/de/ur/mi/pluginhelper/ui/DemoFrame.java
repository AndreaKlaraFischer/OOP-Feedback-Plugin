package de.ur.mi.pluginhelper.ui;

import javax.swing.*;

public class DemoFrame extends JFrame {

    private DemoFrame() {
        super();
        this.setSize(500,500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void showMessage() {
        DemoFrame frame = new DemoFrame();
        frame.setVisible(true);
    }
}
