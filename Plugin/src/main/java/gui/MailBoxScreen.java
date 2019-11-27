package gui;

import answers.Answer;
import answers.AnswerList;
import answers.AnswerTableModel;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import config.Constants;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static javax.swing.JOptionPane.showMessageDialog;

public class MailBoxScreen {

    private AnswerTableModel answerTableModel;
    private JBTable answerOverviewTable;

    private AnswerList rowList;
    private JPanel mailBoxScreenContent;

    public JTextField noAnswersTextfield;
    private JLabel openRequestsLabel;
    //26.11.
    private JLabel title;

    private Controller controller;
    public JBScrollPane answerScrollPane;
    private JBScrollPane detailScrollPane;

    private AnswerDetailScreen answerDetailScreen;

    private ToolWindow toolWindow;
    private BalloonPopup balloonPopup;

    public MailBoxScreen(Controller controller, ToolWindow toolWindow) throws IOException {
        this.controller = controller;
        this.toolWindow = toolWindow;
        controller.mailBoxScreen = this;

        balloonPopup = new BalloonPopup();
        //14.10.
        answerDetailScreen = new AnswerDetailScreen(controller.mailBoxScreen, controller);

        rowList = controller.gitHubModel.answerList;

        //TODO: Das noch fixen
        mailBoxScreenContent = new JPanel();
        noAnswersTextfield = new JTextField();
        //19.11.
        openRequestsLabel = new JLabel();
        //26.11. TODO
        title = new JLabel("Alle Antworten");


        noAnswersTextfield.setText("Noch keine Antworten vorhanden.");
        noAnswersTextfield.setEditable(false);
        answerOverviewTable = new JBTable();
        answerOverviewTable.setDragEnabled(false);
        answerOverviewTable.setSelectionBackground(Constants.HEIDENELKENROT);

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
        //19.11.
        openRequestsLabel.setText(Constants.DEFAULT_OPEN_REQUEST_LABEL);
        //23.11.
        mailBoxScreenContent.add(openRequestsLabel);
        mailBoxScreenContent.add(noAnswersTextfield);
        //
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
                        controller.logData("Antwort geöffnet");
                    }
                }
            }
        });
    }


    public void showAnswerDetailContent(Answer answer) {
        mailBoxScreenContent.remove(answerScrollPane);
        mailBoxScreenContent.remove(noAnswersTextfield);
        mailBoxScreenContent.remove(openRequestsLabel);
        mailBoxScreenContent.add(detailScrollPane);
        detailScrollPane.setVisible(true);

        //setCorrectContents();
        answerDetailScreen.detailedAnswerField.setText(answer.getAnswerMessage());
        if(answerDetailScreen.imageButtonList.size() == 0) {
            answerDetailScreen.createImageFromAttachedImageFile(answer.getImageUrls());
        }

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
        mailBoxScreenContent.add(openRequestsLabel);
        mailBoxScreenContent.revalidate();
        controller.logData("Antwortansicht verlassen");
    }


    public void refreshTable() {
        answerTableModel.fireTableDataChanged();
        answerScrollPane.setVisible(true);
        noAnswersTextfield.setVisible(false);
        //TODO: Fixen
        //toolWindow.getContentManager().setSelectedContent(toolWindow.getContentManager().getContent(mailBoxScreenContent));
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


    public void showWelcomeBackInfo() {
        balloonPopup.createBalloonPopup(mailBoxScreenContent, Balloon.Position.above, "Willkommen zurück! Du hast jetzt wieder Zugriff auf deine Antworten.", MessageType.INFO);
    }

    public void showNotification() {
        showMessageDialog(null, "Neue Antwort von Tutor!");
        //TODO: Set selected content auf mailboxscreen setzen
        //26.11. Versuch - TODO noch ausprobieren daheim  jetzt keinen Bock mehr!
       // toolWindow.getContentManager().setSelectedContent(controller.toolWindowFactory.getContentMailBox());
        if(!mailBoxScreenContent.isVisible()) {
            navigateBackToTable();
        }
    }

    public void updateOpenRequest() {
        if(controller.XMLFileReader.readOpenRequestsValueFromXML() == 0) {
            openRequestsLabel.setText("Keine offenen Anfragen.");
        } else {
            openRequestsLabel.setText("Offene Anfragen: " + String.valueOf(controller.XMLFileReader.readOpenRequestsValueFromXML()));
        }
    }
}
