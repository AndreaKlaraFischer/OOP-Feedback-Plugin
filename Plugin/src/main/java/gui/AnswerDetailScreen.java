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
    //17.11.
    public ArrayList<JButton> imageButtonList;


    public AnswerDetailScreen(MailBoxScreen mailBoxScreen, Controller controller) {
        this.controller = controller;
        this.mailBoxScreen = mailBoxScreen;
        controller.answerDetailScreen = this;
        //17.11.
        imageButtonList = new ArrayList<>();

        GridLayout testLayout = new GridLayout(3,2);
        detailedAnswerField = new JTextField();
        detailedAnswerField.setEditable(false);

        answerTitleLabel = new JLabel();

        solvedCheckbox.addActionListener(this);

        JButton openCodeButton = new JButton();
        openCodeButton.setText("zum Code");
        openCodeButton.addActionListener(this);
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

        //TODO: Wenn Anfragen abgeschickt wurden und noch nicht beantwortet sind, dann anzeigen! Liste mit offenen Anfragen (Visability of system state) --> Ganzes Feature, nicht unterschätzen


        answerDetailScrollPane = new JBScrollPane(answerDetailPanel);
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
                answerDetailPanel.add(imageButton);

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
        JBScrollPane scrolli = new JBScrollPane(new ImagePanel(scaledImg));
        //f.setContentPane(scrolli);
        f.getContentPane().add(scrolli);
        //f.pack();
        f.setVisible(true);
    }

    //TODO: Hier dann das Fenster öffnen, was aktuell noch im Tutorialscreen aufgerufen wird
    //TODO: Controller
    private void openCodeInNewWindow() {
        System.out.println("Open annotated Code");
        controller.logData("Codefenster geöffnet");
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //TODO: Das geht nicht! Das funktioniert nicht mit dem TutorialScreen
    //TODO: Wenn keine Änderungen im Code vom Tutor vorgenommen wurden, soll der Button gar nicht erst sichtbar sein
    //--> Sorgt sonst nur für Verwirrung
    //10.11. Ausgabe wird aufgerufen, aber der Button wird nicht manipuliert. Vielleicht sollte ich das umgekehrt machen
    //--> Standardmäßig nicht zeigen, aber wenn Änderungen da sind, enablen
    public void activateOpenCodeButton() throws GitAPIException, IOException, BadLocationException {
        if(controller.getDiffEntries().size() > 0) {
            controller.hasChanges = true;
        }
        System.out.println(controller.hasChanges);
        if(controller.hasChanges) {
            openCodeButton.setVisible(true);
            openCodeButton.setEnabled(true);
        } /*else {
            System.out.println("keine Änderungen!");
            openCodeButton.setVisible(false);
            openCodeButton.setEnabled(false);
        }*/
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
            taskSuccessfullySolved = Constants.PROBLEM_SOLVED_SUCCESSFULLY;
            controller.sendProblemSolved();
        } else if(clickedButton == openCodeButton) {
            openCodeInNewWindow();
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
