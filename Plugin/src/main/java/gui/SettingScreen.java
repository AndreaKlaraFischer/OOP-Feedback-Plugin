package gui;


import actions.BalloonPopup;
import com.github.javafaker.Faker;
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
        generateAnonymousNameButton.addActionListener(this::generateAnonymousName);
        saveSettingsButton.addActionListener(this::actionPerformed);
        balloonPopup = new BalloonPopup();
        inputNameField.setText(controller.getStudentNameInXML());
        inputMailAddressField.setText(controller.getStudentMailInXML());
    }

    //TODO: Das auslagern noch, hat auch nichts in der View zu suchen
    //https://rieckpil.de/howto-generate-random-data-in-java-using-java-faker/
    private void generateAnonymousName(ActionEvent actionEvent) {
        String anonymousName = "";
        //TODO: Branchname muss dann natürlich auch angepasst werden, damit man dort nicht den Namen rausfinden kann
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        anonymousName = "Anonyme/r " + firstName + " " + lastName;
        System.out.println("anonymousName: " + anonymousName);
        inputNameField.setText(anonymousName);
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
