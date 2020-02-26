package gui;

import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class TutorialScreen {
    private JPanel tutorialScreenContent;
    private JTextPane sendRequestTextPane;
    private JTextPane findAnswersTextPane;
    private JTextPane changeNameTextPane;
    private JTextPane withoutLoginTextPane;
    private JTextPane attachScreenshotsLabel;
    private JTextPane durchDieLoginfunktionKannTextPane;
    private JTextPane sollteDasPasswortVergessenTextPane;
    private JTextPane findAnswerTextPane;
    private JPanel Container;
    private JTextPane questionnaireTextPane;
    private JPanel sendRequestsPanel;
    private JPanel attachScreenshotsPanel;
    private JPanel changeNamePanel;
    private JPanel withoutLoginPanel;
    private JPanel multipleDevicePanel;
    private JPanel forgotPasswordPanel;
    private JPanel questionnairePanel;
    private JPanel findAnswersPanel;


    public TutorialScreen()  {
        tutorialScreenContent.setAutoscrolls(true);
    }


    public JPanel getContent() {
        return tutorialScreenContent;
    }


}
