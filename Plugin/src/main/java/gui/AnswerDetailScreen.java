package gui;

import actions.BalloonPopup;
import answers.ImagePanel;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.components.JBScrollPane;
import config.Constants;
import controller.Controller;
import org.apache.batik.ext.awt.image.spi.ImageWriterParams;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

//TODO: Das wird wahrscheinlich auch nur so lang funktionieren, bis die .form files wieder gehen
public class AnswerDetailScreen  implements ActionListener {
    private JPanel testPanel;
    private MailBoxScreen mailBoxScreen;
    public JBScrollPane answerDetailScrollPane;
    private JButton openCodeButton;
    private JButton backToTableButton;
    public JButton notHelpfulButton;
    public JButton semiHelpfulButton;
    public JButton helpfulButton;

    private Controller controller;

    private JPanel answerDetailPanel;
    public JTextField detailedAnswerField;
    public JLabel answerTitleLabel;
    public JTextArea feedbackInputField;
    private JButton sendFeedbackButton;
    private JCheckBox solvedCheckbox;

    public int selectedHelpfulness;
    public String taskSuccessfullySolved;

    //private JButton imageButton;

    private JFrame highResolutionFrame;


    public AnswerDetailScreen(MailBoxScreen mailBoxScreen, Controller controller) {
        this.controller = controller;
        this.mailBoxScreen = mailBoxScreen;
        controller.answerDetailScreen = this;

        GridLayout testLayout = new GridLayout(3,2);
        detailedAnswerField = new JTextField();
        detailedAnswerField.setEditable(false);

        answerTitleLabel = new JLabel();

        solvedCheckbox.addActionListener(this);

        JButton openCodeButton = new JButton();
        openCodeButton.setText("zum Code");
        openCodeButton.addActionListener(this::openCodeInNewWindow);
        openCodeButton.setVisible(false);
        sendFeedbackButton.addActionListener(this);
        JButton backButton = new JButton();
        //backButton.setText("Zurück zur Übersicht");
        //backButton.addActionListener(this::backToTable);

       // imageLabel = new JLabel();

        helpfulButton.addActionListener(this);
        semiHelpfulButton.addActionListener(this);
        notHelpfulButton.addActionListener(this);
        backToTableButton.addActionListener(this::backToTable);
        //Test

        //Testende
        answerDetailPanel = new JPanel();
        answerDetailPanel.setLayout(testLayout);
        answerDetailPanel.setSize(500,600);

        answerDetailPanel.add(detailedAnswerField);
        answerDetailPanel.add(openCodeButton);
        answerDetailPanel.add(answerTitleLabel);
        //answerDetailPanel.add(backButton);
        answerDetailPanel.add(backToTableButton);
        answerDetailPanel.add(semiHelpfulButton);
        answerDetailPanel.add(notHelpfulButton);
        answerDetailPanel.add(helpfulButton);
        answerDetailPanel.add(feedbackInputField);
        answerDetailPanel.add(sendFeedbackButton);
        answerDetailPanel.add(solvedCheckbox);
        //24.10. Test
       // answerDetailPanel.add(imageLabel);




        answerDetailScrollPane = new JBScrollPane(answerDetailPanel);
    }

    //TODO: Bilder komprimieren
    public  Image createImageFromAttachedImageFile(List<String> imageUrls) {
        //24.10. Test mit hardgecodeter URL, funktioniert
        Image image = null;
        try {

            for(int i = 0; i < imageUrls.size(); i++) {
                //TODO: Das muss ich übergeben
                BufferedImage img = ImageIO.read(new URL(
                        //TODO: Hier den Link, den ich aus dem Bildkommentar geholt habe
                        imageUrls.get(i)));
                //"http://www.java2s.com/style/download.png"));
                ImageIcon icon = new ImageIcon(img);
                //Bild transformieren
                image = icon.getImage();
                Image scaledImage = image.getScaledInstance(120,120, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImage);
                //Bild dem Button hinzufügen
                JButton imageButton = new JButton(icon);
                imageButton.addActionListener(this::openImageHighResolution);
                answerDetailPanel.add(imageButton);

                //Versuch
                addImageToFrame(image);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    private void addImageToFrame(Image image) {
        highResolutionFrame = new JFrame();
        highResolutionFrame.setContentPane(new ImagePanel(image));
    }
    //TODO: Bild übergeben. Das geht noch nicht so.
    private void openImageHighResolution(ActionEvent e) {
      System.out.println("Wird aufgerufen und ich weiß noch nicht so genau, wie ich den Frame erzeugen lassen soll");
      highResolutionFrame.setSize(560,500);
      highResolutionFrame.pack();
      highResolutionFrame.setVisible(true);
    }


    //TODO: Hier dann das Fenster öffnen, was aktuell noch im Tutorialscreen aufgerufen wird
    private void openCodeInNewWindow(ActionEvent e) {
        controller.onOpenCodeButtonPressed();
        System.out.println("Open annotated Code");
    }

    //TODO: Das geht nicht! Das funktioniert nicht mit dem TutorialScreen
    //TODO: Wenn keine Änderungen im Code vom Tutor vorgenommen wurden, soll der Button gar nicht erst sichtbar sein
    //--> Sorgt sonst nur für Verwirrung
    //10.11. Ausgabe wird aufgerufen, aber der Button wird nicht manipuliert. Vielleicht sollte ich das umgekehrt machen
    //--> Standardmäßig nicht zeigen, aber wenn Änderungen da sind, enablen
    public void activateOpenCodeButton() {
        System.out.println(controller.hasChanges);
        if(controller.hasChanges) {
            openCodeButton.setVisible(true);
            openCodeButton.setEnabled(true);
        } else {
            System.out.println("keine Änderungen!");
            openCodeButton.setVisible(false);
            openCodeButton.setEnabled(false);
        }
    }

    private void backToTable(ActionEvent e) {
        mailBoxScreen.navigateBackToTable();
    }


    private Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //TODO: Hier überlegen, ob dort stehen soll: wurde nicht erfolgreich gelöst?
        taskSuccessfullySolved = "";
        Object clickedButton = getClickedButton(e);
        if(clickedButton == helpfulButton) {
            selectedHelpfulness = 1;
        } else if (clickedButton == semiHelpfulButton) {
            selectedHelpfulness = 2;
        } else if (clickedButton == notHelpfulButton) {
            selectedHelpfulness = 3;
        } else if (clickedButton == sendFeedbackButton) {
            controller.sendFeedbackForFeedback();
        } else if(clickedButton == solvedCheckbox) {
            //TODO: Hier Label anhängen
            taskSuccessfullySolved = Constants.PROBLEM_SOLVED_SUCCESSFULLY;
            controller.sendProblemSolved();
        }
    }



    public void createFeedbackText() {
        String prefix = Constants.GITHUB_BOLD_AND_ITALIC + Constants.FEEDBACK_FOR_FEEDBACK + Constants.GITHUB_BOLD_AND_ITALIC;
        String feedbackMessage = Constants.GITHUB_ITALIC + feedbackInputField.getText() + Constants.GITHUB_ITALIC;
        System.out.println(prefix + feedbackMessage);
        if(feedbackInputField.getText().length() > 0) {
            controller.gitHubModel.setFeedbackText(prefix, feedbackMessage);
        }
    }

    public void showSentFeedbackBalloon() {
        BalloonPopup balloonPopup = new BalloonPopup();
        balloonPopup.createBalloonPopup(answerDetailScrollPane, Balloon.Position.above, "Feedback zum Feedback wurde abgeschickt", MessageType.INFO);
    }

    public void showSolvedSuccessfullySentBalloon() {
        BalloonPopup balloonPopup = new BalloonPopup();
        balloonPopup.createBalloonPopup(answerDetailScrollPane, Balloon.Position.above, "Erfolgreich an Tutoren gesendet!", MessageType.INFO);
    }

}
