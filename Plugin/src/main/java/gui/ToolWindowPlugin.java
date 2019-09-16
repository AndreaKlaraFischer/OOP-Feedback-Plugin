package gui;

import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.ui.ComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class ToolWindowPlugin implements ActionListener{
    private JButton submitRequestButton;
    private JPanel toolWindowContent;
    //private JPanel contentAnswerScreen; //test
   // private JPanel contentFAQScreen;
    private JLabel title;
    private JTextArea texti;


    /*Each ToolWindow can contain multiple contents --> Das brauche ich für das Anzeigen im ersten Schritt für das zweistufige Interface,
    * Und dann noch für das Anzeigen der Antworten
    * Idee: Klicken auf das Plugin Icon geht der Posteingang auf und die Anfragen etc werden im ToolWindow angezeigt?*/
    public ToolWindowPlugin(ToolWindow toolwindow) {
       submitRequestButton.addActionListener(this::actionPerformed);
    }

    //TODO: verschiedene Contents anzeigen lassen!
    // switch?
    // Sinnvoll: für jedes Panel eine eigene Klasse und ein .form file
    // TODO: Herausfinden --> wie bekomme ich es dann in die ToolWindow Klasse?
    //
    public JPanel getContent() {
       return toolWindowContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // ActionEvent anschauen!
        System.out.println("Buttonklick funktioniert!");
        System.out.println(e.getActionCommand());
        System.out.println(texti.getText());
        System.out.println("yay!");
    }
}
