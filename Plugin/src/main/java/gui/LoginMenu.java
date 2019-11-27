package gui;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

//-- siehe "safety/LoginManager"
//TODO: Überlegen, ob ich das lieber über ein JFrame lösen soll -> JOptionPane hat zu viele Funktionalitäten eingebaut
public class LoginMenu implements ActionListener {
    private BalloonPopup balloonPopup;
    private Controller controller;
    public JPasswordField passwordFieldLogin;
    public JPasswordField passwordFieldFirstInput;
    public JPasswordField passwordFieldValidateInput;
    private String welcomeText;
    private JLabel introduction;
    private JButton registrationButton;
    private JOptionPane registrationOptionPane;
    private JDialog loginDialog;
    private JDialog registrationDialog;
    private JOptionPane loginOptionPane;
    private JButton loginButton;

    public LoginMenu(Controller controller) {
        this.controller = controller;
        // Erstellung Array vom Datentyp Object, Hinzufügen der Komponenten
        introduction = new JLabel();
        passwordFieldLogin = new JPasswordField();
        passwordFieldFirstInput = new JPasswordField();
        passwordFieldValidateInput = new JPasswordField();
        registrationButton = new JButton();
        registrationButton.setText("Abschicken");
        registrationButton.addActionListener(this);
        balloonPopup = new BalloonPopup();
        loginOptionPane = new JOptionPane();
        loginButton = new JButton();
        loginButton.setText("Einloggen");
        loginButton.addActionListener(this);
        registrationDialog = new JDialog();
        loginDialog = new JDialog();
    }

    public void showRegistrationMenu() {
        //Idee: Wenn das funktioniert, dann aufdröseln in create (Constructor) und show (controller)
        welcomeText = "Turbi";
        introduction.setText(Constants.REGISTRATION_WELCOME_TEXT);
        //Das mit dem Object muss ich noch anders lösen, solange das dabei ist, kann das glaube ich nie ohne Button sein!
        Object[] message = {welcomeText, introduction, passwordFieldFirstInput, passwordFieldValidateInput, registrationButton};
        registrationOptionPane = new JOptionPane(message);

        registrationDialog = registrationOptionPane.createDialog("Willkommen");
        registrationDialog.setVisible(true);
    }

    //https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/DialogDemoProject/src/components/CustomDialog.java
    public void showLoginMenu() throws UnsupportedEncodingException {
        String studentName = controller.getStudentNameInXML();
        welcomeText = "Willkommen " + studentName + "!" + "\n" + "Gib hier dein Passwort ein, um wieder Zugriff auf deine persönlichen Antworten zu haben";

        //TODO: UTF-8
        introduction.setText(welcomeText);
        Object[] message = {introduction, passwordFieldLogin, loginButton};
        JOptionPane loginOptionPane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE);

        loginDialog = loginOptionPane.createDialog(null, "Willkommen zurück!");
        loginDialog.setVisible(true);

       /* JPanel panel = new JPanel();
        panel.add(introduction);
        panel.add(passwordFieldLogin);
        panel.add(loginButton);
        loginDialog.add(panel);
        loginDialog.setVisible(true);*/

    }

    public void hideRegistrationMenu() {
        registrationDialog.dispose();
    }

    public void hideLoginMenu() {
        loginDialog.dispose();
    }

    public void showValidPasswordInfo() {
        balloonPopup.createBalloonPopup(registrationOptionPane, Balloon.Position.above, "Passwort erfolgreich gespeichert, Willkommen!", MessageType.INFO);
    }

    public void showLoginSuccessfulInfo() {
        balloonPopup.createBalloonPopup(loginOptionPane, Balloon.Position.above, "Login erfolgreich, Willkommen zurück", MessageType.ERROR);
    }

    public void showPasswordTooShortError() {
        balloonPopup.createBalloonPopup(registrationOptionPane, Balloon.Position.above, "Passwort muss mindestens 8 Zeichen sein!", MessageType.ERROR);
    }

    public void showPasswordsNotEqualError() {
        balloonPopup.createBalloonPopup(registrationOptionPane, Balloon.Position.above, "Passwörter stimmen nicht überein!", MessageType.ERROR);
    }

    public void showWrongPasswordError() {
        balloonPopup.createBalloonPopup(loginOptionPane, Balloon.Position.above, "Passwort ist falsch! Probiere es nochmal.", MessageType.ERROR);
    }

    private Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object clickedButton = getClickedButton(e);
        if (clickedButton == loginButton) {
            try {
                controller.onLoginButtonPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (clickedButton == registrationButton) {
            try {
                controller.onRegistrationButtonPressed();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
