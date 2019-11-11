package gui;


import actions.BalloonPopup;
import android.os.SystemPropertiesProto;
import com.github.javafaker.Faker;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        generateAnonymousNameButton.addActionListener(this);
        saveSettingsButton.addActionListener(this);
        balloonPopup = new BalloonPopup();
        inputNameField.setText(controller.getStudentNameInXML());
        inputMailAddressField.setText(controller.getStudentMailInXML());
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
                //TODO: Das muss noch irgendwie wegklickbar sein oder so, dass man es auch nach der Warnung noch abschicken kann.
                showNoMailWarning();
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
        }
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
