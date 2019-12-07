package gui;

import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
        linkButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
        try {
            controller.onSubmitQuestionnaireButtonPressed();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        hideQuestionnaireDialog();
    }
}
