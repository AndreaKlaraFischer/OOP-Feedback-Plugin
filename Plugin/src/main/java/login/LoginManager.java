package login;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import controller.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class LoginManager {
    private Controller controller;
    public Project project;

    //TODO: hier die komplette funktionalität
    public LoginManager(Controller controller) {
        this.controller = controller;
        project = controller.project;
    }

    public void encryptPassword() {
        //auf Verschlüsselungsklasse zugreifen!
    }

    public void decryptPassword() {

    }

}
