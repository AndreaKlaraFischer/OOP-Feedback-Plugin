package gui;


import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class SettingScreen implements ActionListener {
    private JPanel settingScreenContent;
    private JTextField inputNameField;
    private JButton generateAnonymousNameButton;
    private JButton saveSettingsButton;
    public static String studentNameInput;
    private Controller controller;

    //07.10. --> Anmelden. Beim Start des Plugins StartScreen: Neuer Benutzer / Bestehender Benutzer --> ID generieren (leicht merkmar)
    // Kann man daheim wieder eingeben, wenn man es rechnerübergreifend benutzen möchte

    public SettingScreen(Controller controller) {
        this.controller = controller;
        studentNameInput = inputNameField.getText();
        //generateAnonymousNameButton.addActionListener(this::generateAnonymousName);
        saveSettingsButton.addActionListener(this::actionPerformed);
    }

    private String generateAnonymousName(ActionEvent actionEvent) {
        String anonymousName ="";
        /*TODO hier: Jeweils ein Array aus n beliebigen Vornamen und Nachnamen basteln,
        jeweils zufällig einen auswählen und in das Textfeld eintragen. Prio Low
        */
        return anonymousName;
    }

    public JPanel getContent() {
        return settingScreenContent;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        studentNameInput = inputNameField.getText();
        if(studentNameInput.length() == 0) {
            showMessageDialog(null, "Feld darf nicht leer sein!");
        } else {
            System.out.println(studentNameInput);
        }
    }
}
