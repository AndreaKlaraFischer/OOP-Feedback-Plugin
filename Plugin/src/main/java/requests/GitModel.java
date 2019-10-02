package requests;


import com.intellij.openapi.project.Project;
import config.Constants;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

public class GitModel {
    public static File repoPath;

    public GitModel(Project project) {
        String clonedRepoPath = project.getBasePath() + Constants.CLONED_REPO_FOLDER;
        repoPath = new File(clonedRepoPath);
    }


    /* However the destination location is chosen, explicitly through your code or by JGit,
    the designated directory must either be empty or must not exist.
    Otherwise, an exception will be thrown.*/
    public void cloneRepo() {
        try {
            if(!repoPath.exists()) {
                System.out.println("Repo Methodenaufruf funktioniert!");
                Git.cloneRepository()
                        .setURI(Constants.REPO_URL)
                        .setCredentialsProvider( new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD ) )
                        .setDirectory(repoPath)
                        .call();
                System.out.println("Repo wurde erfolgreich geklont");
            } else {
                System.out.println("Ordner exisitiert bereits");
            }

        } catch (Exception e){
            System.out.println("Repo wurde nicht erfolgreich geklont" + e.toString());
        } finally {
            //TODO: Ordner schließen/löschen, damit es das nächste Mal wieder geklont werden dann
            //repoPath.delete();
            System.out.println("Wurde gelöscht");

        }
    }
}
