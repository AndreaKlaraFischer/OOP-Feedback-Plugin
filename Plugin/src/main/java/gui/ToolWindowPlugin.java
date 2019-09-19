package gui;

import com.intellij.openapi.wm.ToolWindow;
//import requests.GitHubListener
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//import gui.AnswerScreen;
//import gui.SendRequestScreen;
//import gui.MailBoxScreen;
//import gui.TutorialScreen;ß

public class ToolWindowPlugin implements ActionListener{
    private JButton submitRequestButton;
    //verschiedene Ansichten im ToolWindow
    private JPanel toolWindowContent;
    private JPanel contentAnswerScreen; //test
    private JPanel contentTutorialScreen;
    private JPanel contentMailBoxScreen;
    private JPanel contentSendRequestScreen;
    //Test UI Elemente
    private JLabel title;
    private JTextArea texti;
    private int count = 0;


    /*Each ToolWindow can contain multiple contents --> Das brauche ich für das Anzeigen im ersten Schritt für das zweistufige Interface,
    * Und dann noch für das Anzeigen der Antworten
    * Idee: Klicken auf das Plugin Icon geht der Posteingang auf und die Anfragen etc werden im ToolWindow angezeigt?*/
    public ToolWindowPlugin(ToolWindow toolwindow) {
        submitRequestButton.setToolTipText("Das ist ein ToolTip-Test");
        submitRequestButton.addActionListener(this::actionPerformed);
    }

    //TODO: verschiedene Contents anzeigen lassen!
    // switch?
    // Sinnvoll: für jedes Panel eine eigene Klasse und ein .form file
    // TODO: Herausfinden --> wie bekomme ich es dann in die ToolWindow Klasse?
    //
    public JPanel getContent() {
        if(count == 0) {
            count++;
            return toolWindowContent;
        }
       return contentAnswerScreen;

    }

    //Bleibt der hier oder wird auch der ActionListener ausgelagert in die
    // Sendrequest Klasse?
    @Override
    public void actionPerformed(ActionEvent e) {
        // ActionEvent anschauen!
        //TODO: Request abschicken.
        //StudentRequestModel.addRequest();

        System.out.println("Buttonklick funktioniert!");
        System.out.println(e.getActionCommand());
        System.out.println(texti.getText());
        System.out.println("yay!");
    }


}
