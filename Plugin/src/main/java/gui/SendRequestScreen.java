package gui;

import com.intellij.openapi.project.Project;
import config.Constants;
import org.codehaus.plexus.util.FileUtils;
import org.kohsuke.github.GHIssue;
import requests.IDCreator;
import requests.StudentRequestModel;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
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
    public static File[] screenshots;
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

        submitRequestButton.addActionListener(this::actionPerformed);
        selectCategory.addActionListener(this::getCategory);
        selectFileButton.addActionListener(this::openFileSelector);


        clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
    }

    public JPanel getContent() {
        return contentSendRequest;
    }


    //TODO: sollte eigentlich auch noch ausgelagert werden in ein Model --> keine Funktionen in der View
    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    private void openFileSelector(ActionEvent actionEvent)  {
        try {
        //https://www.mkyong.com/swing/java-swing-jfilechooser-example/
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFileChooser screenshotUploader = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        //UIManager.setLookAndFeel(String.valueOf(screenshotUploader));


        screenshotUploader.setDialogTitle("Screenshots auswählen");
        screenshotUploader.setMultiSelectionEnabled(true);
        screenshotUploader.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = screenshotUploader.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            screenshots = screenshotUploader.getSelectedFiles();
            System.out.println("Files Found\n");
            Arrays.asList(screenshots).forEach(file -> {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            });
        }
        //Wenn es Screenshots gibt, dann Screenshots hinzufügen
        //TODO: Commiten zum funktionieren bringen!
        if(screenshots.length > 0) {
            addScreenshotsToBranch();
        } } catch (Exception e) {

        }

    }
    //TODO: Auslagern
    private void addScreenshotsToBranch(){
        //TODO: hinzufügen und commiten
        try {
            //TODO: Schauen, ob das mit Konstante funktioniert hat
            File screenshotFolder = new File(clonedRepoPath + Constants.SCREENSHOT_FOLDER);
            screenshotFolder.mkdir();
            System.out.println(clonedRepoPath);

            for (int i = 0; i < screenshots.length; i++) {
                    System.out.println(clonedRepoPath + "/screenshots/" + screenshots[i].getName());
                    File screenshot = new File(clonedRepoPath + "/screenshots/" + screenshots[i].getName());

                    FileUtils.copyFile(screenshots[i], screenshot);

                    System.out.println("Schwurbel" + screenshot);
            }
        } catch (Exception e) {

        }
    }

    //TODO: Das noch zu dem Branch hinzufügen mit der ID, Ordner erstellen



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
            System.out.println(e.getActionCommand());
            System.out.println(inputMessageArea.getText());
            studentRequestModel.sendRequest();
            showMessageDialog(null, "Anfrage wurde erfolgreich abgeschickt!");
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