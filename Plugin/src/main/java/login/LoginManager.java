package login;

import com.google.common.hash.Hashing;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import controller.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;

public class LoginManager {
    private Controller controller;
    public Project project;

    //TODO: hier die komplette funktionalität
    public LoginManager(Controller controller) {
        this.controller = controller;
        project = controller.project;
    }

    public String encryptPassword(char[] password) {
       String passwordString = password.toString();
       String sha256hex = Hashing.sha256().hashString(passwordString, StandardCharsets.UTF_8).toString();
       System.out.println(sha256hex);
       return sha256hex;
    }


}
