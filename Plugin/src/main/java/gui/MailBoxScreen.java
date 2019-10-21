package gui;

import actions.BalloonPopup;
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

    private int row;
    private String tutorName;


    public MailBoxScreen(Controller controller) {
        this.controller = controller;
        controller.mailBoxScreen = this;
        //14.10.
        answerDetailScreen = new AnswerDetailScreen(controller.mailBoxScreen, controller);

        rowList = controller.gitHubModel.answerList;

        //TODO: Das noch fixen
        mailBoxScreenContent = new JPanel();
        noAnswersTextfield = new JTextField();
        noAnswersTextfield.setText("Noch keine Antworten vorhanden.");
        noAnswersTextfield.setEditable(false);
        answerOverviewTable = new JBTable();
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
                if (e.getClickCount() == 1 || e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    row = target.getSelectedRow();
                    //TODO: Das geht nur beim ersten Mal! Fixen
                    if(row >= 0) {
                        //TODO 16.10.
                        //TODO: Soll NUR auf angeklickter Reihe funktionieren
                        System.out.println("Diese Zeile wurde geklickt: " + row);

                        showAnswerDetailContent();
                    }
                }
            }
        });
    }

    private void showAnswerDetailContent() {
        mailBoxScreenContent.remove(answerScrollPane);
        mailBoxScreenContent.remove(noAnswersTextfield);
        mailBoxScreenContent.add(detailScrollPane);
        detailScrollPane.setVisible(true);
        setCorrectContents();
        mailBoxScreenContent.revalidate();
    }

    //TODO: Hier auch noch mit Parametern arbeiten?!
    //title, message, Code?
    private void setCorrectContents() {
        //TODO: Antwortzelle auslesen. Das funktioniert schon mal -->
        // TODO: Das über ArrayList machen (Tabellenzeilen sind im UI verschiebbar)
        String answerMessageCellContent = (String) answerOverviewTable.getValueAt(row, 0);
        System.out.println(answerOverviewTable.getValueAt(row, 0));
        answerDetailScreen.detailedAnswerField.setText(answerMessageCellContent);
        createAnswerDetailTitle();
    }

    //TODO: Refactoren ? --> Das muss noch funktionieren
    private void createAnswerDetailTitle() {
        String answerTitle = "";
        tutorName = (String) answerOverviewTable.getValueAt(row, 1);
        if(!tutorName.equals("")) {
            answerTitle = Constants.ANSWER_TITLE_BEGINNING + tutorName;
            answerDetailScreen.answerTitleLabel.setText(answerTitle);
        } else {
            answerTitle = Constants.ANSWER_TITLE_BEGINNING;
            answerDetailScreen.answerTitleLabel.setText(answerTitle);
        }
    }

    public void navigateBackToTable(ActionEvent actionEvent) {
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
        //TODO: Überlegen, ob man nicht doch beim MessageDialog bleibt, weil der so schön penetrant ist und erst weggeht,
        // wenn was geklickt wurde und die IDE unten auch noch blinkt.
        BalloonPopup stickyBalloonPopup = new BalloonPopup();
        stickyBalloonPopup.createStickyBalloonPopup(mailBoxScreenContent, Balloon.Position.above, "Neue Antwort von Tutor", MessageType.WARNING);
        //showMessageDialog(null, "Neue Antwort von Tutor!");
        //TODO: Das ist nicht die ideale Lösung, aber momentan funktioniert sie --> Sticky Balloons (Marlena fragen und Link anschauen)
        //Also am besten einen Listener auf die Tabellenreihe machen, damit die Notification wieder verschwindet
    }

}

