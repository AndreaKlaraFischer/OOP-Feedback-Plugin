package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SendRequestScreen implements ActionListener{
    private JButton submitRequestButton;
    private JPanel contentSendRequest;
    private JComboBox selectCategory;
    private JTextArea inputMessageArea;
    private JTextField hierNochEineKurzeTextField;

    //Mit dem Branch erstellen sollen auch die Bilder mitgeschickt werden, Unterverzeichnis "Screenshot"
    //Java File API
    //Ordner richtig erstellen

    //Beschreiben, wie man Screenshots macht (Screenshot Tool, Druck-Taste)
    //UIManager.setLookAndFell(UIManager.getSystemLookAndFeelClassName());
    //chooser.getSelectedFile().getAbsolutePath());

    //TODO: Bookmarks, übertragen in das Repo (wo werden Bookmarks gespeichert (Projektdatei?)
    //Bookmarks setzen: F11

    public SendRequestScreen() {
        submitRequestButton.addActionListener(this::actionPerformed);
        selectCategory.addActionListener(this::actionPerformed);
    }

    public JPanel getContent() {
        return contentSendRequest;
    }

    //MAC Adresse plus UUID
    //im Plugin an geeigneter Stelle merken
    //verwenden für Branch und Issue


    @Override
    public void actionPerformed(ActionEvent e) {
        String inputMessage = inputMessageArea.getText().trim();
        //Abfragen, ob Nachricht leer ist!
        //Kategorie muss auch verpflichtend sein
        if(inputMessage.length() > 0) {
            getCategory(e);
            System.out.println(e.getActionCommand());
            System.out.println(inputMessageArea.getText());
            //StudentRequestModel.addRequest();
        } else {
            //Fehlermeldung
            //TODO: Wie bekomme ich das ins Interface?
            System.out.println("Feld darf nicht leer sein!");
        }

    }

    private void getCategory(ActionEvent e) {
        String category = selectCategory.getSelectedItem().toString();
        if(category != null) {
            System.out.println(category);
        }
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
