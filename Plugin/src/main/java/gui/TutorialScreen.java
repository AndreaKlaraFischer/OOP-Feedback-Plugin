package gui;

import config.Constants;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class TutorialScreen implements ActionListener{
    private JPanel tutorialScreenContent;
    private JButton testi;
    private JTextPane umEineAnfrageZuTextPane;
    private JTextPane dieAntwortenBefindenSichTextPane;
    private JTextPane namenUndEmailadresseKönnenTextPane;
    private JTextPane dieStudienleistungKannAuchTextPane;
    private JTextPane nachdemDuEineAntwortTextPane;
    private JTextPane screenshotsKannManEntwederTextPane;
    private JTextPane möchtestDuAnonymFragenTextPane;
    private JTextPane umDieStellenImTextPane;
    private JTextPane textPane6;
    private JTextPane dasPluginKannAuchTextPane;
    private JTextPane sollteDasPasswortVergessenTextPane;
    private Controller controller;
    //Testi
    private LoginMenu loginMenu;
    //testi 5.11.
    public File srcFolder;
    public String srcFolderPath;
    private String clonedRepoFolderPath;
    public LoginMenu1 loginMenu1;

    public TutorialScreen(Controller controller) throws BadLocationException {
        this.controller = controller;
        testi.addActionListener(this);
        srcFolder = new File(controller.project.getBasePath() + Constants.CLONED_SRC_FOLDER);
        srcFolderPath = controller.project.getBasePath() + Constants.CLONED_SRC_FOLDER;
        clonedRepoFolderPath = controller.project.getBasePath() + Constants.CLONED_REPO_FOLDER;

        //textEditorDemo = new TextEditorDemo();
    }


    public JPanel getContent() {
        return tutorialScreenContent;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
     /*   //19.11. Erster Versuch mit einem hardgecodeten Branch

        controller.gitModel.cloneBranch("refs/heads/Branch-JamesPotter-1-1911-1217");
        try {
            //20.11. Zu Testzwecken
            controller.modifiedFilesReader.listAllFiles(clonedRepoFolderPath);
            //TODO: Das hier noch in eine Getter-Methode schreiben, damit ich auf Änderungen lauschen kanN!
           // controller.modifiedFilesReader.matchFiles( controller.modifiedFilesReader.listAllFiles(srcFolderPath), controller.gitModel.gitStatus());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GitAPIException ex) {
            ex.printStackTrace();
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

       try {
           //TODO: Hier muss noch was anderes übergeben werden! gitStatus gibt modifiedFileNames zurück.
            controller.annotatedCodeWindow.createWindow( controller.modifiedFilesReader.matchFiles(controller.modifiedFilesReader.listAllFiles(srcFolderPath), controller.gitModel.gitStatus()));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        } catch (GitAPIException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
     //24.11. LoginMenu tester.
     loginMenu1 = new LoginMenu1(controller);
    }
}
