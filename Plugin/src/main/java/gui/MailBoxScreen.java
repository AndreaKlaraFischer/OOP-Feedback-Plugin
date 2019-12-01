package gui;

import answers.Answer;
import answers.AnswerList;
import answers.AnswerTableModel;
import com.intellij.ui.components.JBScrollPane;
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
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.rowAtPoint(e.getPoint());
                    if(row >= 0) {
                        //TODO
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


    public void refreshTable() {
        //updateNoAnswersLabel();
        answerTableModel.fireTableDataChanged();
        answerScrollPane.setVisible(true);
        noAnswersLabel.setVisible(false);
        //TODO: Fixen
        //toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(mailBoxScreenContent));
    }

    //TODO: Das noch resistenter machen. wird ja dann im Endeffekt bei refreshTable wieder aufgerufen --> dublicate code
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
           // navigateBackToTable();
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
        System.out.println("showDetailContent");
        controller.answerDetailScreen1.detailAnswerTitleLabel.setText(Constants.ANSWER_TITLE_BEGINNING + answer.getTutorName());
        controller.answerDetailScreen1.previousMessageTextArea.setText(controller.getRequestMessage());
        controller.answerDetailScreen1.tutorAnswerTextArea.setText(answer.getAnswerMessage());

        //TODO!!!
        /*if(controller.answerDetailScreen1.imageButtonList.size() == 0) {
            controller.answerDetailScreen1.createImageFromAttachedImageFile(answer.getImageUrls());
        }*/

    }

    public JPanel getContent() {
        return mailBoxScreenContent;
    }
}