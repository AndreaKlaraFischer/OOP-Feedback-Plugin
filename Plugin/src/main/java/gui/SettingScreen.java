package gui;


import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.showMessageDialog;

public class SettingScreen implements ActionListener {
    private JPanel settingScreenContent;
    public JTextField inputNameField;
    private JButton generateAnonymousNameButton;
    private JButton saveSettingsButton;
    public JTextField inputMailAddressField;
    public static String studentNameInput;
    private Controller controller;
    private BalloonPopup balloonPopup;


    //07.10. --> Anmelden. Beim Start des Plugins StartScreen: Neuer Benutzer / Bestehender Benutzer
    // Kann man daheim wieder eingeben, wenn man es rechnerübergreifend benutzen möchte

    public SettingScreen(Controller controller) {
        controller.settingScreen = this;
        this.controller = controller;
        studentNameInput = inputNameField.getText(); //Brauche ich das überhaupt noch?
        //generateAnonymousNameButton.addActionListener(this::generateAnonymousName);
        saveSettingsButton.addActionListener(this::actionPerformed);
        balloonPopup = new BalloonPopup();
        inputNameField.setText(controller.getStudentNameInXML());
        inputMailAddressField.setText(controller.getStudentMailInXML());
    }

    //TODO: Florin fragen
    private String generateAnonymousName(ActionEvent actionEvent) {
        String anonymousName = "";
        /*TODO hier: Jeweils ein Array aus n beliebigen Vornamen und Nachnamen basteln,
        jeweils zufällig einen auswählen und in das Textfeld eintragen. Prio Low
        */
        //TODO: Falls das noch funktionieren sollte irgendwann, muss ich das auch noch abfangen
        return anonymousName;
    }


    public JPanel getContent() {
        return settingScreenContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inputNameField.getText().length() == 0) {
            showNoNameError();
        } else if (inputMailAddressField.getText().length() == 0) {
            //TODO: Das muss noch irgendwie wegklickbar sein oder so, dass man es auch nach der Warnung noch abschicken kann.
            showNoMailWarning();
        } else {
            if (validateMail(inputMailAddressField.getText())) {
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
    }


    //https://stackoverflow.com/questions/8204680/java-regex-email
    public boolean validateMail(String mailAddressInput) {
        String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(mailAddressInput);
        return emailMatcher.find();
    }

    private void showSuccessfullySavedInfo() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Erfolgreich gespeichert.", MessageType.INFO);
    }

    private void showInvalidMailError() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Ungültige Email-Adresse!", MessageType.ERROR);
    }

    private void showNoMailWarning() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Wenn du keine Mailadresse angibst, bekommst du die Antwort des Tutors nicht zusätzlich per Mail zugeschickt.", MessageType.WARNING);
    }

    private void showNoNameError() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Bitte einen Namen eingeben!.", MessageType.ERROR);
    }
}
