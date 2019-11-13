package answers;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.BorderLayout;

import controller.Controller;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

//https://github.com/bobbylight/RSyntaxTextArea
public class TextEditorDemo extends JFrame {
    private JPanel panel;
    private RSyntaxTextArea textArea;
    private Controller controller;
    private RSyntaxDocument document;

    public TextEditorDemo(Controller controller) throws BadLocationException {
        this.controller = controller;
    }


    public RSyntaxTextArea createCodeWindow(String code) throws BadLocationException {
        JPanel panel = new JPanel(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        // TODO Anschauen
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
