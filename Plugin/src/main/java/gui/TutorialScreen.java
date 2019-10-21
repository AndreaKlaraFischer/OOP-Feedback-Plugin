package gui;

import config.Constants;
import controller.Controller;
import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class TutorialScreen {
    private JPanel tutorialScreenContent;
    private JTextPane loremIpsumDolorSitTextPane;
    private JTextField wasMussHierAllesTextField;
    private Controller controller;


    public TutorialScreen(Controller controller) {
        this.controller = controller;
    }

    public JPanel getContent() {
        return tutorialScreenContent;
    }


}
