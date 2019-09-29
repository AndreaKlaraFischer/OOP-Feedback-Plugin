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

    //TODO: Abfragen, ob Ordner schon existiert
    /* However the destination location is chosen, explicitly through your code or by JGit,
    the designated directory must either be empty or must not exist.
    Otherwise, an exception will be thrown.*/
    public void cloneRepo() {
        try {
            System.out.println("Repo Methodenaufruf funktioniert!");
            Git.cloneRepository()
                    .setURI(Constants.REPO_URL)
                    .setCredentialsProvider( new UsernamePasswordCredentialsProvider(Constants.REPO_LOGIN, Constants.REPO_PASSWORD ) )
                    .setDirectory(repoPath)
                    .call();
            System.out.println("Repo wurde erfolgreich geklont");
        } catch (Exception e){
            System.out.println("Repo wurde nicht erfolgreich geklont" + e.toString());
        } finally {
            //TODO: Ordner schließen/löschen
        }
    }
}
