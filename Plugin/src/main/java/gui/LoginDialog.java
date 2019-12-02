package gui;


import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

public class LoginDialog implements ActionListener {
    private JDialog loginDialog;
    private BalloonPopup balloonPopup;
    private JPanel contentPane;
    private JButton loginButton;
    public JPasswordField passwordFieldLogin;
    private JLabel welcomeLabel;
    private Controller controller;

    public LoginDialog(Controller controller) {
        this.controller = controller;
        balloonPopup = new BalloonPopup();
        loginDialog = new JDialog();
        loginDialog.setContentPane(contentPane);
        loginDialog.setModal(true);
        loginButton.addActionListener(this);
    }

    public void showLoginDialog() {
        String studentName = controller.getStudentNameInXML();
        welcomeLabel.setText("Willkommen, " + studentName + "!");
        //Mit Enter einloggbar
        loginDialog.getRootPane().setDefaultButton(loginButton);
        loginDialog.setSize(500,300);
        loginDialog.setVisible(true);
    }

    public void showWrongPasswordError() {
        balloonPopup.createBalloonPopup(contentPane, Balloon.Position.above, "Passwort ist falsch! Probiere es nochmal.", MessageType.ERROR);
    }

    public void showEmptyPasswordError() {
        balloonPopup.createBalloonPopup(contentPane, Balloon.Position.above, "Bitte ein Passwort eingeben!", MessageType.ERROR);
    }

    public void hideLoginMenu() {
        loginDialog.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.onLoginButtonPressed();
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
    }
}
