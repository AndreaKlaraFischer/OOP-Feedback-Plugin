package gui;

import config.Constants;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorialScreen implements ActionListener {
    private JPanel tutorialScreenContent;
    private JTextPane loremIpsumDolorSitTextPane;
    private JButton helpfulButton;
    private JButton semiHelpfulButton;
    private JButton notHelpfulButton;


    public TutorialScreen() {
        helpfulButton.addActionListener(this);
        semiHelpfulButton.addActionListener(this);
        notHelpfulButton.addActionListener(this);
    }

    public JPanel getContent() {
        return tutorialScreenContent;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getSource());
        //Herausfinden, welcher Button geklickt wurde, in Variable speichern
        Object clickedButton = e.getSource();
        if(clickedButton == helpfulButton) {
            setHelpfulFeedbackLabel();
        } else if (clickedButton == semiHelpfulButton) {
            setSemiHelpfulFeedbackLabel();
        } else if (clickedButton == notHelpfulButton) {
            setNotHelpfulFeedbackLabel();
        }
    }

    //TODO: Issues herbekommen!!
    private void setNotHelpfulFeedbackLabel() {
        String notHelpfulLabel = Constants.COMMON_LABEL_BEGIN + "nicht hilfreich";
        System.out.println("NICHT HILFREICH :(");
        //issue.addLabels(notHelpfulLabel);
    }

    private void setSemiHelpfulFeedbackLabel() {
        String semiHelpfulLabel = Constants.COMMON_LABEL_BEGIN + "mittel hilfreich";
        System.out.println("Das war so lala");
        //issue.addLabels(semiHelpfulLabel);
    }

    private void setHelpfulFeedbackLabel() {
        String helpfulLabel = Constants.COMMON_LABEL_BEGIN + "hilfreich";
        System.out.println("Supi, danke für´s Feedback!");
        //issue.addLabels(helpfulLabel);
    }
}
