package answers;

import com.intellij.ui.components.JBTabbedPane;
import controller.Controller;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyEvent;

public class AnnotatedCodeModel {
    private TextEditorDemo textEditorDemo;
    private Controller controller;
    private JBTabbedPane tabbedPane;
    private JFrame codeFrame;

    public AnnotatedCodeModel(Controller controller) throws BadLocationException {
        this.controller = controller;
        textEditorDemo = new TextEditorDemo(controller);
        tabbedPane = new JBTabbedPane();
        codeFrame = new JFrame("Annotierter Code");
    }


    //TODO: Wird bei jedem Buttonklick wieder ausgeführt! --> Fixen, vielleicht dann einfach wieder die Tabs killen? und neu schreiben.
   // https://www.tutorialspoint.com/swingexamples/example_of_tabbed_pane.htm
    public void createWindow() throws BadLocationException {
        createUI(codeFrame);
        codeFrame.setSize(560, 500);
        codeFrame.setLocationRelativeTo(null);
    }

    public void showAnnotatedCodeWindow() {
        codeFrame.setVisible(true);
    }

    private void createUI(final JFrame codeFrame) throws BadLocationException {
        tabbedPane = new JBTabbedPane();
       //12.11. Versuch, ob ich so dynamisch Tabs erstellen kann
        //TODO: Vielleicht hier nochmal eine for-schleife, die durch die DiffEntrys iteriert? Oder daraus noch eine Liste machen?
        for(int i = 0; i < controller.gitModel.modifiedFiles.size(); i++) {
            //TODO: Das geht nicht. Die Reihenfolge stimmt da nicht. Ich muss mir den Titel woanders her holen.
           //for (int j = 0; j < controller.gitModel.diffEntries.size(); j++) {
               System.out.println("diggi entries in create ui: " + controller.gitModel.diffEntries);
               createTab(controller.gitModel.modifiedFiles.get(i), "testiString");
               //createTab(controller.gitModel.modifiedFiles.get(i), "testiaiaiaiai");
               //createTab("testiaiaiaiai", controller.gitModel.modifiedFiles.get(j));
           //}
         }
       codeFrame.getContentPane().add(tabbedPane,BorderLayout.CENTER);
    }

    private void createTab(String title, String code) throws BadLocationException {
        JPanel panel = new JPanel(false);
        //TODO: Dem hier noch den modifiedCode übergeben! Als dynamisch generierten Inhalt
        RSyntaxTextArea filler = textEditorDemo.createCodeWindow(code);
        JLabel viewInfoLabel = new JLabel();
        viewInfoLabel.setText("Du befindest dich in der Ansicht des vom Tutor bearbeiteten Code.");
        panel.setLayout((new GridLayout(1,1)));
        panel.add(filler);
        panel.add(viewInfoLabel);
        tabbedPane.addTab(title, null, panel);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    }
}
