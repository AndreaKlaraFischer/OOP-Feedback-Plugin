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

    public void modifyGitignoreFile() throws IOException {
        //https://stackoverflow.com/questions/4614227/how-to-add-a-new-line-of-text-to-an-existing-file-in-java
        Writer output;
        output = new BufferedWriter(new FileWriter(controller.project.getBasePath() + Constants.GIT_IGNORE_PATH, true));
        output.append("\n" + "/tutor_comments");
        output.close();


        /*try {
            File f1 = new File(controller.project.getBasePath() + Constants.GIT_IGNORE_PATH);
            FileReader fr = new FileReader(f1);
            BufferedReader br = new BufferedReader(fr);
            while ((line = br.readLine()) != null) {
                System.out.println("line, sollte tutor_comments sein: " + line);
            if (line.contains("workspace.xml"))
              //line = line.replace("java", " ");
            lines.add("\n" + line);
           }
            fr.close();
            br.close();

            FileWriter fw = new FileWriter(f1);
            BufferedWriter out = new BufferedWriter(fw);
            for (String s : lines)
                out.write(s);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}