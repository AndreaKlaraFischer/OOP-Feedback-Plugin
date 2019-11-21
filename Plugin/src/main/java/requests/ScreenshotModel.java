package requests;

import config.Constants;
import controller.Controller;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScreenshotModel {

    private File[] screenshots;
    private File screenshotFolder;
    public Controller controller;
    private String allImagesString;
    private boolean screenshotAttached;
    private ArrayList<String> screenshotTitles;

    public ScreenshotModel(Controller controller) {
        this.controller = controller;
        screenshotTitles = new ArrayList<>();
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
                        screenshotTitles.add(file.getName());
                        System.out.println(file.getName());
                    }
                });
            }
            //Wenn es Screenshots gibt, dann Ordner erstellen und Screenshots hinzufügen
            if (screenshots.length > 0) {
                screenshotAttached = true;
                addScreenshotTitlesToLabel();
                addScreenshotsToBranch();
                addScreenshotsToIssue(screenshots);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        controller.sendRequestScreen.showScreenshotAttachedInfo();
    }

    private void addScreenshotTitlesToLabel() {
        controller.sendRequestScreen.attachedScreenshotsLabel.setText(String.valueOf(controller.screenshotModel.screenshotTitles));
        controller.sendRequestScreen.attachedScreenshotsLabel.setVisible(true);
    }

    public boolean hasScreenShotAttached() {
        return screenshotAttached;
    }

    //Hier das Bild zusammenwurschteln und das dann dem Issue mitgeben! --> Im Controller!
    private String addScreenshotsToIssue(File[] screenshots) {
        String imageUrl = ""; // Das wird spannend
        String image;
        if (screenshots.length > 0) {
            for (File screenshot : screenshots) {
                String fileName = screenshot.getName();
                String imageName = fileName.substring(0, fileName.lastIndexOf('.'));
                System.out.println("Imagename: " + imageName);
                //TODO: ?
                String linkToImage = createImageUrl();
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

    public String createImageUrl() {
        return "OOP-Feedback-Test/OOP-Feedback/blob/" + controller.getBranchName() + "/screenshots/";
    }

    public String getAllImagesString() {
        return allImagesString;
    }

    private void addScreenshotsToBranch() {
        //TODO: Warum Umweg über das gitmodel?- Refactor
        String projectPath = controller.project.getBasePath();
        screenshotFolder = new File(projectPath + Constants.SCREENSHOT_FOLDER);
        screenshotFolder.mkdir();

        for (File file : screenshots) {
            System.out.println(controller.gitModel.projectPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            File screenshot = new File(projectPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            try {
                FileUtils.copyFile(file, screenshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearScreenshotFolder() {
       // if (screenshots.length == 0) {
            try {
                FileUtils.cleanDirectory(screenshotFolder);
            } catch (Exception e) {
                System.out.println("Ordner löschen " + e.toString());
            }
       // }
    }
}
