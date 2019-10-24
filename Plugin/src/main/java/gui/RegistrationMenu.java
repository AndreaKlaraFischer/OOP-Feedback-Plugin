package gui;

import javax.swing.*;

public class RegistrationMenu {
    //TODO: Das im Controller oder in der ToolWindowPluginFactory aufrufen! (aber erstmal in einem testbutton)
    public void showDialog() {

        // Erstellung Array vom Datentyp Object, Hinzufügen der Komponenten
        JLabel introduction = new JLabel();
        JTextField id = new JTextField();
        JButton needId = new JButton();
        needId.setText("Ich brauche noch eine Id");
        Object[] message = {"Hast du schon eine ID oder benutzt du das Plugin zum ersten Mal?", introduction, "Ich habe schon eine ID:", id
               , needId};

        JOptionPane pane = new JOptionPane( message,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        pane.createDialog(null, "Willkommen zum Feedback Plugin!").setVisible(true);

        System.out.println("Eingabe: " + id.getText() + ", " + needId.getText());
        //Das beendet die komplette Anwendung, also das hier noch ändern!
        //System.exit(0);
    }
}
