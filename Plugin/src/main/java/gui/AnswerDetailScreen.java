package gui;

import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.components.JBScrollPane;
import config.Constants;
import controller.Controller;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

//TODO: Das wird wahrscheinlich auch nur so lang funktionieren, bis die .form files wieder gehen
public class AnswerDetailScreen  implements ActionListener {
    private JPanel testPanel;
    private MailBoxScreen mailBoxScreen;
    public JBScrollPane answerDetailScrollPane;
    private JButton openCodeButton;
    private JButton backToTableButton;
    private JLabel answerTitelLabel;
    public JButton notHelpfulButton;
    public JButton semiHelpfulButton;
    public JButton helpfulButton;

    private Controller controller;

    JPanel answerDetailPanel;
    public JTextField detailedAnswerField;
    public JLabel answerTitleLabel;
    public JTextArea feedbackInputField;
    private JButton sendFeedbackButton;
    private JCheckBox solvedCheckbox;
    public String feedbackMessage;

    public int selectedHelpfulness;
    public String taskSuccessfullySolved;

    public ImageIcon icon;
    public BufferedImage img;
    private JLabel imageLabel;


    public AnswerDetailScreen(MailBoxScreen mailBoxScreen, Controller controller) {
        this.controller = controller;
        this.mailBoxScreen = mailBoxScreen;
        controller.answerDetailScreen = this;


        GridLayout testLayout = new GridLayout(3,2);
        detailedAnswerField = new JTextField();
        detailedAnswerField.setEditable(false);

        answerTitleLabel = new JLabel();

        solvedCheckbox.addActionListener(this::actionPerformed);
        JButton openCodeButton = new JButton();
        openCodeButton.setText("zum Code");
        openCodeButton.addActionListener(this::openCodeInNewWindow);
        sendFeedbackButton.addActionListener(this::actionPerformed);
        JButton backButton = new JButton();
        //backButton.setText("Zurück zur Übersicht");
        //backButton.addActionListener(this::backToTable);

        imageLabel = new JLabel();

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
    public  void createImageFromAttachedImageFile(List<String> imageUrls) {
        //24.10. Test mit hardgecodeter URL, funktioniert
        try {
            for(int i = 0; i < imageUrls.size(); i++) {
                img = ImageIO.read(new URL(
                        //TODO: Hier den Link, den ich aus dem Bildkommentar geholt habe
                        imageUrls.get(i)));
                //"http://www.java2s.com/style/download.png"));
                icon = new ImageIcon(img);
                compressImages(img);
                imageLabel.setIcon(icon);
                answerDetailPanel.add(imageLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void compressImages(BufferedImage img) {

    }


    private void openCodeInNewWindow(ActionEvent e) {
        activateOpenCodeButton();
        controller.onOpenCodeButtonPressed();
        //TODO: CodeModel, herausfinden, wie das geht.
        //TODO: Marlena fragen, was sie bis jetzt herausgefunden hat über die File API
        System.out.println("Open annotated Code");
    }

    //TODO: Wenn keine Änderungen im Code vom Tutor vorgenommen wurden, soll der Button gar nicht erst sichtbar sein
    //--> Sorgt sonst nur für Verwirrung
    private void activateOpenCodeButton() {
        if(controller.gitHubModel.checkIfChangesInCode()) {
            openCodeButton.setVisible(true);
            openCodeButton.setEnabled(true);
        } else {
            openCodeButton.setVisible(false);
            openCodeButton.setEnabled(false);
        }
    }

    private void backToTable(ActionEvent e) {
        mailBoxScreen.navigateBackToTable();
    }


    public Object getClickedButton(ActionEvent e) {
        return e.getSource();
    }

    //TODO: Das refactoren über den Controller. Auch wiedeer falsch
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
