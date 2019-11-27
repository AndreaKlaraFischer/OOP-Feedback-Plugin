package gui;

import android.util.Log;
import controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

public class LoginScreen implements ActionListener {

    private Controller controller;
    private JPanel sendRequestScreenContent;
    private JButton loginButton;

    public LoginScreen(Controller controller) {
        this.controller = controller;
        loginButton.addActionListener(this);
    }

    public JPanel getContent() {
        return sendRequestScreenContent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            controller.showWelcomeMenu();
            controller.updateToolWindow();
            //showWelcomeBackInfo();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

}
