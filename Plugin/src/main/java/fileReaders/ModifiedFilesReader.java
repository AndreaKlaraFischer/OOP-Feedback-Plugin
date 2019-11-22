package fileReaders;

import android.os.SystemPropertiesProto;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.intellij.openapi.project.Project;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.text.BadLocationException;
import java.io.*;
import java.util.*;

public class ModifiedFilesReader {
    public Controller controller;
    public Project project;

    public ModifiedFilesReader(Controller controller) {
        this.controller = controller;
        project = controller.project;
    }

    //https://stackoverflow.com/questions/14676407/list-all-files-in-the-folder-and-also-sub-folders
    //Use ArrayList instead of array as return value
    public List<File> listAllFiles(String directoryName) throws IOException, GitAPIException, BadLocationException {
        //TODO: Auch wenn ich den ganzen geclonten Repo-Ordner übergebe, wird nur der src folder angeschaut. Warum?
        File srcFolder = new File(directoryName);

        List<File> allFilesList = new ArrayList<>();
        //get all files from a directory
        File[] fileList = srcFolder.listFiles();
        if (fileList != null) {
            allFilesList.addAll(Arrays.asList(fileList));
        }
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    System.out.println(file.getAbsolutePath());
                } else if (file.isDirectory()) {
                    allFilesList.addAll(listAllFiles(file.getAbsolutePath()));
                }
            }
        }
        return allFilesList;
    }


    public List<File> matchFiles(List<File> resultList, List<String> modifiedFilesNames) {
        List<File> modifiedFiles = new ArrayList<>();
        List<String> allFileNamesWithoutPath = new ArrayList<>();
        removeAllFoldersFromFileList(resultList);

        for (int i = 0; i < resultList.size(); i++) {
            System.out.println("resultList: " + resultList);
            for (String modifiedFilesName : modifiedFilesNames) {
                //Hier bekomme ich den String ohne Pfad, nur den Dateinamen mit Endung
                String modifiedFileName = getFileNameWithoutPath(modifiedFilesName);
                String resultFileName = getFileNameWithoutPath(resultList.get(i).getName());

                allFileNamesWithoutPath.add(resultFileName);
                System.out.println(allFileNamesWithoutPath);
                allFileNamesWithoutPath.add("testTest"); //Das steht hier nur zu Testzwecken

                if (resultFileName.equals(modifiedFileName)) {
                    System.out.println("Hier bin ich reingekommen.");
                    modifiedFiles.add(resultList.get(i));
                }
                if(allFileNamesWithoutPath.contains(modifiedFileName)) {
                    System.out.println("Änderungen vorhanden!");
                }
            }
        }
        System.out.println("modifiedFiles: " + modifiedFiles);
        return modifiedFiles;
    }

    //TODO: Überlegen, was passiert, wenn ein Dateiname gar keinen File.seperator drin hat.
    private String getFileNameWithoutPath(String modifiedFileName) {
        System.out.println("File.separator" + File.separator);
        System.out.println("File.seperatorChar" + File.separatorChar);
        System.out.println("modifiedFileName: " + modifiedFileName);
        modifiedFileName = modifiedFileName.replace('/', File.separatorChar);
        modifiedFileName = modifiedFileName.replace('\\', File.separatorChar);
        modifiedFileName = modifiedFileName.substring(modifiedFileName.lastIndexOf(File.separatorChar ) + 1); //TODO: Backslash escape , Pfad library
        return modifiedFileName;
    }

    private void removeAllFoldersFromFileList(List<File> resultList) {
        System.out.println("removeAllFoldersFromFileList");
        System.out.println("START resultlist: " + resultList);
        //Sonst wird was übersprungen! (Danke Jürgen!)
        for(int i = resultList.size() - 1; i > -1; --i) {
            // your conditional here
            String fileName = resultList.get(i).getName();
            System.out.println("fileName: " + fileName);
            if (!fileName.contains(".")) {
                System.out.println("resultListEntry: " + resultList.get(i));
                resultList.remove(resultList.get(i));
            }
        }
        System.out.println("FINAL resultlist: " + resultList);
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
