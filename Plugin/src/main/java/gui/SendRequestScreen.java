package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendRequestScreen implements ActionListener {
    private JButton hilfeAnfragenButton;
    private JPanel contentSendRequest;
    private JComboBox comboBox1;
    private JTextArea halloHalloTestTestTextArea;

    public SendRequestScreen() {
        hilfeAnfragenButton.addActionListener(this::actionPerformed);
    }

    public JPanel getContent() {
        return contentSendRequest;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

   
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Buttonklick funktioniert!");
        System.out.println(e.getActionCommand());
        System.out.println(halloHalloTestTestTextArea.getText());
    }


    /*required UI-elements:
        - Textarea (noch abfangen, dass sie nicht leer sein darf!
        - ComboBox (JComboBox)
        - Button (JButton)
        - Titel (JLabel)
        - Text
        - Feld zum Auswählen von Screenshots (JFileChooser)
            - ggf. Liste mit bereits hinzugefügten Dateien
            - allow multiple selection of files
     */

}
