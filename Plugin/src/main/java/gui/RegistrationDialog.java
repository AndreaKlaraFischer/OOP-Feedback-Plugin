package gui;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistrationDialog  implements ActionListener {
    private BalloonPopup balloonPopup;
    private JPanel contentPane;
    private JButton registrationButton;
    public JPasswordField passwordFieldFirstInput;
    public JPasswordField passwordFieldValidateInput;
    private JLabel introductionText;
    private Controller controller;
    private JDialog registrationDialog;

    public RegistrationDialog(Controller controller) {
        this.controller = controller;
        registrationDialog = new JDialog();
        balloonPopup = new BalloonPopup();
        registrationDialog.setContentPane(contentPane);
        registrationDialog.setModal(true);
        registrationButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registrationButton.addActionListener(this);
    }

    public void showRegistrationMenu() {
        registrationDialog.setSize(700,300);
        registrationDialog.setVisible(true);
    }


    public void hideRegistrationMenu() {
        registrationDialog.dispose();
    }

    public void showPasswordTooShortError() {
        balloonPopup.createBalloonPopup(contentPane, Balloon.Position.above, "Passwort muss mindestens " + Constants.MINIMUM_PASSWORD_LENGTH + " Zeichen sein!", MessageType.ERROR);
    }

    public void showPasswordsNotEqualError() {
        balloonPopup.createBalloonPopup(contentPane, Balloon.Position.above, "Eingaben nicht identisch!", MessageType.ERROR);
    }

    public void showValidPasswordInfo() {
        balloonPopup.createBalloonPopup(contentPane, Balloon.Position.above, "Passwort erfolgreich gespeichert, Willkommen!", MessageType.INFO);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.onRegistrationButtonPressed();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
