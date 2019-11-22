package gui;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.wm.ToolWindow;
import controller.Controller;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class SendRequestScreen implements ActionListener{
    private JButton submitRequestButton;
    private JPanel sendRequestScreenContent;
    public JComboBox selectCategory;
    private JTextArea inputMessageArea;
    private JTextField introductionText;
    private JButton selectFileButton;
    private JLabel bookmarkHyperlink;
    private JLabel screenshotHyperlink;
    public JLabel attachedScreenshotsLabel;
    public Controller controller;
    private BalloonPopup balloonPopup;

    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)


    public SendRequestScreen(Controller controller, ToolWindow toolWindow, TutorialScreen tutorialScreen) {
        this.controller = controller;
        controller.sendRequestScreen = this;
        balloonPopup = new BalloonPopup();

        submitRequestButton.addActionListener(this);
        selectFileButton.addActionListener(this::openFileSelector);
        attachedScreenshotsLabel.setVisible(false);
        screenshotHyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(tutorialScreen.getContent()));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        bookmarkHyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
               toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(tutorialScreen.getContent()));
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public JPanel getContent() {
        return sendRequestScreenContent;
    }


    private void openFileSelector(ActionEvent actionEvent)  {
        controller.onSelectFileButtonPressed();
    }

    //https://javabeginners.de/Design_Patterns/Model-View-Controller.php
    @Override
    public void actionPerformed(ActionEvent e) {
        saveSelectedCategoryAsString();
        controller.onSubmitRequestButtonPressed();
    }

    //TODO: Noch abfangen, dass wenn Screenshots angehängt wurden, ist der Input nicht mehr 0!
    //Idee: Gettermethode schreiben für die Bilderstrings und dann Länge minus die Bilder, wenn das größer 0 ist
    public String getInputMessage() {
        return inputMessageArea.getText().trim();
    }

    public void showEmptyMessageError() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Bitte eine Nachricht eingeben!", MessageType.ERROR);
    }

    public void showNoNameError() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Bitte einen Namen eingeben!.", MessageType.ERROR);
    }

    public void showSentRequestInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Anfrage wurde abgeschickt", MessageType.INFO);
    }

    public void showErrorMessage(String message) {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, message, MessageType.ERROR);
    }

    public void showWelcomeBackInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Willkommen zurück", MessageType.INFO);
    }

    public void showWelcomeInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Willkommen zum Plugin", MessageType.INFO);
    }

    public void showScreenshotAttachedInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Screenshot angehängt", MessageType.INFO);
    }

    public String saveSelectedCategoryAsString() {
        return Objects.requireNonNull(selectCategory.getSelectedItem()).toString();
    }
}