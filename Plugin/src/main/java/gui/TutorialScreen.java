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
    private JTextPane platzhalterText;
    private JButton testi;
    private JList list1;
    private Controller controller;
    //Testi
    private LoginMenu loginMenu;
    //testi 5.11.
    public LoginMenu1 loginMenu1;
    public File srcFolder;
    public String srcFolderPath;
    private String clonedRepoFolderPath;

    public TutorialScreen(Controller controller) throws BadLocationException {
        this.controller = controller;
        testi.addActionListener(this::actionPerformed);
        loginMenu1 = new LoginMenu1();
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
        //Das hier soll bei den Klick auf den "zum Code" Button aufgerufen werden!
        //TODO: Deswegen wird es andauernd ausgeführt, weil er bei jedem Buttonklick die ArrayList wieder befüllt.
        //TODO: Muss natürlich über den Controller aufgerufen werden und auch nur einmal!
        //19.11. Erster Versuch mit einem hardgecodeten Branch
        //TODO: Das muss dann rechtzeitig wieder gelöscht werden! Damit bei der nächsten Antwort wieder der Branch gepullt werden kann. Aber erst, wenn eine Antwort da ist.

        controller.gitModel.cloneBranch("refs/heads/Branch-JamesPotter-1-1911-1217");
        try {
            //20.11. Zu Testzwecken
            controller.modifiedFilesReader.listAllFiles(clonedRepoFolderPath);
           // controller.modifiedFilesReader.matchFiles( controller.modifiedFilesReader.listAllFiles(srcFolderPath), controller.gitModel.gitStatus());
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (GitAPIException ex) {
            ex.printStackTrace();
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        //controller.gitModel.compareBranchesForCodeChanges();
        // } catch (GitAPIException | IOException | BadLocationException ex) {
        //controller.annotatedCodeModel.showAnnotatedCodeWindow();
       try {
           //TODO: Hier muss noch was anderes übergeben werden! gitStatus gibt modifiedFileNames zurück.
            controller.annotatedCodeWindow.createWindow( controller.modifiedFilesReader.matchFiles(controller.modifiedFilesReader.listAllFiles(srcFolderPath), controller.gitModel.gitStatus()));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        } catch (GitAPIException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
