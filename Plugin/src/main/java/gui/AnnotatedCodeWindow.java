package gui;

import com.intellij.ui.TabbedPane;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.uiDesigner.core.GridLayoutManager;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AnnotatedCodeWindow extends JFrame {
    private Controller controller;

    public AnnotatedCodeWindow(Controller controller) throws BadLocationException {
        this.controller = controller;
    }

    // https://www.tutorialspoint.com/swingexamples/example_of_tabbed_pane.htm
    //30.11. auskommentiert zu Testzwecken --> Erstmal nur Tab anzeigen
    public void createWindow(List<File> modifiedFiles) throws BadLocationException, GitAPIException, IOException {
        JFrame codeFrame = new JFrame("Annotierter Code");
        createUI(codeFrame, modifiedFiles);
        codeFrame.setSize(560, 600);
        codeFrame.setLocationRelativeTo(null);
        codeFrame.setVisible(true);
    }

     private void createUI(JFrame codeFrame, List<File> modifiedFiles) throws BadLocationException, GitAPIException, IOException {
        JBTabbedPane tabbedPane = new JBTabbedPane();

       for (File modifiedFile : modifiedFiles) {
            createTab(tabbedPane, modifiedFile.getName(), controller.modifiedFilesReader.readCode(modifiedFile.getAbsolutePath()));
        }
        //createTab(tabbedPane, "Testtitel", "Testbody");
        codeFrame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    private void createTab(JBTabbedPane tabbedPane, String title, String code) throws BadLocationException {
        JPanel panel = new JPanel(false);
        RSyntaxTextArea filler = createCodeWindow(code);
        filler.setAutoscrolls(true);
        JLabel viewInfoLabel = new JLabel();
        viewInfoLabel.setText("Du befindest dich in der Ansicht des vom Tutor bearbeiteten Code.");
        panel.setLayout((new GridLayout(2, 1)));
        panel.add(filler);
        panel.add(viewInfoLabel);
        JBScrollPane scrollPane = new JBScrollPane(panel);
        scrollPane.setViewportView(panel);
        tabbedPane.addTab(title, null, scrollPane);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    }

    private RSyntaxTextArea createCodeWindow(String code) throws BadLocationException {
        JPanel panel = new JPanel(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        RSyntaxDocument document = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_JAVA);
        document.insertString(0, code, null);
        textArea.setDocument(document);
        textArea.setEditable(false);
        RTextScrollPane textScrollPane = new RTextScrollPane(textArea);
        panel.add(textScrollPane);
        setContentPane(panel);
        pack();
        setLocationRelativeTo(null);
        return textArea;
    }
}
