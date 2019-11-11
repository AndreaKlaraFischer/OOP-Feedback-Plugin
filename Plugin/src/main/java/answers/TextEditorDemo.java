package answers;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.BorderLayout;

import controller.Controller;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

//https://github.com/bobbylight/RSyntaxTextArea
public class TextEditorDemo extends JFrame {
    private JFrame frame;
    private JPanel cp;
    RSyntaxTextArea textArea;
    Controller controller;

    public TextEditorDemo(Controller controller) throws BadLocationException {
        //frame = new JFrame();
        cp = new JPanel(new BorderLayout());
        this.controller = controller;

        textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        // Anschauen
        RSyntaxDocument document = new RSyntaxDocument(SyntaxConstants.SYNTAX_STYLE_JAVA);
        //TODO: Wie bekomme ich dann den Code hier rein?
        //9.11., das ist ein Test und funktioniert
        document.insertString(0, controller.XMLFileReader.getDocString(), null);
        textArea.setDocument(document);
        //Das hier ist wichtig!
        textArea.setEditable(false);
        RTextScrollPane sp = new RTextScrollPane(textArea);
        //Das tut auch nicht das, was ich will
        sp.setLineNumbersEnabled(true);
        cp.add(sp);
        //frame.add(cp);
        setContentPane(cp);
        //setTitle("Text Editor Demo");
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public RSyntaxTextArea showTextEditor() {
        /*SwingUtilities.invokeLater(() -> {
            try {
                new TextEditorDemo().setVisible(true);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        });*/
        return textArea;
    }

}