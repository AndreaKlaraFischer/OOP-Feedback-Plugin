package gui;


import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static javax.swing.JOptionPane.showMessageDialog;

public class SettingScreen implements ActionListener {
    private JPanel settingScreenContent;
    public JTextField inputNameField;
    private JButton generateAnonymousNameButton;
    private JButton saveSettingsButton;
    public JTextField inputMailAddressField;
    public String studentMailInput;
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
        String anonymousName ="";
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
        if(inputNameField.getText().length() == 0) {
            showNoNameError();
        //TODO: hier noch ein RegEx für validen MailInput !! Nice to have aber sehr praktisch
        } else if (inputMailAddressField.getText().length() == 0) {
            //TODO: Das muss noch irgendwie wegklickbar sein oder so, dass man es auch nach der Warnung noch abschicken kann.
            showNoMailWarning();
        } else {
            try {
                controller.onSaveSettingsButtonPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void showNoMailWarning() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Wenn du keine Mailadresse angibst, bekommst du die Antwort des Tutors nicht zusätzlich per Mail zugeschickt.", MessageType.WARNING);
    }

    public void showNoNameError() {
        balloonPopup.createBalloonPopup(settingScreenContent, Balloon.Position.above, "Bitte einen Namen eingeben!.", MessageType.ERROR);
    }
}
