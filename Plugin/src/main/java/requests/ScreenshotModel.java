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
    public String allImagesString;
    public String screenshotLabel;
    public boolean screenshotAttached;

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
                //TODO: Hier komprimieren?
                Arrays.asList(screenshots).forEach(file -> {
                    if (file.isFile()) {
                        System.out.println(file.getName());
                    }
                });
            }
            //Wenn es Screenshots gibt, dann Ordner erstellen und Screenshots hinzufügen

            if (screenshots.length > 0) {
               hasScreenShotAttached();
                screenshotLabel = "screenshot angehängt";
                addScreenshotsToBranch();
                addScreenshotsToIssue(screenshots);
            } else {
                screenshotLabel = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO: Hier Balloon machen

    }

    public boolean hasScreenShotAttached() {
        screenshotAttached = true;
        return true;
    }

    public String getScreenshotLabel() {
        return screenshotLabel;
    }

    //Hier das Bild zusammenwurschteln und das dann dem Issue mitgeben! --> Im Controller!
    public String addScreenshotsToIssue(File [] screenshots) {
        String imageUrl = ""; // Das wird spannend
        String image;
        if(screenshots.length > 0) {
            for (int i = 0; i < screenshots.length; i++) {
                File screenshot = screenshots[i];
                String fileName = screenshot.getName();
                String imageName = fileName.substring(0, fileName.lastIndexOf('.'));
                System.out.println("Imagename: " + imageName);
                //TODO: Das vllt ins GitModel auslagern um zu tricksen?
                String linkToImage = controller.gitModel.createImageUrl();
                System.out.println(linkToImage);
                //String fileExtension = fileName.substring(fileName.lastIndexOf('.'));
               // System.out.println("Endung: " + fileExtension);
                //https://github.com/OOP-Feedback-Test/OOP-Feedback/blob/branch-zdt-2-2810-2107/screenshots/testBild.jpg
                //imageUrl = "https://user-images.githubusercontent.com/" + linkToImage + fileName;
                imageUrl = "https://raw.githubusercontent.com/" + linkToImage + fileName;
                image = "![" + imageName + "]" + "(" + imageUrl + ")" + "\n";
                allImagesString += image;
            }
        }
        System.out.println(allImagesString);
        return allImagesString;
    }

    public String getAllImagesString() {
        return allImagesString;
    }

    public void addScreenshotsToBranch()  {
       screenshotFolder = new File( controller.gitModel.projectPath + Constants.SCREENSHOT_FOLDER);
        screenshotFolder.mkdir();

        for (File file : screenshots) {
            System.out.println(controller.gitModel.projectPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            File screenshot = new File(controller.gitModel.projectPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            try {
                FileUtils.copyFile(file, screenshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
