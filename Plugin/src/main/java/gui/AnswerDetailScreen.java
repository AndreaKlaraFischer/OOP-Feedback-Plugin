package gui;

import answers.ImagePanel;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.JBColor;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AnswerDetailScreen implements ActionListener {
    private Controller controller;
    public JPanel answerDetailScreenContent;
    private JButton backToTableButton;
    public JTextPane previousRequestField;
    public JTextPane tutorAnswerField;
    public JLabel detailAnswerTitleLabel;
    private JLabel feedbackForFeedbackLabel;
    private JButton openCodeButton;
    private JButton helpfulFeedbackButton;
    private JButton neutralFeedbackButton;
    private JButton notHelpfulFeedbackButton;
    private JButton sendFeedbackButton;
    private JCheckBox solvedProblemCheckbox;
    public JTextArea feedbackTextField;
    public JTextArea tutorAnswerTextArea;
    public JTextArea previousMessageTextArea;
    public JPanel screenshotPanel;
    private JPanel Container;
    private JPanel SmilieContainer;
    private JPanel AnswerContainer;
    private JBScrollPane RequestsPane;
    private JBScrollPane AnswerPane;
    private JPanel FeedbackPane;
    private JTextPane feedbackTextPane1;
    private JTextPane feedbackTextPane2;
    private JLabel attachedScreensnotsLabel;
    private BalloonPopup balloonPopup;


    public int selectedHelpfulness;

    public JFrame highResolutionFrame;
    //17.11.
    public ArrayList<JButton> imageButtonList;

    //TODO: TextPanes noch vertauschen!! im Screen
    //TODO: Label setzen richtig machen
    //TODO: Button anzeigen lassen!!

    public AnswerDetailScreen(Controller controller) {
        this.controller = controller;
        controller.answerDetailScreen = this;
        balloonPopup = new BalloonPopup();
        //30.11. Versuch, zu retten
        imageButtonList = new ArrayList<>();
        //
        backToTableButton.addActionListener(this);
        backToTableButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        openCodeButton.addActionListener(this);
        openCodeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        helpfulFeedbackButton.addActionListener(this);
        helpfulFeedbackButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        neutralFeedbackButton.addActionListener(this);
        neutralFeedbackButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notHelpfulFeedbackButton.addActionListener(this);
        notHelpfulFeedbackButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        sendFeedbackButton.addActionListener(this);
        sendFeedbackButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        solvedProblemCheckbox.addActionListener(this);
        solvedProblemCheckbox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        helpfulFeedbackButton.isSelected();

        helpfulFeedbackButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                helpfulFeedbackButton.setBackground(JBColor.GREEN);
                // helpfulFeedbackButton.setBorder(BorderFactory.createLineBorder(JBColor.GREEN));
                neutralFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
                notHelpfulFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
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


        neutralFeedbackButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                neutralFeedbackButton.setBackground(JBColor.YELLOW);
               notHelpfulFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
                helpfulFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
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

        notHelpfulFeedbackButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                notHelpfulFeedbackButton.setBackground(JBColor.RED);
                neutralFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
                helpfulFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {
             //   notHelpfulFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
            }
        });

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
            helpfulFeedbackButton.setBackground(JBColor.GREEN);
            notHelpfulFeedbackButton.setBackground((JBColor.LIGHT_GRAY));
            neutralFeedbackButton.setBackground(JBColor.LIGHT_GRAY);
            solvedProblemCheckbox.setSelected(false);

        } else if(clickedButton == openCodeButton) {
            //controller.logData("Code geöffnet");
            controller.onOpenCodeButtonPressed();
        } else if(clickedButton == helpfulFeedbackButton) {
            //controller.logData("Hilfreich Button ausgewählt");
           // helpfulFeedbackButton.setBackground(JBColor.GREEN);
            selectedHelpfulness = 1;
        } else if(clickedButton == neutralFeedbackButton) {
            //controller.logData("Neutral hilfreich Button ausgewählt");
           // neutralFeedbackButton.setBackground(JBColor.YELLOW);
            selectedHelpfulness = 2;
        } else if(clickedButton == notHelpfulFeedbackButton) {
           // controller.logData("Nicht hilfreiche Button ausgewählt");
           // notHelpfulFeedbackButton.setBackground(JBColor.RED);
            selectedHelpfulness = 3;
        } else if(clickedButton == sendFeedbackButton) {
            //01.12. Test, ob ich so beide Buttons verknüpfen kann
            //TODO: NPE beheben!
            if(solvedProblemCheckbox.isSelected()) {
               // controller.sendProblemSolved();
                controller.logData("Problem erfolgreich gelöst CheckBox");
                selectedHelpfulness = 4;
                //controller.gitHubModel.setProblemSolvedLabel();
            }
            controller.logData("Feedback abgeschickt, mit selectedHelpfulnesss: " + selectedHelpfulness);
            controller.sendFeedbackForFeedback(selectedHelpfulness);
           // controller.sendProblemSolved();
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
            //controller.hasChanges = true;
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
                Image scaledImage = image.getScaledInstance(80 , 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
                //Bild dem Button hinzufügen
                JButton imageButton = new JButton(icon);
                //17.11.
                imageButtonList.add(imageButton);
                imageButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                imageButton.addActionListener(e -> {
                    openImageHighResolution(img);
                    controller.logData("Screenshot geöffnet");
                });
                //answerDetailScreenContent.add(imageButton);
                System.out.println("screenshotPaneL vor neu erstellen: " + screenshotPanel);
                System.out.println("imagebutton vor neu erstellen: " + imageButton);
                screenshotPanel.add(imageButton);
                System.out.println("screenshotPanel nach neu erstellem: " + screenshotPanel);

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
