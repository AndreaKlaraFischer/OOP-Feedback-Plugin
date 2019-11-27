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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginManager {
    private Controller controller;
    public Project project;

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

    public boolean validateMail(String mailAddressInput) {
        String emailRegex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher emailMatcher = emailPattern.matcher(mailAddressInput);
        return emailMatcher.find();
    }

    public boolean checkPassword() {
        boolean passwordIsCorrect = false;
        String password = controller.getPasswordLogin();
        System.out.println("password");
        String encryptedPasswordLogin = encryptPassword(password);
        System.out.println("encryptedPasswordLogin: " + encryptedPasswordLogin);
        String encryptedPasswordXML = controller.XMLFileReader.readEncryptedPasswordFromXML();
        System.out.println("encryptedPasswordXML" + encryptedPasswordXML);

        if (encryptedPasswordLogin.equals(encryptedPasswordXML)) {
            passwordIsCorrect = true;
        }
        return passwordIsCorrect;
    }
}
