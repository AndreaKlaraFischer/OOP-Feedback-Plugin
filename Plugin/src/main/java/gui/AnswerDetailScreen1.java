package gui;

import answers.ImagePanel;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.components.JBScrollPane;
import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnswerDetailScreen1 implements ActionListener {
    private Controller controller;
    public JPanel answerDetailScreenContent;
    private JButton backToTableButton;
    public JTextPane previousRequestField;
    public JTextPane tutorAnswerField;
    public JLabel detailAnswerTitleLabel;
    private JLabel feedbackForFeedbackLabel;
    private JLabel selectFeedbackIntroductionLabel;
    private JLabel introductionFeedbackTextLabel;
    private JButton openCodeButton;
    private JButton helpfulFeedbackButton;
    private JButton neutralFeedbackButton;
    private JButton notHelpfulFeedbackButton;
    private JButton sendFeedbackButton;
    private JCheckBox solvedProblemCheckbox;
    public JTextArea feedbackTextField;
    public JTextArea tutorAnswerTextArea;
    public JTextArea previousMessageTextArea;

    public int selectedHelpfulness;

    private JFrame highResolutionFrame;
    //17.11.
    public ArrayList<JButton> imageButtonList;

    public AnswerDetailScreen1(Controller controller) {
        this.controller = controller;
        controller.answerDetailScreen1 = this;
        backToTableButton.addActionListener(this);
        openCodeButton.addActionListener(this);
        helpfulFeedbackButton.addActionListener(this);
        neutralFeedbackButton.addActionListener(this);
        notHelpfulFeedbackButton.addActionListener(this);
        sendFeedbackButton.addActionListener(this);
        solvedProblemCheckbox.addActionListener(this);

        openCodeButton.setVisible(false);
    }

    public JPanel getContent() {
        return answerDetailScreenContent;
    }

    private Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String taskSuccessfullySolved = "";
        Object clickedButton = getClickedButton(e);
        if(clickedButton == backToTableButton) {
            controller.toolWindowFactory.navigateBackToMailBoxScreen();
        } else if(clickedButton == openCodeButton) {
            controller.logData("Code geöffnet");
            controller.onOpenCodeButtonPressed();
        } else if(clickedButton == helpfulFeedbackButton) {
            controller.logData("Hilfreich Button ausgewählt");
            selectedHelpfulness = 1;
        } else if(clickedButton == neutralFeedbackButton) {
            controller.logData("Neutral hilfreich Button ausgewählt");
            selectedHelpfulness = 2;
        } else if(clickedButton == notHelpfulFeedbackButton) {
            controller.logData("Nicht hilfreiche Button ausgewählt");
            selectedHelpfulness = 3;
        } else if(clickedButton == sendFeedbackButton) {
            controller.logData("Feedback abgeschickt");
            controller.sendFeedbackForFeedback(selectedHelpfulness);
        }
    }

    public void createFeedbackText() {
        String prefix = Constants.GITHUB_BOLD_AND_ITALIC + Constants.FEEDBACK_FOR_FEEDBACK + Constants.GITHUB_BOLD_AND_ITALIC;
        String feedbackMessage = Constants.GITHUB_ITALIC + feedbackTextField.getText() + Constants.GITHUB_ITALIC;
        System.out.println(prefix + feedbackMessage);
        if(feedbackTextField.getText().length() > 0) {
            controller.gitHubModel.setFeedbackText(prefix, feedbackMessage);
        }
    }

    public void showSentFeedbackBalloon() {
        BalloonPopup balloonPopup = new BalloonPopup();
        balloonPopup.createBalloonPopup(answerDetailScreenContent, Balloon.Position.above, "Feedback zum Feedback wurde abgeschickt", MessageType.INFO);
    }

    public void activateOpenCodeButton() throws GitAPIException, IOException, BadLocationException {
        System.out.println("controller hasChanges, sollte false sein: " + controller.hasChanges);
        if(controller.getModifiedFiles().size() > 0) {
            //TODO: Das woanders noch machen!
            controller.hasChanges = true;
            System.out.println("controller hasChanges, sollte true sein: " + controller.hasChanges);
            openCodeButton.setVisible(true);
            openCodeButton.setEnabled(true);
        }
    }

    public void createImageFromAttachedImageFile(List<String> imageUrls) {
        try {
            for (String imageUrl : imageUrls) {
                BufferedImage img = ImageIO.read(new URL(imageUrl));
                //"http://www.java2s.com/style/download.png"));
                ImageIcon icon = new ImageIcon(img);
                //Bild transformieren
                Image image = icon.getImage();
                Image scaledImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
                //Bild dem Button hinzufügen
                JButton imageButton = new JButton(icon);
                //17.11.
                imageButtonList.add(imageButton);
                imageButton.addActionListener(e -> {
                    openImageHighResolution(img);
                    controller.logData("Screenshot geöffnet");
                });
                answerDetailScreenContent.add(imageButton);
                //answerDetailPanel.add(imageButton);

                //Versuch
                //addImageToFrame(image);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openImageHighResolution(BufferedImage img) {
        System.out.println("Wird aufgerufen und ich weiß noch nicht so genau, wie ich den Frame erzeugen lassen soll");

        JFrame f = new JFrame("Image pane");
        f.setSize(img.getWidth() / 2, img.getHeight() / 2);
        Image scaledImg = img.getScaledInstance(img.getWidth() / 2, img.getHeight() / 2, Image.SCALE_SMOOTH);
        JBScrollPane imageSrcollPane = new JBScrollPane(new ImagePanel(scaledImg));
        //f.setContentPane(scrolli);
        f.getContentPane().add(imageSrcollPane);
        //f.pack();
        f.setVisible(true);
    }
}
