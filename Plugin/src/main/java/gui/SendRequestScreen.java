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
    public JTextArea inputMessageArea;
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

        if(controller.isLoggedIn) {
            showWelcomeBackInfo();
        }
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

    public void showWelcomeBackInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Willkommen zurück!" + controller.getStudentNameInXML(), MessageType.INFO);
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

    public void showScreenshotAttachedInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Screenshot angehängt", MessageType.INFO);
    }

    public String saveSelectedCategoryAsString() {
        return Objects.requireNonNull(selectCategory.getSelectedItem()).toString();
    }

    public void showNoInternetWarning() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Keine Internetverbindung! Um Anfragen zu stellen, musst du mit dem Internet verbunden sein", MessageType.WARNING);
    }
}