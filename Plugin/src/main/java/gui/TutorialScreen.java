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
    private JTextPane feedbackForFeedbackTextPane;
    private JTextPane screenshotTextPane;
    private JTextPane anonymousQuestionsTextPane;
    private JTextPane bookmarksTextPane;
    private JTextPane multipleDeviceTextPane;
    private JTextPane forgotPasswordTextPane;


    public TutorialScreen()  {
        tutorialScreenContent.setAutoscrolls(true);
    }


    public JPanel getContent() {
        return tutorialScreenContent;
    }


}
