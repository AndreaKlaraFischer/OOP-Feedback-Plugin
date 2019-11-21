package de.ur.mi.pluginhelper.logger;

import java.io.*;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

public class Log {

    private String id;
    private File logFile;
    private File dataFile;

    public Log(String id, File logFile, File dataFile) {
        this.id = id;
        this.logFile = logFile;
        this.dataFile = dataFile;
        if (!logFile.exists()) {
            prepareLogFile();
        }
        if (!dataFile.exists()) {
            createDataFile();
        }
    }

    public String getId() {
        return id;
    }

    public void log(LogData data) {
        writeLineToFile(data.toCSV(), logFile);
    }

    public void log(Timestamp timestamp, String sessionID, LogDataType type, String label, String payload) {
        LogData data = new LogData(timestamp, sessionID, type, label, payload);
        log(data);
    }

    public void log(String sessionID, LogDataType type, String label, String payload) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        LogData data = new LogData(now, sessionID, type, label, payload);
        log(data);
    }

    private void prepareLogFile() {
        if (logFile.exists()) {
            return;
        }
        try {
            logFile.createNewFile();
            writeLineToFile(LogData.DATA_HEADER, logFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createDataFile() {
        if (dataFile.exists()) {
            return;
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        //String hardwareAddress = getHardwareAddress();
        Properties properties = new Properties();
        properties.setProperty("Created At", String.valueOf(now));
       // properties.setProperty("Hardware Identifier", hardwareAddress);
        properties.setProperty("Identifier", id);
        try {
            dataFile.createNewFile();
            properties.store(new FileOutputStream(dataFile), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getHardwareAddress() {
        try {
            byte[] address = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();
            StringBuilder addressBuilder = new StringBuilder();
            for (int i = 0; i < address.length; i++) {
                addressBuilder.append(String.format("%02X%s", address[i], (i < address.length - 1) ? "-" : ""));
            }
            return addressBuilder.toString();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "unknown";
    }

    private void writeLineToFile(String line, File file) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(file, true));
            out.println(line);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
