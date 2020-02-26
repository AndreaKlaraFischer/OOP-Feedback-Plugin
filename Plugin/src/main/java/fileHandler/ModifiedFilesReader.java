package fileHandler;

import com.intellij.openapi.project.Project;
import controller.Controller;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class ModifiedFilesReader {
    public Controller controller;
    public Project project;

    private HashMap<String, File> localFileMap, clonedFileMap;

    public ModifiedFilesReader(Controller controller) {
        this.controller = controller;
        project = controller.project;
    }

    //all files of the original project and the annotated cloned project are each saved in a hash map
    public void saveFilesInHashMaps(String branchName) throws IOException {
        String srcFolderPathLocalRepo = project.getBasePath() + "/src";
        String srcFolderPathClonedRepo = project.getBasePath() + "/.idea/tutor_comments/" + branchName + "/src";
        List<File> filesSrcFolderLocalRepo = listAllFiles(srcFolderPathLocalRepo);
        List<File> filesSrcFolderClonedRepo = listAllFiles(srcFolderPathClonedRepo);

        localFileMap = new HashMap<>();
        clonedFileMap = new HashMap<>();

        for (File file : filesSrcFolderLocalRepo) {
            String fileName = getFileNameWithoutPath(file.getName());
            localFileMap.put(fileName, file);
        }

        for (File file : filesSrcFolderClonedRepo) {
            String fileName = getFileNameWithoutPath(file.getName());
            clonedFileMap.put(fileName, file);
        }
    }

    //Check if there are modified files
    public List<File> collectModifiedFiles() throws IOException {
        List<File> modifiedFiles = new ArrayList<>();
        try {
            for (String key : localFileMap.keySet()) {
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
    private List<File> listAllFiles(String directoryName) {
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
        return allFilesList;
    }

    //Remove path from filename in order to get only the name for comparing the files
    private String getFileNameWithoutPath(String modifiedFileName) {
        modifiedFileName = modifiedFileName.replace('/', File.separatorChar);
        modifiedFileName = modifiedFileName.replace('\\', File.separatorChar);
        modifiedFileName = modifiedFileName.substring(modifiedFileName.lastIndexOf(File.separatorChar) + 1);
        return modifiedFileName;
    }

    //https://stackoverflow.com/questions/326390/how-do-i-create-a-java-string-from-the-contents-of-a-file
    //read content of file
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
