package fileReaders;

import android.os.SystemPropertiesProto;
import com.intellij.openapi.project.Project;
import controller.Controller;
import org.eclipse.jgit.api.errors.GitAPIException;

import javax.swing.text.BadLocationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("Methodenaufruf von matchFiles hat funktioniert");
        List<File> modifiedFiles = new ArrayList<>();
        removeAllFoldersFromFileList(resultList);
        System.out.println("resultList hofftl ohne package" + resultList);
        for (int i = 0; i < resultList.size(); i++) {
            System.out.println("resultList: " + resultList);
            for (int j = 0; j < modifiedFilesNames.size(); j++) {
                //Es steht auf jeden Fall was drin. Aber wieso kann ich es nicht anhängen?
                System.out.println("modifiedFilesNames: " + modifiedFilesNames);

                //Hier bekomme ich den String ohne Pfad, nur den Dateinamen mit Endung
                String modifiedFileName = getFileNameWithoutPath(modifiedFilesNames.get(j));

                //20.11. 1515 Test, um eine file da reinzuspeichern
                modifiedFiles.add(resultList.get(i));

                if (resultList.get(i).getName().equals(modifiedFileName)) {
                    //TODO: Das geht nicht. Es wird nichts angehängt, aber macht auch Sinn soweit.
                    //TODO: Das irgendwie faken!
                    modifiedFiles.add(resultList.get(i));

                }
            }
        }
        System.out.println("modifiedFiles: " + modifiedFiles);
        return modifiedFiles;
    }

    public String getFileNameWithoutPath(String modifiedFileName) {
        modifiedFileName = modifiedFileName.substring(modifiedFileName.lastIndexOf('/'));
        return modifiedFileName;
    }

    //TODO: Umkehren und nur bestimmte Datentypen akzeptieren (oder Substring machen)
    private List<File> removeAllFoldersFromFileList(List<File> resultList) {
        //TODO: Schauen, ob eine Datei eine Endung hat. und wenn nicht, dann raushauen.
        //TODO: Das funktioniert noch nicht!
        System.out.println("removeAllFoldersFromFileList");
        System.out.println("START resultlist: " + resultList);
        //Sonst wird was übersprungen! (Danke Jürgen!)
        for(int i = resultList.size() - 1; i > -1; --i)
        {
            // your conditional here
            String fileName = resultList.get(i).getName();
            System.out.println("fileName: " + fileName);
            if (!fileName.contains(".")) {
                System.out.println("resultListEntry: " + resultList.get(i));
                resultList.remove(resultList.get(i));
            }
        }

        /*for (int i = 0; i < resultList.size(); i++) {
            String fileName = resultList.get(i).getName();
            System.out.println("fileName: " + fileName);
            if (!fileName.contains(".")) {
                System.out.println("resultListEntry: " + resultList.get(i));
                resultList.remove(resultList.get(i));
            }
        }*/
        System.out.println("FINAL resultlist: " + resultList);
        return resultList;
    }

    //Ich erstelle den Tab aus der Liste mit den modifiedFiles.
    //Durch eine Schleife. Der Titel ist das File and der Stelle i getName() und der Panel Inhalt ist das hier. Also dort aufrufen.
    //TODO: Wie bekomme ich dann den Dateipfad?

    public String readCodeFromModifiedFile(String filePath) throws FileNotFoundException {
        Scanner codeScanner = new Scanner(new File(filePath));
        String modifiedCode = codeScanner.nextLine();
        codeScanner.close();
        return modifiedCode;
    }

}
