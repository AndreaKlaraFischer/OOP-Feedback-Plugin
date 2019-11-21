package de.ur.mi.pluginhelper.logger;

import de.ur.mi.pluginhelper.config.PluginConfiguration;

import java.io.File;
import java.util.UUID;

public class LogManager {

    public static Log createLog() {
        String id = UUID.randomUUID().toString();
        return getLog(id);
    }

    public static Log openLog(String id) {
        return getLog(id);
    }

    private static Log getLog(String id) {
        File logPath = getLogPath();
        File logFile = new File(logPath, id+".log");
        File dataFile = new File(logPath, id+".data");
        return new Log(id, logFile, dataFile);
    }

    private static File getLogPath() {
        File userDir = new File(System.getProperty("user.home"));
        File dataPath = new File(userDir, PluginConfiguration.DEFAULT_DATA_FOLDER);
        if(!dataPath.exists()) {
            dataPath.mkdir();
        }
        return dataPath;
    }

    private static void syncLog(Log log) {

    }

}
