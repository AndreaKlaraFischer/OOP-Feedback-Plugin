package gui;

import com.intellij.openapi.project.Project;
import controller.Controller;
import org.kohsuke.github.GHIssue;
import requests.IDCreator;
import requests.ScreenshotModel;
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
    public JComboBox selectCategory;
    private JTextArea inputMessageArea;
    private JTextField introductionText;
    private JButton selectFileButton;
    //28.09. Versuch, öffentlich, damit ich aus dem StudentRequestModel darauf zugreifen kann
    public static String inputMessage;
    public static String problemCategory;
    //03.10. Hier werden alle ausgewählten Screenshots gespeichert. Diese müssen ja nich persistent gespeichert werden, oder?

    public String clonedRepoPath;

    public Controller controller;
    private StudentRequestModel studentRequestModel;
    private ScreenshotModel screenshotModel;
    private IDCreator idCreator;
    private List<GHIssue> issueList;
    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)

    //TODO: Bookmarks, übertragen in das Repo (wo werden Bookmarks gespeichert (Projektdatei?)
    //Bookmarks setzen: (Strg + )F11

    public SendRequestScreen(Project project, Controller controller) {
        this.controller = controller;
        this.studentRequestModel = new StudentRequestModel(project, controller);
        this.idCreator = new IDCreator();
        this.screenshotModel = new ScreenshotModel(studentRequestModel);

        submitRequestButton.addActionListener(this);
        selectCategory.addActionListener(this::getCategory);
        selectFileButton.addActionListener(this::openFileSelector);
    }

    public JPanel getContent() {
        return contentSendRequest;
    }


    private void openFileSelector(ActionEvent actionEvent)  {
        screenshotModel.chooseFiles();
    }

    //MAC Adresse plus UUID, im Plugin an geeigneter Stelle merken, verwenden für Branch und Issue

    //TODO: Model / View / Controller --> ActionListener in Model auslagern?
    //https://javabeginners.de/Design_Patterns/Model-View-Controller.php
    @Override
    public void actionPerformed(ActionEvent e) {
        inputMessage = inputMessageArea.getText().trim();
        getCategory(e);
        if(inputMessage.length() == 0) {
            showMessageDialog(null, "Bitte eine Nachricht eingeben");
        } else if (SettingScreen.studentNameInput.length() == 0) {
            showMessageDialog(null, "Bitte zuerst einen Namen eingeben!");
        } else {
            studentRequestModel.sendRequest();
            showMessageDialog(null, "Anfrage wurde abgeschickt!");
        }
    }


    public void getCategory(ActionEvent e) {
        problemCategory = Objects.requireNonNull(selectCategory.getSelectedItem()).toString();
        if(problemCategory != null) {
            System.out.println(problemCategory);
        }
    }


}