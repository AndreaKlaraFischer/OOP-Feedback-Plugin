package gui;

import actions.BalloonPopup;
import answers.Answer;
import answers.AnswerList;
import answers.AnswerTableModel;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class MailBoxScreen implements ActionListener {

    private AnswerTableModel answerTableModel;
    private JBTable answerOverviewTable;

    private AnswerList rowList;
    private JPanel mailBoxScreenContent;

    public JTextField noAnswersTextfield;
    private Controller controller;
    public JBScrollPane answerScrollPane;
    private JBScrollPane detailScrollPane;

    private AnswerDetailScreen answerDetailScreen;



    public MailBoxScreen(Controller controller) {
        this.controller = controller;
        controller.mailBoxScreen = this;
        //14.10.
        answerDetailScreen = new AnswerDetailScreen(controller.mailBoxScreen, controller);

       //TODO: kapseln (nicht den Umweg über s GitHubModel, zB controller.getAnswers)
        rowList = controller.gitHubModel.answerList;

        //TODO: Das noch fixen
        mailBoxScreenContent = new JPanel();
        noAnswersTextfield = new JTextField();
        noAnswersTextfield.setText("Noch keine Antworten vorhanden.");
        noAnswersTextfield.setEditable(false);
        answerOverviewTable = new JBTable();
        answerOverviewTable.setDragEnabled(false);
        //controller!
        answerTableModel = new AnswerTableModel(controller.gitHubModel.answerList);
        answerOverviewTable.setModel(answerTableModel);
        //12.10.
        answerOverviewTable.setRowSelectionAllowed(true);
        ListSelectionModel rowSelectionModel = answerOverviewTable.getSelectionModel();
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        //Das gehört zur Tabelle
        answerScrollPane = new JBScrollPane(answerOverviewTable);
        answerScrollPane.setViewportView(answerOverviewTable);
        //scrollPane.setSize(400,400);
        mailBoxScreenContent.add(noAnswersTextfield);
        mailBoxScreenContent.add(answerScrollPane);

        detailScrollPane = answerDetailScreen.answerDetailScrollPane;

        changeVisibilityOfTextFieldAndTable();

        answerOverviewTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //Zeile kann mit Einfach- oder mit Doppelklick ausgewählt werden
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.rowAtPoint(e.getPoint());
                    if(row >= 0) {
                        controller.onAnswerSelected(answerTableModel.getAnswerAt(row));
                    }
                }
            }
        });
    }

    public void showAnswerDetailContent(Answer answer) {
        mailBoxScreenContent.remove(answerScrollPane);
        mailBoxScreenContent.remove(noAnswersTextfield);
        mailBoxScreenContent.add(detailScrollPane);
        detailScrollPane.setVisible(true);


        //setCorrectContents();
        answerDetailScreen.detailedAnswerField.setText(answer.getAnswerMessage());

        //createAnswerDetailTitle();
        String answerTitle = "";
        String tutorName = answer.getTutorName();
        //NPE gefixt?
        if(tutorName.length() > 0) {
            answerTitle = Constants.ANSWER_TITLE_BEGINNING + Constants.SEPARATOR + tutorName;
            answerDetailScreen.answerTitleLabel.setText(answerTitle);
        } else {
            answerTitle = Constants.ANSWER_TITLE_BEGINNING;
            answerDetailScreen.answerTitleLabel.setText(answerTitle);
        }
        mailBoxScreenContent.revalidate();
    }


    public void navigateBackToTable() {
        mailBoxScreenContent.remove(detailScrollPane);
        mailBoxScreenContent.add(answerScrollPane);
        mailBoxScreenContent.revalidate();
    }


    public void refreshTable() {
        answerTableModel.fireTableDataChanged();
        answerScrollPane.setVisible(true);
        noAnswersTextfield.setVisible(false);
        showNotification();
    }

    //TODO: Das noch resistenter machen. wird ja dann im Endeffekt bei refreshTable wieder aufgerufen --> dublicate code
    private void changeVisibilityOfTextFieldAndTable() {
        try {
            if (rowList.getAnswerList().size() == 0) {
                answerScrollPane.setVisible(false);
                noAnswersTextfield.setVisible(true);
            } else if (rowList.getAnswerList().size() > 0) {
                noAnswersTextfield.setVisible(false);
                answerScrollPane.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public JPanel getContent() {
        return mailBoxScreenContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void showNotification() {
        showMessageDialog(null, "Neue Antwort von Tutor!");
        navigateBackToTable();
    }
}
