package de.ur.mi.pluginhelper.User;

import de.ur.mi.pluginhelper.config.PluginConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class User {

    private static final String DEFAULT_USER_FILE_NAME=  "CurrentUser";
    private static final String ID_KEY = "ID";

    private String id;
    private String sessionID;

    private User(File userFile) {
        initFromFile(userFile);
        sessionID = UUID.randomUUID().toString();
    }

    private void initFromFile(File userFile) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream((userFile)));
            this.id = properties.getProperty(ID_KEY, "undefined");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getID() {
        return id;
    }

    public String getSessionID() {
        return sessionID;
    }

    public static User getLocalUser() {
        File userFile = getUserFile();
        if(!userFile.exists()) {
            userFile = createUserFile(userFile);
        }
        return new User(userFile);
    }

    public static void removeLocalUser() {
    }

    private static File getUserFile() {
        File dataPath = getDataPath();
        File userFile = new File(dataPath, DEFAULT_USER_FILE_NAME);
        return userFile;
    }

    private static File createUserFile(File userFile) {
        String id = UUID.randomUUID().toString();
        Properties properties = new Properties();
        properties.setProperty(ID_KEY, id);
        try {
            userFile.createNewFile();
            properties.store(new FileOutputStream(userFile), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userFile;
    }

    private static File getDataPath() {
        File userDir = new File(System.getProperty("user.home"));
        File dataPath = new File(userDir, PluginConfiguration.DEFAULT_DATA_FOLDER);
        if(!dataPath.exists()) {
            dataPath.mkdir();
        }
        return  dataPath;
    }
}
