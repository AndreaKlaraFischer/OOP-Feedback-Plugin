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
import java.util.Objects;

public class ScreenshotModel {

    private File[] screenshots;
    public File screenshotFolder;
    public Controller controller;
    private String allImagesString;
    private boolean screenshotAttached;
   public  ArrayList<String> screenshotTitles;

    public ScreenshotModel(Controller controller) {
        this.controller = controller;
        screenshotTitles = new ArrayList<>();
    }

    public File[] chooseFiles() {
        try {
            //https://www.mkyong.com/swing/java-swing-jfilechooser-example/
            JFileChooser screenshotUploader = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            screenshotUploader.setDialogTitle("Screenshots mitschicken");
            screenshotUploader.setMultiSelectionEnabled(true);
            screenshotUploader.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnValue = screenshotUploader.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                screenshots = screenshotUploader.getSelectedFiles();
                Arrays.asList(screenshots).forEach(file -> {
                    if (file.isFile()) {
                        screenshotTitles.add(file.getName());
                    }
                });
            }

            //Wenn es Screenshots gibt, dann Ordner erstellen und Screenshots hinzufügen
            if (screenshots.length > 0) {
                screenshotAttached = true;
                addScreenshotTitlesToLabel();
                addScreenshotsToBranch();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenshots;
    }


    private void addScreenshotTitlesToLabel() {
        controller.sendRequestScreen.attachedScreenshotPanel.setVisible(true);
        controller.sendRequestScreen.attachedScreenshotsLabel.setVisible(true);
        controller.sendRequestScreen.attachedScreenshotsLabel.setText(String.valueOf(controller.screenshotModel.screenshotTitles));
        controller.sendRequestScreen.deleteScreenshotsButton.setVisible(true);

    }

    public boolean hasScreenShotAttached() {
        return screenshotAttached;
    }

    private void addScreenshotsToBranch() {
        String projectPath = controller.project.getBasePath();
        screenshotFolder = new File(projectPath + Constants.SCREENSHOT_FOLDER);
        screenshotFolder.mkdir();

        for (File file : screenshots) {
            File screenshot = new File(projectPath + Constants.SCREENSHOT_FOLDER + "/" + file.getName());
            try {
                FileUtils.copyFile(file, screenshot);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearScreenshotFolder() {
            try {
                FileUtils.cleanDirectory(screenshotFolder);
            } catch (Exception e) {
                e.getStackTrace();
            }
    }

    public void deleteScreenshots() {
        screenshotTitles.clear();
        controller.sendRequestScreen.attachedScreenshotsLabel.setVisible(false);
        controller.sendRequestScreen.deleteScreenshotsButton.setVisible(false);
    }
}
