package gui;

import javax.swing.*;

public class AnswerDetailScreen {
    //TODO: Dieser Screen soll nur bei Anklicken aufgehen!
    //TODO: neuen Tab öffnen zum Anzeigen des Codes
    //Textfeld, readonly: Antwort des Tutors wird angezeigt
    private JPanel answerScreenContent;
    private JButton button1;
    private JRadioButton radioButton1;

    public AnswerDetailScreen() {

    }

    public JPanel getContent() {
        return answerScreenContent;
    }
}
