package requests;

import communication.GitModel;
import config.Constants;
import controller.Controller;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ScreenshotModel {

    public static File[] screenshots;
    public File screenshotFolder;
    public Controller controller;

    /*public ScreenshotModel(StudentRequestModel studentRequestModel) {
        this.studentRequestModel = studentRequestModel;
    }*/
    public ScreenshotModel(Controller controller) {
        this.controller = controller;
        //gitModel = controller.gitModel;
    }

    public void chooseFiles() {
        try {
            //https://www.mkyong.com/swing/java-swing-jfilechooser-example/
            //Damit der FileChooser so aussieht wie im jeweiligen Betriebssystem und nicht wie der von IntelliJ
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            JFileChooser screenshotUploader = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            screenshotUploader.setDialogTitle("Screenshots auswählen");
            screenshotUploader.setMultiSelectionEnabled(true);
            screenshotUploader.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = screenshotUploader.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                screenshots = screenshotUploader.getSelectedFiles();
                System.out.println("Files Found\n");
                Arrays.asList(screenshots).forEach(file -> {
                    if (file.isFile()) {
                        System.out.println(file.getName());
                    }
                });
            }
            //Wenn es Screenshots gibt, dann Ordner erstellen und Screenshots hinzufügen
            if (screenshots.length > 0) {
                addScreenshotsToBranch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addScreenshotsToBranch()  {
        screenshotFolder = new File(controller.gitModel.clonedRepoPath + Constants.SCREENSHOT_FOLDER);
        screenshotFolder.mkdir();

        for (File file : screenshots) {
            System.out.println(controller.gitModel.clonedRepoPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            File screenshot = new File(controller.gitModel.clonedRepoPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            try {
                FileUtils.copyFile(file, screenshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //FileUtils.copyFile(screenshots[i], screenshot);
            System.out.println("Schwurbel" + screenshot);
        }
    }


    public void deleteScreenshotFolder() {
        try {
            FileUtils.cleanDirectory(screenshotFolder);
            FileUtils.deleteDirectory(screenshotFolder);
        } catch (Exception e) {
            System.out.println("Ordner löschen " + e.toString());
        }
    }
}
