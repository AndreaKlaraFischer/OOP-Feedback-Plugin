package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorialScreen implements ActionListener{
    private JPanel tutorialScreenContent;
    private JTextPane platzhalterText;
    private JTextField wasMussHierAllesTextField;
    private JButton testi;
    private Controller controller;
    RegistrationMenu registrationMenu;


    public TutorialScreen(Controller controller) {
        this.controller = controller;
        testi.addActionListener(this::actionPerformed);
        //TODO: Das ist hier nur zu Testzwecken
        registrationMenu = new RegistrationMenu();
    }

    public JPanel getContent() {
        return tutorialScreenContent;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        registrationMenu.showDialog();
    }
}
