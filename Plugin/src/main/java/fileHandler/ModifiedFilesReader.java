package fileHandler;

import com.intellij.openapi.project.Project;
import controller.Controller;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class ModifiedFilesReader {
    public Controller controller;
    public Project project;

    public List<File> filesSrcFolderLocalRepo;
    public List<File> filesSrcFolderClonedRepo;

    public String srcFolderPathLocalRepo;
    public String srcFolderPathClonedRepo;

    public HashMap<String, File> localFileMap, clonedFileMap;

    public ModifiedFilesReader(Controller controller) {
        this.controller = controller;
        project = controller.project;
    }

    public void saveFilesInHashMaps(String branchName) throws IOException {
        srcFolderPathLocalRepo = project.getBasePath() + "/src";
        srcFolderPathClonedRepo = project.getBasePath() + "/.idea/tutor_comments/" + branchName + "/src";
        System.out.println("srcFolderPathClonedRepo: " + srcFolderPathClonedRepo);

        filesSrcFolderLocalRepo = listAllFiles(srcFolderPathLocalRepo);
        System.out.println(" filesSrcFolderLocalRepo: " + filesSrcFolderLocalRepo);

        filesSrcFolderClonedRepo = listAllFiles(srcFolderPathClonedRepo);
        System.out.println(" filesSrcFolderClonedRepo: " + filesSrcFolderClonedRepo);

        localFileMap = new HashMap<>();
        clonedFileMap = new HashMap<>();

        for (File file : filesSrcFolderLocalRepo) {
            String fileName = getFileNameWithoutPath(file.getName());
            localFileMap.put(fileName, file);
        }
        System.out.println("localFileMap: " + localFileMap);

        for (File file : filesSrcFolderClonedRepo) {
            String fileName = getFileNameWithoutPath(file.getName());
            clonedFileMap.put(fileName, file);
        }
        System.out.println("clonedFileMap: " + clonedFileMap);
    }

    public List<File> collectModifiedFiles() throws IOException {
        //Hallo Mensch der Zukunft, der jetzt hier die Markierungen hinzufügen muss - Ich hab an dich gedacht!
        List<File> modifiedFiles = new ArrayList<>();
        try {
            System.out.println("localFileMap: " + localFileMap);
            System.out.println("clonedFileMap: " + clonedFileMap);
            for (String key : localFileMap.keySet()) {
                System.out.println("key: " + key);
                if (!FileUtils.contentEquals(clonedFileMap.get(key), localFileMap.get(key))) {
                    modifiedFiles.add(clonedFileMap.get(key));
                }
            }
        } catch (NullPointerException e) {
            System.out.println("npe: " + e.getMessage());
        }
        return modifiedFiles;
    }


    //https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders
    //Use ArrayList instead of array as return value
    public List<File> listAllFiles(String directoryName) {
        File srcFolder = new File(directoryName);

        List<File> allFilesList = new ArrayList<>();
        //get all files from a directory
        File[] fileList = srcFolder.listFiles();

        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile() && !file.isDirectory()) {
                    allFilesList.add(file);
                    System.out.println(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    allFilesList.addAll(listAllFiles(file.getAbsolutePath()));
                }
            }
        }
        System.out.println("allFilesList: " + allFilesList);
        return allFilesList;
    }


    //TODO: Überlegen, was passiert, wenn ein Dateiname gar keinen File.seperator drin hat.
    private String getFileNameWithoutPath(String modifiedFileName) {
        System.out.println("File.separator" + File.separator);
        System.out.println("File.seperatorChar" + File.separatorChar);
        System.out.println("modifiedFileName: " + modifiedFileName);
        modifiedFileName = modifiedFileName.replace('/', File.separatorChar);
        modifiedFileName = modifiedFileName.replace('\\', File.separatorChar);
        modifiedFileName = modifiedFileName.substring(modifiedFileName.lastIndexOf(File.separatorChar) + 1);
        return modifiedFileName;
    }


    //https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
    public String readCode(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            String ls = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }
            reader.close();
            return stringBuilder.toString();
        }
    }
}
