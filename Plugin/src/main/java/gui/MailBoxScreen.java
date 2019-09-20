package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MailBoxScreen implements ActionListener {
    //TODO: Button für Actionlistener, Debugzwecke
    private JPanel mailBoxScreenContent;
    private JButton testButton;


    public MailBoxScreen() {
        testButton.addActionListener(this::actionPerformed);

    }
    //TODO: Wenn hier etwas geklickt wird, soll der "AnswerDetailScreen" aufgehen

    public JPanel getContent() {
        return mailBoxScreenContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Hier sollte dann etwas cooles passieren!");
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
