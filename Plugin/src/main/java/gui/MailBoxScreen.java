package gui;

import answers.Answer;
import answers.AnswerList;
import answers.AnswerTableModel;
import com.intellij.ui.table.JBTable;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class MailBoxScreen {
    private Controller controller;
    private JPanel mailBoxScreenContent;

    private JLabel noAnswersLabel;
    private JLabel openRequestsLabel;
    private JScrollPane answerScrollPane;

    private AnswerList rowList;
    private AnswerTableModel answerTableModel;
    private JBTable answerOverviewTable;


    public MailBoxScreen(Controller controller) {
        this.controller = controller;
        controller.mailBoxScreen = this;

        openRequestsLabel.setText(Constants.DEFAULT_OPEN_REQUEST_LABEL);
        //openRequestsLabel.setText("Offene Anfragen: " + String.valueOf(controller.XMLFileReader.readOpenRequestsValueFromXML()));

        rowList = controller.answerList;
        answerTableModel = new AnswerTableModel(controller.answerList);

        //Tabelle wird nicht über form gemacht, sondern neu erstellt
        answerOverviewTable = new JBTable();
        answerOverviewTable.setDragEnabled(false);
        answerOverviewTable.setSelectionBackground(Constants.HEIDENELKENROT);
        answerOverviewTable.setModel(answerTableModel);
        answerOverviewTable.setRowSelectionAllowed(true);
        ListSelectionModel rowSelectionModel = answerOverviewTable.getSelectionModel();
        rowSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        answerScrollPane.setViewportView(answerOverviewTable);

        answerOverviewTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 || e.getClickCount() == 1) {
                    JTable target = (JTable) e.getSource();
                    int row = target.rowAtPoint(e.getPoint());
                    if(row >= 0) {
                        controller.onAnswerSelected(answerTableModel.getAnswerAt(row));
                        controller.logData("Antwort geöffnet");
                    }
                }
            }
        });

        changeVisibilityOfTextFieldAndTable();
    }

    public void updateNoAnswersLabel() {
        noAnswersLabel.setVisible(false);
    }


    public void refreshTableThread() {
        answerTableModel.fireTableDataChanged();
        answerScrollPane.setVisible(true);
        noAnswersLabel.setVisible(false);
    }

    public void refreshTableLogin() {
        answerScrollPane.setVisible(true);
        noAnswersLabel.setVisible(false);
    }

    //If there are no answered requests yet, a label with "Keine Antworten" is visible
    //If the user receives an answer, the label should not be visbile
    private void changeVisibilityOfTextFieldAndTable() {
        try {
            if (rowList.getAnswerList().size() == 0) {
                answerScrollPane.setVisible(false);
                noAnswersLabel.setVisible(true);
            } else if (rowList.getAnswerList().size() > 0) {
                noAnswersLabel.setVisible(false);
                answerScrollPane.setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void showNotification() {
        showMessageDialog(null, "Neue Antwort von Tutor!");
        if(!mailBoxScreenContent.isVisible()) {
        }
    }

    public void updateOpenRequest() {
        if(controller.XMLFileReader.readOpenRequestsValueFromXML() == 0) {
            openRequestsLabel.setText("Keine offenen Anfragen.");
        } else {
            openRequestsLabel.setText("Offene Anfragen: " + String.valueOf(controller.XMLFileReader.readOpenRequestsValueFromXML()));
        }
    }

    public void showAnswerDetailContent(Answer answer) {
        controller.answerDetailScreen.detailAnswerTitleLabel.setText(Constants.ANSWER_TITLE_BEGINNING + answer.getTutorName());
        controller.answerDetailScreen.previousMessageTextArea.setText(answer.getRequestMessage());
        controller.answerDetailScreen.tutorAnswerTextArea.setText(answer.getAnswerMessage());
        //for displaying attached screenshots: image urls have to be converted to image which is visible in a a preview
        if(controller.answerDetailScreen.imageButtonList.size() == 0) {
            controller.answerDetailScreen.createImageFromAttachedImageFile(answer.getImageUrls());
            controller.answerDetailScreen.screenshotPanel.setVisible(true);
        }

    }

    public JPanel getContent() {
        return mailBoxScreenContent;
    }
}