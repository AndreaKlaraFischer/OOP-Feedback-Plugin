package gui.uiElements;
//TextArea um Problemnachrichten eingeben zu können
//Ziel: Umrandung, feste Breite und Höhe, bestimmte Schrift
//Speichern des Textes in eine Variable

import javax.swing.*;
import java.awt.*;

public class InputMessageTextArea {

    private JTextArea inputMessageArea;
    public  InputMessageTextArea(boolean editable, String text) {
        //setLayout(new BorderLayout());
        inputMessageArea = new JTextArea("");
        //inputMessageArea.setSize(400,400);
        inputMessageArea.setWrapStyleWord(true);
        inputMessageArea.setLineWrap(true);
        inputMessageArea.setEditable(editable);
        inputMessageArea.setText(text);

    }

    public void setText(String text) {
        inputMessageArea.setText(text);
    }

    public String getText() {
        return inputMessageArea.getText();
    }

}
