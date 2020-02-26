package gui;


import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import static javax.swing.JOptionPane.showMessageDialog;

public class SettingScreen implements ActionListener {
    private JPanel settingScreenContent;
    private JLabel subtitleName;
    private JTextField textField1;
    private JButton einstellungenSpeichernButton;
    private JTextField textField2;
    public JTextField inputNameField;
    private JButton generateAnonymousNameButton;
    private JButton saveSettingsButton;
    public JTextField inputMailAddressField;
    private JTextPane gibHierBitteEineTextPane;
    private JButton toQuestionnaireButton;
    private JPanel toQuestionnairePanel;
    private JPanel Container;
    private JPanel nameInputPanel;
    private JPanel mailInputPanel;
    private JPanel settingPanel;
    public static String studentNameInput;
    private Controller controller;
    private BalloonPopup balloonPopup;

    public SettingScreen(Controller controller) throws ParseException {
        controller.settingScreen = this;
        this.controller = controller;
        studentNameInput = inputNameField.getText(); //Brauche ich das überhaupt noch?
        generateAnonymousNameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        generateAnonymousNameButton.addActionListener(this);
        saveSettingsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveSettingsButton.addActionListener(this);
        toQuestionnaireButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toQuestionnaireButton.addActionListener(this);

        balloonPopup = new BalloonPopup();
        inputNameField.setText(controller.getStudentNameInXML());
        inputMailAddressField.setText(controller.getStudentMailInXML());


        if(controller.isNewRegistered) {
            showWelcomeInfo();
        }
        updateToQuestionnaireButton();
    }

    public void updateToQuestionnaireButton() throws ParseException {
        if(controller.questionnaireTimeCalculator.calculateQuestionnaireDelta()) {
           toQuestionnairePanel.setVisible(true);
        }

    }


    public JPanel getContent() {
        return settingScreenContent;
    }

    private Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object clickedButton = getClickedButton(e);
        if(clickedButton == generateAnonymousNameButton) {
            controller.nameGenerator.generateAnonymousName();
        } else if(clickedButton == saveSettingsButton) {
            if (inputNameField.getText().length() == 0) {
                showNoNameError();
            } else if (inputMailAddressField.getText().length() == 0) {
                showNoMailError();
            } else {
                if (controller.loginManager.validateMail(inputMailAddressField.getText())) {
                    try {
                        controller.onSaveSettingsButtonPressed();
                        showSuccessfullySavedInfo();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    showInvalidMailError();
                }
            }
        } else if(clickedButton == toQuestionnaireButton) {
            try {
                controller.onSubmitQuestionnaireButtonPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            toQuestionnairePanel.setVisible(false);
        }
    }
    public void showWelcomeInfo() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Willkommen!", MessageType.INFO);
    }

    private void showSuccessfullySavedInfo() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Erfolgreich gespeichert.", MessageType.INFO);
    }

    private void showInvalidMailError() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Ungueltige Email-Adresse!", MessageType.ERROR);
    }

    private void showNoMailError() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Bitte eine gueltige Mailadresse angeben!", MessageType.ERROR);
    }

    private void showNoNameError() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Bitte einen Namen eingeben!.", MessageType.ERROR);
    }
}
