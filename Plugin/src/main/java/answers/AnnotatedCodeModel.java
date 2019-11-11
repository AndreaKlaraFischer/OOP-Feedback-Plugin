package answers;

import com.intellij.ui.components.JBTabbedPane;
import controller.Controller;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//TODO: GEh
public class AnnotatedCodeModel {
    private TextEditorDemo textEditorDemo;
    private Controller controller;
    private ArrayList<String> tabNames = new ArrayList<>();
    JBTabbedPane tabbedPane;

    public AnnotatedCodeModel(Controller controller) throws BadLocationException {
        this.controller = controller;
        textEditorDemo = new TextEditorDemo(controller);
        tabNames.add("Testi");
        tabNames.add("Turbi");
        tabNames.add("Schwurbi");
        tabbedPane = new JBTabbedPane();
    }

    public void showAnnotatedCode() throws BadLocationException {

    }


   // https://www.tutorialspoint.com/swingexamples/example_of_tabbed_pane.htm
    public void createWindow() {
        JFrame codeFrame = new JFrame("Annotierter Code");
        createUI(codeFrame);
        codeFrame.setSize(560, 500);
        codeFrame.setLocationRelativeTo(null);
        codeFrame.setVisible(true);
    }

    //TODO bzw Idee: Das dynamisch machen --> Kann man tabs als Liste machen? Mit For-Schleife? For() {addTab;}
    private void createUI(final JFrame codeFrame){

        //TODO: RSyntaxArea innerhalb der Tabs machen
        //Idee: Tabs vertikal machen, da vertikales Scrollen Standard ist (für den Fall, dass viele Datein geändert werden)
        //Idee: Baumdarstellung (JTree)
        //Auf linker Seite JTree, auf rechter Seite Texteditor
        //TODO: Label, dass man sich jetzt gerade Feedback anschaut --> Verortung (zwei verschiedenene Repräsentationen von einem Dokument, sauber trennen, deutlich machen)
        //Unter den Texteditor

        tabbedPane = new JBTabbedPane();

        JPanel panel1 = new JPanel(false);
        //TODO: Hier den Texteditor reinhauen
        RSyntaxTextArea filler = textEditorDemo.showTextEditor();
        JLabel filler2 = new JLabel("Du befindest dich gerade in der Ansicht des annotierten Codes.");

        panel1.setLayout(new GridLayout(1, 1));
        panel1.add(filler, filler2);
        tabbedPane.addTab("Tab 1", null, panel1,"Tab 1 tooltip");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JPanel panel2 = new JPanel(false);
       // JLabel filler2 = new JLabel("Tab 2");
        //filler2.setHorizontalAlignment(JLabel.CENTER);
        panel2.setLayout(new GridLayout(1, 1));
        //panel2.add(filler2);
        tabbedPane.addTab("Tab 2", null, panel2,"Tab 2 tooltip");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);

        codeFrame.getContentPane().add(tabbedPane,BorderLayout.CENTER);

        //TODO: Unter-Methode mit createTab oder sowas, vielleicht ein Interface?
    }

}
