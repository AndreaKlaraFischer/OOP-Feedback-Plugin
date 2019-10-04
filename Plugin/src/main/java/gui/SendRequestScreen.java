package gui;

import com.intellij.openapi.project.Project;
import config.Constants;
import org.kohsuke.github.GHIssue;
import requests.IDCreator;
import requests.StudentRequestModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class SendRequestScreen implements ActionListener{
    private JButton submitRequestButton;
    private JPanel contentSendRequest;
    private JComboBox selectCategory;
    private JTextArea inputMessageArea;
    private JTextField introductionText;
    private JButton selectFileButton;
    //28.09. Versuch, öffentlich, damit ich aus dem StudentRequestModel darauf zugreifen kann
    public static String inputMessage;
    public static String problemCategory;
    //03.10. Hier werden alle ausgewählten Screenshots gespeichert. Diese müssen ja nich persistent gespeichert werden, oder?

    private String clonedRepoPath;


    private StudentRequestModel studentRequestModel;
    private IDCreator idCreator;
    private List<GHIssue> issueList;

    //Mit dem Branch erstellen sollen auch die Bilder mitgeschickt werden, Unterverzeichnis "Screenshot"
    //Java File API
    //Ordner richtig erstellen

    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)

    //chooser.getSelectedFile().getAbsolutePath());

    //TODO: Bookmarks, übertragen in das Repo (wo werden Bookmarks gespeichert (Projektdatei?)
    //Bookmarks setzen: (Strg + )F11

    public SendRequestScreen(Project project) {
        this.studentRequestModel = new StudentRequestModel(project);
        this.idCreator = new IDCreator();

        submitRequestButton.addActionListener(this);
        selectCategory.addActionListener(this::getCategory);
        selectFileButton.addActionListener(this::openFileSelector);

        clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
    }

    public JPanel getContent() {
        return contentSendRequest;
    }

    //TODO: sollte eigentlich auch noch ausgelagert werden in ein Model --> keine Funktionen in der View
    private void openFileSelector(ActionEvent actionEvent)  {
        studentRequestModel.chooseFiles();
    }

    //MAC Adresse plus UUID, im Plugin an geeigneter Stelle merken, verwenden für Branch und Issue

    //TODO: Model / View / Controller --> ActionListener in Model auslagern?
    //TODO: Refactoren: Die if-Bedingung als Methode und dann auslagern
    //https://javabeginners.de/Design_Patterns/Model-View-Controller.php
    @Override
    public void actionPerformed(ActionEvent e) {
        inputMessage = inputMessageArea.getText().trim();
        getCategory(e);
        if(inputMessage.length() == 0) {
            showMessageDialog(null, "Feld darf nicht leer sein!");
        } else if (SettingScreen.studentNameInput.length() == 0) {
            showMessageDialog(null, "Bitte zuerst einen Namen eingeben!");
        }  else{
            studentRequestModel.sendRequest();
            showMessageDialog(null, "Anfrage wurde abgeschickt!");
        }
    }

    //TODO: Diese Methode noch auslagern, die hat in der View nichts zu suchen
    private void getCategory(ActionEvent e) {
        problemCategory = Objects.requireNonNull(selectCategory.getSelectedItem()).toString();
        if(problemCategory != null) {
            System.out.println(problemCategory);
        }
    }

}