package gui;

import com.intellij.openapi.project.Project;
import requests.IDCreator;
import requests.StudentRequestModel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import static javax.swing.JOptionPane.showMessageDialog;

public class SendRequestScreen implements ActionListener{
    private JButton submitRequestButton;
    private JPanel contentSendRequest;
    private JComboBox selectCategory;
    private JTextArea inputMessageArea;
    private JTextField introductionText;
    private JButton uploadi;
    //28.09. Versuch, öffentlich, damit ich aus dem StudentRequestModel darauf zugreifen kann
    public static String inputMessage;
    public static String problemCategory;
    JFileChooser screenshotUploader;


    private StudentRequestModel studentRequestModel;
    private IDCreator idCreator;

    //Mit dem Branch erstellen sollen auch die Bilder mitgeschickt werden, Unterverzeichnis "Screenshot"
    //Java File API
    //Ordner richtig erstellen

    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)
    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    //chooser.getSelectedFile().getAbsolutePath());

    //TODO: Bookmarks, übertragen in das Repo (wo werden Bookmarks gespeichert (Projektdatei?)
    //Bookmarks setzen: (Strg + )F11

    public SendRequestScreen(Project project) {
        this.studentRequestModel = new StudentRequestModel(project);
        this.idCreator = new IDCreator();


        submitRequestButton.addActionListener(this::actionPerformed);
        selectCategory.addActionListener(this::actionPerformed);
        uploadi.addActionListener(this::openFileSelector);
    }

    private void openFileSelector(ActionEvent actionEvent) {
        //https://www.mkyong.com/swing/java-swing-jfilechooser-example/
        JFileChooser screenshotUploader = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        screenshotUploader.setDialogTitle("TESTI");
        screenshotUploader.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = screenshotUploader.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if(screenshotUploader.getSelectedFile().isDirectory()) {
                System.out.println("Deine gewählte Datei ist: " + screenshotUploader.getSelectedFile());
            }
        }
    }

    public JPanel getContent() {
        return contentSendRequest;
    }

    //MAC Adresse plus UUID, im Plugin an geeigneter Stelle merken, verwenden für Branch und Issue


    //TODO: Model / View / Controller --> ActionListener in Model auslagern?
    //https://javabeginners.de/Design_Patterns/Model-View-Controller.php
    @Override
    public void actionPerformed(ActionEvent e) {
        inputMessage = inputMessageArea.getText().trim();
        //Abfragen, ob Nachricht leer ist!
        if(inputMessage.length() > 0) {
            getCategory(e);
            System.out.println(e.getActionCommand());
            System.out.println(inputMessageArea.getText());
            studentRequestModel.sendRequest();
        } else {
            //Fehlermeldung
            showMessageDialog(null, "Feld darf nicht leer sein!");
            System.out.println("Feld darf nicht leer sein!");
        }
    }

    //TODO: Diese Methode noch auslagern, die hat in der View nichts zu suchen
    public void getCategory(ActionEvent e) {
        problemCategory = Objects.requireNonNull(selectCategory.getSelectedItem()).toString();
        if(problemCategory != null) {
            System.out.println(problemCategory);
        }
    }


    /*required UI-elements:
        - Feld zum Auswählen von Screenshots (JFileChooser)
            - ggf. Liste mit bereits hinzugefügten Dateien
            - allow multiple selection of files
     */

}