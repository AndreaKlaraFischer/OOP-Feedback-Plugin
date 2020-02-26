package gui;

import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.wm.ToolWindow;
import com.sun.javafx.cursor.CursorType;
import controller.Controller;
import org.intellij.lang.annotations.JdkConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class SendRequestScreen implements ActionListener{
    public JButton submitRequestButton;
    private JPanel sendRequestScreenContent;
    public JComboBox selectCategory;
    public JTextArea inputMessageArea;
    private JButton selectFileButton;
    private JLabel bookmarkHyperlink;
    public JLabel attachedScreenshotsLabel;
    private JPanel container;
    private JPanel introductionPanel;
    private JPanel sendRequestPanel;
    private JTextPane introductionTextPane;
    private JLabel settingsHyperlink;
    public JButton deleteScreenshotsButton;
    public JPanel attachedScreenshotPanel;
    private JLabel assistanceHyperlink;
    private JLabel currentNameLabel;
    private JPanel nameSettingsPanel;
    public Controller controller;
    private BalloonPopup balloonPopup;

    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)


    public SendRequestScreen(Controller controller, ToolWindow toolWindow, AssistanceScreen assistanceScreen, SettingScreen settingScreen) {
        this.controller = controller;
        controller.sendRequestScreen = this;
        balloonPopup = new BalloonPopup();
        currentNameLabel.setText("Angemeldet als " + controller.getStudentNameInXML());
        String settingHyperlinkText = "Namen bearbeiten";
        settingsHyperlink.setText("<HTML><U>" + settingHyperlinkText + "</U></HTML>");
        settingsHyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        String assistanceHyperlinkText = "Hilfestellung";
        assistanceHyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        assistanceHyperlink.setText("<HTML><U>Wenn du dir mit einer Fehlermeldung unsicher bist, schaue in der "  + assistanceHyperlinkText + " nach</U></HTML>");
        deleteScreenshotsButton.addActionListener(this);
        deleteScreenshotsButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitRequestButton.addActionListener(this);
        submitRequestButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        selectFileButton.addActionListener(this::openFileSelector);
        selectFileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //TODO: Labeltext ändern zu: noch kein Name angegeben (wenn leer)

        if(controller.isLoggedIn) {
            showWelcomeBackInfo();
        }
        assistanceHyperlink.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(assistanceScreen.getContent()));
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
        settingsHyperlink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(settingScreen.getContent()));
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

    private Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }


    private void openFileSelector(ActionEvent actionEvent)  {
        controller.onSelectFileButtonPressed();
    }

    //https://javabeginners.de/Design_Patterns/Model-View-Controller.php
    @Override
    public void actionPerformed(ActionEvent e) {
        Object clickedButton = getClickedButton(e);
        if(clickedButton == deleteScreenshotsButton) {
            if(controller.screenshotModel.screenshotTitles.size() > 0) {
                controller.screenshotModel.deleteScreenshots();
                showDeletedScreenshotListWarning();
            }
        } else if (clickedButton == submitRequestButton) {
            saveSelectedCategoryAsString();
            controller.onSubmitRequestButtonPressed();
        }

    }

    private void showDeletedScreenshotListWarning() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Screenshots entfernt", MessageType.WARNING );
    }

    private void showWelcomeBackInfo() {
        balloonPopup.createBalloonPopup(sendRequestScreenContent, Balloon.Position.above, "Willkommen!" + controller.getStudentNameInXML(), MessageType.INFO);
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

    public void updateLabel() {
        currentNameLabel.setText("Angemeldet als " + controller.getStudentNameInXML());
    }
}