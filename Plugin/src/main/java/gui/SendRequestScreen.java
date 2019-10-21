package gui;

import actions.BalloonPopup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import controller.Controller;
import org.kohsuke.github.GHIssue;
import requests.IDCreator;
import requests.ScreenshotModel;
import requests.StudentRequestModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class SendRequestScreen implements ActionListener{
    private JButton submitRequestButton;
    private JPanel sendRequestScreenContent;
    public JComboBox selectCategory;
    private JTextArea inputMessageArea;
    private JTextField introductionText;
    private JButton selectFileButton;
    private JLabel hyperlink;
    //28.09. Versuch, öffentlich, damit ich aus dem StudentRequestModel darauf zugreifen kann
    public static String inputMessage;
    public static String problemCategory;
    //03.10. Hier werden alle ausgewählten Screenshots gespeichert. Diese müssen ja nich persistent gespeichert werden, oder?

    public String clonedRepoPath;

    public Controller controller;
    private StudentRequestModel studentRequestModel;
    private ScreenshotModel screenshotModel;
    private BalloonPopup balloonPopup;
    private IDCreator idCreator;
    private List<GHIssue> issueList;
    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)

    //TODO: Bookmarks, übertragen in das Repo (wo werden Bookmarks gespeichert (Projektdatei?)
    //Bookmarks setzen: (Strg + )F11
    //Bookmarks vielleicht noch mal woanders speichern?

    public SendRequestScreen(Project project, Controller controller) {
        this.controller = controller;
        controller.sendRequestScreen = this;
        //TODO: Das muss noch über den Controller geregelt werden!
       // this.studentRequestModel = new StudentRequestModel(project, controller);
        this.idCreator = new IDCreator();
        //this.screenshotModel = new ScreenshotModel(controller.studentRequestModel);

        balloonPopup = new BalloonPopup();

        submitRequestButton.addActionListener(this);
        selectCategory.addActionListener(this::saveSelectedCategoryAsString);
        selectFileButton.addActionListener(this::openFileSelector);

        hyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    //TODO: hier den anderen Content zeigen, switch tabs
                    Desktop.getDesktop().browse(new URI("http://www.codejava.net"));

                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                hyperlink.setText("<html><a href=''>" + "HyperlinkTest"+ "</a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hyperlink.setText("HyperlinkTest");
            }
        });
    }

    public JPanel getContent() {
        return sendRequestScreenContent;
    }


    private void openFileSelector(ActionEvent actionEvent)  {
        controller.onSelectFileButtonPressed();
    }

    //MAC Adresse plus UUID, im Plugin an geeigneter Stelle merken, verwenden für Branch und Issue

    //TODO: Model / View / Controller --> ActionListener in Model auslagern?
    //https://javabeginners.de/Design_Patterns/Model-View-Controller.php
    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO: Das nochwoanders herholen bzw "speichern"
        //inputMessage = inputMessageArea.getText().trim();
        saveSelectedCategoryAsString(e);
        controller.onSubmitRequestButtonPressed();
    }

    public String getInputMessage() {
        return inputMessageArea.getText().trim();
    }

    public void showEmptyMessageError() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Bitte eine Nachricht eingeben!", MessageType.ERROR);

    }

    public void showNoNameWarning() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Bitte zuerst einen Namen eingeben! Ansonsten wird die Anfrage anonym abgeschickt.", MessageType.WARNING);
    }

    public void showSentRequestInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Anfrage wurde abgeschickt", MessageType.INFO);
    }

    public void showErrorMessage(String message) {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, message, MessageType.ERROR);
    }

    public void saveSelectedCategoryAsString(ActionEvent e) {
        problemCategory = Objects.requireNonNull(selectCategory.getSelectedItem()).toString();
        if(problemCategory != null) {
            System.out.println(problemCategory);
        }
    }

}