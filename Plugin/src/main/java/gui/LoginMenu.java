package gui;

import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

//TODO: Das am besten noch über zwei verschiedene Klassen laufen lassen - package "login" - eine Klasse für die View und eine für die Funktionalitäten
//-- siehe "safety/LoginManager"
//TODO: Überlegen, ob ich das lieber über ein JFrame lösen soll -> JOptionPane hat zu viele Funktionalitäten eingebaut
public class LoginMenu implements ActionListener {
    private BalloonPopup balloonPopup;
    private Controller controller;
    private boolean isInitialized = false;
    private JPasswordField passwordField;
    //TODO: Beide Möglichkeiten in Konstanten speichern
    private String welcomeText;
    JLabel introduction;
    JButton submitButton;
    private JOptionPane loginDialog;
    private JFrame test;

    public LoginMenu(Controller controller) {
        this.controller = controller;
        // Erstellung Array vom Datentyp Object, Hinzufügen der Komponenten
        introduction = new JLabel();
        passwordField = new JPasswordField();
        submitButton = new JButton();
        submitButton.setText("Abschicken");
        submitButton.addActionListener(this::actionPerformed);
        balloonPopup = new BalloonPopup();
        loginDialog = new JOptionPane();
    }

    //TODO: Das im Controller oder in der ToolWindowPluginFactory aufrufen! (aber erstmal in einem testbutton)
    public void showDialog2() throws IOException {
        //Beim allerersten Programmstart soll ein anderer Text angezeigt werden als beim normalen Programmstart
        //Das über die Configfile abfragen

        checkIfIsInitialized();
        if(!isInitialized) {
            welcomeText = "Um deine Daten zu schützen, gebe hier ein Passwort ein (mindestens 8 Zeichen)";
        } else {
            welcomeText = "Willkommen zurück! Gib hier dein Passwort ein, um Zugriff zu deinen bisherigen Antworten zu haben.";
        }

        Object[] message = {welcomeText, introduction, passwordField, submitButton};
        //TODO: Hier noch mit Buttons und ActionListenern arbeiten, sonst geht hier gar nichts
        //--> Das dann auch wieder über den Controller laufen lassen
        loginDialog = new JOptionPane( message,
                JOptionPane.PLAIN_MESSAGE);
        loginDialog.createDialog(null, "Willkommen zum Feedback Plugin!").setVisible(true);

        System.out.println("Eingabe: " + passwordField.getText());
    }

    public void showDialog() {
        test = new JFrame();
        JPanel panel = new JPanel();
        panel.add(introduction);
        panel.add(passwordField);
        panel.add(submitButton);
        test.add(panel);
        test.setTitle("Willkommen");
        test.setVisible(true);
    }

    //NPE
    public char[] getPassword() {
        return passwordField.getPassword();
    }

    private void checkIfIsInitialized() throws IOException {
        //Idee: Mit Strings vergleichen, also ob die workspace (die davor auslesen) diesen String enthält
        //TODO: Das ist hier kriegsentscheidend
        //Ich schau mir das an, wenn da noch kein Passwortgedöns drin ist,
        // dann wird isInitialized auf false gesetzt. Wenn isIn falls ist, dann soll initialisiert werden
        //if(isInitialized == false) {
        initialize();
        //} else {
        //
        //}
    }

    private void initialize() throws IOException {
        generateToken();
        saveTokenInConfigFile();
        savePasswordInConfigFile();
        isInitialized = true;
    }

    //TODO: Das Passwort muss ich noch woanders übergeben
    private void savePasswordInConfigFile() throws IOException {
        char [] password = getPassword();
        if (validatePassword(password)) {
            controller.loginManager.encryptPassword();
            controller.readXMLFile.readAndModifyXML();
            //Do stuff here - Hier das in die workspace schreiben
        }

    }



    private void showValidPasswordInfo() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort erfolgreich gespeichert", MessageType.INFO);
    }

    public void showLoginSuccessfulInfo() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort akzeptiert, Login erfolgreich", MessageType.ERROR);
    }

    public void showInvalidPasswordError() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort muss länger als 8 Zeichen sein!", MessageType.ERROR);
    }

    public void showWrongPasswordError() {
        balloonPopup.createBalloonPopup(loginDialog, Balloon.Position.above, "Passwort ungültig!", MessageType.ERROR);
    }


    //TODO: Das soll alles über einen ActionListener laufen und dann wieder über den Controller:
    //if !isInitialized - dann alles speichern und so ein Gedöns
    //Wenn bereits initialisiert, dann das Loginzeugs mit den Balloons und so und das Fenster soll geschlossen werden

    //TODO: Das nur, wenn es noch nicht gespeichert wurde, bekommt das verschlüsselte Passwort übergeben
    //TODO: Grafik erstellen
    private void saveTokenInConfigFile() {
        String token = generateToken();
        //TODO: Das noch Speichern - in workspace
        isInitialized = true;
    }

    private boolean validatePassword(char[] password) {
        for(int i = 0; i < password.length; i++) {
            if(password.length >= 8) {
                showValidPasswordInfo();
                return true;
            } else {
                showInvalidPasswordError();
            }
        }
        return false;
    }

    //TODO: Das darf auch nur einmal aufgerufen werden
    private String generateToken() {
        System.out.println(controller.idCreator.createUUID());
        return controller.idCreator.createUUID();
    }




    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.onLoginButtonPressed();
            validatePassword(getPassword());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        //TODO: Hier noch schließen
    }
}
