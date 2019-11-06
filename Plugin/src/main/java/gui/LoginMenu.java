package gui;

import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

//TODO: Das am besten noch über zwei verschiedene Klassen laufen lassen - package "login" - eine Klasse für die View und eine für die Funktionalitäten
//-- siehe "safety/LoginManager"
//TODO: Überlegen, ob ich das lieber über ein JFrame lösen soll -> JOptionPane hat zu viele Funktionalitäten eingebaut
public class LoginMenu implements ActionListener {
    private BalloonPopup balloonPopup;
    private Controller controller;
    private boolean isInitialized = false;
    private JPasswordField passwordFieldLogin;
    private JPasswordField passwordFieldFirstInput;
    private JPasswordField passwordFieldValidateInput;
    //TODO: Beide Möglichkeiten in Konstanten speichern
    private String welcomeText;
    private JLabel introduction;
    private JButton registrationButton;
    private JOptionPane registrationDialog;
    private JOptionPane loginDialog;
    private JFrame test;
    private JButton loginButton;
    private JButton gotToLoginButton;

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
        loginDialog = new JOptionPane();
        loginButton = new JButton();
        loginButton.setText("Einloggen");
        loginButton.addActionListener(this);
        gotToLoginButton = new JButton();
        gotToLoginButton.setText("Ich habe bereits ein Passwort und möchte mich einloggen");
        gotToLoginButton.addActionListener(this::actionPerformed);
    }


    //TODO: Neue Idee: Ein JOptionPane, wo steht: Willkommen zum Feedback-Tool, bist du bereits registriert,...?
    //--> Dann die anderen Optionen sichtbar machen
    //Wenn der State schon true gesetzt ist, dann das login

    public void showRegistrationMenu() {
        //TODO: Hier noch eine Art Link einbauen oder Button: Ich bin bereits registriert--> show login
        welcomeText = "Turbi";
        introduction.setText("Wähle hier ein Passwort, um deine persönlichen Daten zu haben. Mindestens 8 Zeichen");
        Object[] message = {welcomeText, introduction, passwordFieldFirstInput, passwordFieldValidateInput, registrationButton, gotToLoginButton};
        registrationDialog = new JOptionPane(message);
        registrationDialog.createDialog("Willkommen").setVisible(true);
    }

    public void showLoginMenu() {
        //TODO: Hier noch den gespeicherten Studentennamen
        welcomeText = "Schwurbi";
        introduction.setText("Gib hier dein Passwort ein, um wieder Zugriff auf deine persönlichen Antworten zu haben");
        Object[] message = {welcomeText, introduction, passwordFieldLogin, loginButton};
        loginDialog = new JOptionPane(message);
        loginDialog.createDialog(null, "Willkommen zurück!").setVisible(true);
        registrationDialog.setVisible(false);
    }

    //Das ist beim login wichtig
    public char[] getPasswordLogin() {
        return passwordFieldLogin.getPassword();
    }

    //Erstes Feld
    public char[] getPasswordFirstInput() {
        return passwordFieldFirstInput.getPassword();
    }
    //Zeites Feld --> Diese beiden müssen miteinander verglichen werden
    public char[] getPasswordValidateInput() {
        return passwordFieldValidateInput.getPassword();
    }

    public void showValidPasswordInfo() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort erfolgreich gespeichert", MessageType.INFO);
    }
    //TODO: Das sollte wahrscheinlich im SendRequestScreen stehen!
    public void showWelcomeBackInfo() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Willkommen zurück", MessageType.INFO);
    }

    public void showLoginSuccessfulInfo() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort akzeptiert, Login erfolgreich", MessageType.ERROR);
    }

    public void showPasswordTooShortError() {
        balloonPopup.createBalloonPopup(registrationDialog, Balloon.Position.above, "Passwort muss mindestens 8 Zeichen sein!", MessageType.ERROR);
    }

    public void showPasswordsNotEqualError() {
        balloonPopup.createBalloonPopup(registrationDialog, Balloon.Position.above, "Passwörter stimmen nicht überein!", MessageType.ERROR);
    }

    public void showWrongPasswordError() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort ungültig! Probiere es nochmal.", MessageType.ERROR);
    }

    public Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object clickedButton = getClickedButton(e);
        if(clickedButton == loginButton) {
            controller.onLoginButtonPressed();
            loginDialog.setVisible(false);
            System.out.println("loginButtonPressed");
        } else if(clickedButton == registrationButton) {
            try {
                controller.onRegistrationButtonPressed();
                System.out.println("registrationButtonPressed");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if(clickedButton == gotToLoginButton) {
            showLoginMenu();
        }
    }
}
