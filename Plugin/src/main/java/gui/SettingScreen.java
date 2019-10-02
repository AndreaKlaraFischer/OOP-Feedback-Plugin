package gui;




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


    public SettingScreen() {
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

    /*public void getCategoryName(ActionEvent e) {
        studentNameInput = inputNameField.getText();
        if(studentNameInput != null) {
            System.out.println(studentNameInput);
        }
    }*/

    @Override
    public void actionPerformed(ActionEvent e) {
        studentNameInput = inputNameField.getText();
        if(studentNameInput.length() == 0) {
            //TODO: Wenn das genau das gleiche ist wie in der SendRequestKlasse, dann refactoren
            showMessageDialog(null, "Feld darf nicht leer sein!");
        } else {
            System.out.println(studentNameInput);
        }
    }
}
