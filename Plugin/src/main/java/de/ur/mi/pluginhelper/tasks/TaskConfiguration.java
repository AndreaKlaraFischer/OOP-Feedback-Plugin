package de.ur.mi.pluginhelper.tasks;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class TaskConfiguration {

    private static final String HANDOUT_URL_KEY = "HANDOUT_URL";
    private static final String HANDOUT_URL_DEFAULT_VALUE = "https://github.com/esolneman/OOP-Helper-Handout-Template";
    private static final String TASK_TITLE_KEY = "TASK";
    private static final String TASK_TITLE_DEFAULT_VALUE = "Demo Task";
    private static final String DEFAULT_CONFIGURATION_NAME = ".task";

    private String handoutURL;
    private String taskTitle;

    private TaskConfiguration() {
    }

    public void setHandoutURL(String handoutURL) {
        this.handoutURL = handoutURL;
    }

    public String getHandoutURL() {
        return handoutURL;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public static TaskConfiguration loadFrom() {
        return loadFrom(DEFAULT_CONFIGURATION_NAME);
    }

    public static TaskConfiguration loadFrom(String fileName) {
        File configFile = getConfigurationFile(fileName);
        TaskConfiguration config = createConfiguration(configFile);
        return config;
    }

    private static File getConfigurationFile(String fileName) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        Path configFilePath = Paths.get(project.getBasePath(), fileName);
        return configFilePath.toFile();
    }

    private static TaskConfiguration createConfiguration(File input) {
        Properties properties = new Properties();
        TaskConfiguration config = new TaskConfiguration();
        try {
            properties.load(new FileInputStream((input)));
            String taskTitle = properties.getProperty(TASK_TITLE_KEY, TASK_TITLE_DEFAULT_VALUE);
            String handoutURL = properties.getProperty(HANDOUT_URL_KEY, HANDOUT_URL_DEFAULT_VALUE);
            config.setTaskTitle(taskTitle);
            config.setHandoutURL(handoutURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public String toString() {
        String out = "TaskConfiguration:" +
                "\nTask:\t" + this.getTaskTitle() +
                "\nHandout-URL:\t" + this.getHandoutURL();
        return out;
    }
}
