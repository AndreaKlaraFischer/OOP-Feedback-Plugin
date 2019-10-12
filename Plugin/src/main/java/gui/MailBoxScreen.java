package gui;

import answers.Answer;
import answers.AnswerList;
import answers.AnswerTableModel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import static javax.swing.JOptionPane.showMessageDialog;

public class MailBoxScreen implements ActionListener {

    private AnswerTableModel answerTableModel;
    private JBTable table1;

    private AnswerList rowList;
    private JPanel mailBoxScreenContent;
    private JButton testButton;
    public JTextField noAnswersTextfield;
    private Controller controller;
    private JBScrollPane scrollPane;
    private JBScrollPane answerDetailView;
    private JTextField testi;
    private JButton openCodeButton;
    private JTextField detailedAnswer;
    private JLabel answerTitleLabel;

    private String answerTitle;


    //TODO 09.10 Das geht noch nicht!
    public MailBoxScreen(Controller controller) {
        controller.mailBoxScreen = this;

        this.controller = controller;
        testButton.addActionListener(this);
        rowList = controller.gitHubModel.answerList;

        //TODO: Das noch fixen
        mailBoxScreenContent = new JPanel();
        answerTableModel = new AnswerTableModel(controller.gitHubModel.answerList);

        table1.setModel(answerTableModel);


        //TODO: Wie bekomme ich die alle in ein Panel? Kann ich einen Container einem Panel hinzufügen?
        //Das sind alles Testelemente für den "zweiten" Screen
        testi = new JTextField();
        testi.setText("teteteteetettststsstst"); //TODO: Kommentar zum Issue
        answerTitleLabel = new JLabel();
        answerTitle = Constants.ANSWER_TITEL_BEGINNING + "Tutorname" + Constants.ANSWER_TITLE_MIDDLE + "Datum";
        answerTitleLabel.setText(answerTitle);
        openCodeButton = new JButton();
        openCodeButton.setText("zum Code");
        openCodeButton.addActionListener(this::actionPerformed);
        detailedAnswer = new JTextField();
        detailedAnswer.setText(Constants.TEST_TEXT);
        //Versuch, mehrere Components hinzuzufügen
        answerDetailView = new JBScrollPane(answerDetailView);
        addComponentsToAnswerDetailPanel();
        answerDetailView.setVisible(false);
        //Das gehört zur Tabelle
        scrollPane = new JBScrollPane(table1);
        scrollPane.setViewportView(table1);
        mailBoxScreenContent.add(noAnswersTextfield);
        mailBoxScreenContent.add(scrollPane);
        mailBoxScreenContent.add(testButton);
        controller.gitHubModel.answerList.add(new Answer("messager", "name", new Date()));

        //Da soll die andere View gesetzt werden
        mailBoxScreenContent.add(answerDetailView);

    }

    //TODO: ganz dringend noch implementieren, dass jede Antwort nur einmal in die Tabelle hinzugefügt wird!!!!
    public void refreshTable() {
        answerTableModel.fireTableDataChanged();
        changeVisibilityOfTextField();
        showNotification();
    }

    //TODO: Das geht noch nicht leider
    public void addComponentsToAnswerDetailPanel() {
        answerDetailView.add(answerTitleLabel);
        answerDetailView.add(testi);
        answerDetailView.add(detailedAnswer);
        answerDetailView.add(openCodeButton);
    }

    private void changeVisibilityOfTextField() {
        try {
            if(rowList.getAnswerList().size() == 0 ) {
                showTextfield();
            } else if(rowList.getAnswerList().size() > 0){
                hideTextfield();
            }
        } catch (Exception e) {
            System.out.println("Aus diesem Grund hat diese Scheiße nicht funktioniert" + e.toString());
        }
    }


    public JPanel getContent() {
        return mailBoxScreenContent;
    }

    private void showAnswerDetailContent() {
        table1.getSelectedRow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        openCodeInNewWindow();
        changeVisibilityOfTableTest();
    }

    private void openCodeInNewWindow() {
        //TODO: CodeModel
    }

    private void hideTextfield() {
        noAnswersTextfield.setVisible(false);
    }

    private void showTextfield() {
        noAnswersTextfield.setVisible(true);
        System.out.println("Das wird ausgeführt");
    }


    //TODO: Das darf sich mit anderen Sichtbarkeiten nicht überschneiden
    public void changeVisibilityOfTableTest() {
        if(scrollPane.isVisible()) {
           scrollPane.setVisible(false);
           answerDetailView.setVisible(true);
        } else {
           scrollPane.setVisible(true);
           answerDetailView.setVisible(false);
        }
    }



    private void showNotification() {
        showMessageDialog(null, "Neue Antwort von Tutor!");
        //TODO: Das ist nicht die ideale Lösung, aber momentan funktioniert sie --> Sticky Balloons
        //Also am besten einen Listener auf die Tabellenreihe machen, damit die Notification wieder verschwindet
    }

    //TODO: Wenn eine Zeile angeklickt wird, soll der "AnswerDetailScreen" aufgehen

}

