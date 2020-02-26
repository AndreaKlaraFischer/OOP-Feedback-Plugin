package gui;

import controller.Controller;

import javax.swing.*;

public class AssistanceScreen {
    private Controller controller;
    private JPanel assistanceScreenContent;
    private JLabel title1;
    private JTextPane howTo;
    private JLabel title2;
    private JLabel subtitleNPE;
    private JTextPane eineNullPointerExceptionTrittDannTextPane;
    private JPanel container;

    public AssistanceScreen(Controller controller) {
        this.controller = controller;

    }

    public JPanel getContent() {
        return assistanceScreenContent;
    }
}
