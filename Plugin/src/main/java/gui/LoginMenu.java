package gui;

import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

//TODO: Das am besten noch über zwei verschiedene Klassen laufen lassen - package "login" - eine Klasse für die View und eine für die Funktionalitäten
//-- siehe "safety/LoginManager"
//TODO: Überlegen, ob ich das lieber über ein JFrame lösen soll -> JOptionPane hat zu viele Funktionalitäten eingebaut
public class LoginMenu implements ActionListener {
    private BalloonPopup balloonPopup;
    private Controller controller;
    public JPasswordField passwordFieldLogin;
    private JPasswordField passwordFieldFirstInput;
    private JPasswordField passwordFieldValidateInput;
    private String welcomeText;
    private JLabel introduction;
    private JButton registrationButton;
    public JOptionPane registrationOptionPane;
    public JDialog loginDialog;
    public JDialog registrationDialog;
    public JOptionPane loginOptionPane;
    private JFrame test;
    private JButton loginButton;
    private JButton goToLoginButton;

    public LoginMenu(Controller controller) {
        this.controller = controller;
        // Erstellung Array vom Datentyp Object, Hinzufügen der Komponenten
        introduction = new JLabel();
        passwordFieldLogin = new JPasswordField();
        passwordFieldFirstInput = new JPasswordField();
        passwordFieldValidateInput = new JPasswordField();
        registrationButton = new JButton();
        registrationButton.setText("Abschicken");
        registrationButton.addActionListener(this::actionPerformed);
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

    public void showLoginMenu() {
        String studentName = controller.getStudentNameInXML();
        welcomeText = "Willkommen " + studentName + "!";
        introduction.setText("Gib hier dein Passwort ein, um wieder Zugriff auf deine persönlichen Antworten zu haben");
        Object[] message = {welcomeText, introduction, passwordFieldLogin, loginButton};
        loginOptionPane = new JOptionPane(message);

        loginDialog = loginOptionPane.createDialog(null, "Willkommen zurück!");
        loginDialog.setVisible(true);
    }

    public void hideRegistrationMenu() {
        registrationDialog.dispose();
    }

    public void hideLoginMenu() {
        loginDialog.dispose();
    }

    //TODO: Wieso wird das nicht aufgerufen?? Vielleicht ist das das Problem, dass das n Char array ist
    public String getPasswordLogin() {
        System.out.println("getPasswordLogin" + Arrays.toString(passwordFieldLogin.getPassword()));
        return Arrays.toString(passwordFieldLogin.getPassword());
    }

    //Erstes Feld
    public String getPasswordFirstInput() {
        return Arrays.toString(passwordFieldFirstInput.getPassword());
    }

    //Zweites Feld --> Diese beiden müssen miteinander verglichen werden
    public String getPasswordValidateInput() {
        return Arrays.toString(passwordFieldValidateInput.getPassword());
    }

    public void showValidPasswordInfo() {
        balloonPopup.createBalloonPopup(loginOptionPane, Balloon.Position.above, "Passwort erfolgreich gespeichert", MessageType.INFO);
    }

    public void showLoginSuccessfulInfo() {
        balloonPopup.createBalloonPopup(loginOptionPane, Balloon.Position.above, "Passwort akzeptiert, Login erfolgreich", MessageType.ERROR);
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
            controller.onLoginButtonPressed();
            System.out.println("loginButtonPressed");
        } else if (clickedButton == registrationButton) {
            try {
                controller.onRegistrationButtonPressed();
                System.out.println("registrationButtonPressed");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
