package fileHandler;

import config.Constants;
import controller.Controller;
import org.apache.commons.io.FileUtils;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GitIgnoreModifier {
    private Controller controller;

    public GitIgnoreModifier(Controller controller) {
        this.controller = controller;
    }


    //https://stackoverflow.com/questions/13741751/modify-the-content-of-a-file-using-java
    private List<String> lines = new ArrayList<String>();
    private String line = null;

    //the folder "tutor_comments" has to be added to the .gitignore due to its capacity
    public void modifyGitignoreFile() throws IOException {
        //https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java
        Writer output;
        output = new BufferedWriter(new FileWriter(controller.project.getBasePath() + Constants.GIT_IGNORE_PATH, true));
        output.append("\n" + "/tutor_comments");
        output.close();
    }
}