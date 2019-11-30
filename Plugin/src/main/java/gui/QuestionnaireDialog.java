public class QuestionnaireDialog extends javax.swing.JDialog {
private javax.swing.JPanel contentPane;
private javax.swing.JButton buttonOK;
private javax.swing.JButton buttonCancel;

public QuestionnaireDialog(){
setContentPane(contentPane);
setModal(true);
getRootPane().setDefaultButton(buttonOK);
}

public static void main(String[] args){
QuestionnaireDialog dialog = new QuestionnaireDialog();
dialog.pack();
dialog.setVisible(true);
System.exit(0);
}
}
