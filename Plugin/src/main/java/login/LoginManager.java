package login;

import com.google.common.hash.Hashing;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import controller.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.UUID;

public class LoginManager {
    private Controller controller;
    public Project project;

    //TODO: hier die komplette Funktionalität

    public LoginManager(Controller controller) {
        this.controller = controller;
        project = controller.project;
    }


    //TODO: Hier vielleicht noch eine andere Library einbinden, damit das nicht mehr "unstable" gemeckert wird
    public String encryptPassword(String password) {
       String sha256hex = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
       System.out.println("SHA-256: " + sha256hex);
       return sha256hex;
    }


    public String createToken() {
        return UUID.randomUUID().toString();
    }

}
