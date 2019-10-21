package gui;

import actions.BalloonPopup;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.components.JBScrollPane;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//TODO: Das wird wahrscheinlich auch nur so lang funktionieren, bis die .form files wieder gehen
public class AnswerDetailScreen  implements ActionListener {
    private MailBoxScreen mailBoxScreen;
    public JBScrollPane answerDetailScrollPane;
    private JPanel testPanel;
    private JTextField textField1;
    private JButton openCodeButton;
    private JButton backToTabelButton;
    private JLabel answerTitelLabel;
    private JButton notHelpfulButton;
    private JButton semiHelpfulButton;
    private JButton helpfulButton;

    private Controller controller;

    public JTextField detailedAnswerField;
    public JLabel answerTitleLabel;
    public JTextArea feedbackInputField;
    public String feedbackMessage;

    public AnswerDetailScreen(MailBoxScreen mailBoxScreen, Controller controller) {
        this.controller = controller;
        this.mailBoxScreen = mailBoxScreen;

        GridLayout testLayout = new GridLayout(3,2);
        detailedAnswerField = new JTextField();
        detailedAnswerField.setEditable(false);

        answerTitleLabel = new JLabel();

        JButton openCodeButton = new JButton();
        openCodeButton.setText("zum Code");
        openCodeButton.addActionListener(this::openCodeInNewWindow);
        JButton backButton = new JButton();
        backButton.setText("Zurück zur Übersicht");
        backButton.addActionListener(this::backToTable);

        helpfulButton.addActionListener(this);
        semiHelpfulButton.addActionListener(this);
        notHelpfulButton.addActionListener(this);

        JPanel answerDetailPanel = new JPanel();
        answerDetailPanel.setLayout(testLayout);
        answerDetailPanel.setSize(500,600);

        answerDetailPanel.add(detailedAnswerField);
        answerDetailPanel.add(openCodeButton);
        answerDetailPanel.add(answerTitleLabel);
        answerDetailPanel.add(backButton);
        answerDetailPanel.add(semiHelpfulButton);
        answerDetailPanel.add(notHelpfulButton);
        answerDetailPanel.add(helpfulButton);
        answerDetailPanel.add(feedbackInputField);

        answerDetailScrollPane = new JBScrollPane(answerDetailPanel);
    }


    private void openCodeInNewWindow(ActionEvent e) {
        controller.onOpenCodeButtonPressed();
        //TODO: CodeModel, herausfinden, wie das geht.
        //TODO: Marlena fragen, was sie bis jetzt herausgefunden hat über die File API
        System.out.println("Open annotated Code");
    }

    private void backToTable(ActionEvent e) {
        mailBoxScreen.navigateBackToTable(e);
    }

    //TODO: Das refactoren über den Controller
    @Override
    public void actionPerformed(ActionEvent e) {
        controller.gitHubModel.matchFeedbackAndRequest(controller.gitHubModel.answerNumber);
        //Switch geht leider nicht mit Objekten
        Object clickedButton = e.getSource();
        if(clickedButton == helpfulButton) {
            //TODO: Das refactoren! duplicate code
           controller.gitHubModel.setHelpfulFeedbackLabel();
           createFeedbackText();
           showSentFeedbackBalloon();
        } else if (clickedButton == semiHelpfulButton) {
            controller.gitHubModel.setNeutralLabel();
            createFeedbackText();
            showSentFeedbackBalloon();
        } else if (clickedButton == notHelpfulButton) {
            controller.gitHubModel.setNotHelpfulFeedbackLabel();
            createFeedbackText();
            showSentFeedbackBalloon();
        }
    }


    public void createFeedbackText() {
        String prefix = Constants.GITHUB_BOLD_AND_ITALIC + Constants.FEEDBACK_FOR_FEEDBACK + Constants.GITHUB_BOLD_AND_ITALIC;
        String feedbackMessage = Constants.GITHUB_ITALIC + feedbackInputField.getText() + Constants.GITHUB_ITALIC;
        if(feedbackInputField.getText().length() > 0) {
            controller.gitHubModel.setFeedbackText(prefix, feedbackMessage);
        }
    }

    public void showSentFeedbackBalloon() {
        BalloonPopup balloonPopup = new BalloonPopup();
        balloonPopup.createBalloonPopup(answerDetailScrollPane, Balloon.Position.above, "Feedback zum Feedback wurde abgeschickt", MessageType.INFO);
    }
}
