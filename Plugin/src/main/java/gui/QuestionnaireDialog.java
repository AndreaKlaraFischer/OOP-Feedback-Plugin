package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuestionnaireDialog implements ActionListener {
    private JPanel contentPane;
    private JLabel introductionLabel;
    private JButton linkButton;
    private JDialog questionnaireDialog;
    private Controller controller;

    public QuestionnaireDialog(Controller controller) {
        this.controller = controller;
        questionnaireDialog = new JDialog();
        questionnaireDialog.setContentPane(contentPane);
        questionnaireDialog.setModal(true);
        linkButton.addActionListener(this);
        linkButton.setText("<HTML><U>Hier geht es zum Zwischenfragebogen</U></HTML>");
    }

    public void showQuestionnaireDialog() {
        questionnaireDialog.setSize(550,200);
       questionnaireDialog.setVisible(true);
    }

    public void hideQuestionnaireDialog() {
        questionnaireDialog.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("action performed!");
        controller.onSubmitQuestionnaireButtonPressed();
        hideQuestionnaireDialog();
    }
}
