package gui;

import controller.Controller;

import javax.swing.*;

public class AssistanceScreen {
    private Controller controller;
    private JPanel assistanceScreenContent;

    public AssistanceScreen(Controller controller) {
        this.controller = controller;

    }

    public JPanel getContent() {
        return assistanceScreenContent;
    }
}
