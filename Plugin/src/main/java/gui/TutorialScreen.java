package gui;

import answers.TextEditorDemo;
import controller.Controller;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TutorialScreen implements ActionListener{
    private JPanel tutorialScreenContent;
    private JTextPane platzhalterText;
    private JTextField wasMussHierAllesTextField;
    private JButton testi;
    private Controller controller;
    //Testi
    private LoginMenu loginMenu;
    //testi 5.11.
    public LoginMenu1 loginMenu1;
    private TextEditorDemo textEditorDemo;


    public TutorialScreen(Controller controller) throws BadLocationException {
        this.controller = controller;
        testi.addActionListener(this::actionPerformed);
        //TODO: Das ist hier nur zu Testzwecken
        loginMenu = new LoginMenu(controller);
        loginMenu1 = new LoginMenu1();

        textEditorDemo = new TextEditorDemo();
    }

    public JPanel getContent() {
        return tutorialScreenContent;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Das hier soll bei den Klick auf den "zum Code" Button aufgerufen werden!
        textEditorDemo.showTextEditor();
        //controller.annotatedCodeModel.showAnnotatedCode();
        controller.annotatedCodeModel.createWindow();
    }
}
