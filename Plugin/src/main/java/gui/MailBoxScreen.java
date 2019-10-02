package gui;

import requests.Answer;
import requests.GitHubModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MailBoxScreen implements ActionListener {
    //TODO: Button für Actionlistener, Debugzwecke
    private GitHubModel gitHubModel;
    private JPanel mailBoxScreenContent;
    private JButton testButton;
    private JList answerList;

    public MailBoxScreen() {
        gitHubModel = new GitHubModel();
        //30.09. 20:21 Test --> Geschlossene Issues sind ja Antworten! Das muss hier wiederverwendet werden, irgendwie als Modell
        //answerList = (JBList)GitHubModel.closedIssueList;
        testButton.addActionListener(this::actionPerformed);

    }

    public JPanel getContent() {
        return mailBoxScreenContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Hier sollte dann etwas cooles passieren!");
        gitHubModel.getClosedIssueList();
        //gitHubModel.getStateOfAllSentRequests();
        //gitHubModel.getClosedIssueList();
    }
    //TODO: Liste dynamisch erstellen
    //For-Schleife? //Array-List?
    //if answers.size() > 0
    //Wenn neue Antwort kam, dann soll der Liste etwas hinzugefügt werden (add()).
   /* https://www.comp.nus.edu.sg/~cs3283/ftp/Java/swingConnect/tech_topics/jlist_1/jlist.html
   In the following code snippet, we create a JList that displays the strings in a data[] array:

    //String[] data = {"one", "two", "free", "four"};
   //JList dataList = new JList(data);*/
    //TODO: ArrayList erstellen
    //TODO: Wenn hier etwas geklickt wird, soll der "AnswerDetailScreen" aufgehen
    //Wo muss diese Methode aufgerufen werden? Im Controller?
    public void createAnswersList() {
        ArrayList<Answer> answers = new ArrayList<Answer>();


    }





}
