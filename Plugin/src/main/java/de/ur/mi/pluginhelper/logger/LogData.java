package de.ur.mi.pluginhelper.logger;

import java.sql.Timestamp;

public class LogData {

    public static final String DATA_HEADER = "timestamp,type,label,payload";
    private final Timestamp timestamp;
    private final String sessionID;
    private final LogDataType type;
    private final String label;
    private final String payload;

    public LogData(Timestamp timestamp, String sessionID, LogDataType type, String label, String payload) {
        this.timestamp = timestamp;
        this.sessionID = sessionID;
        this.type = type;
        this.label = label;
        this.payload = payload;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public LogDataType getType() {
        return type;
    }

    public String getLabel() {
        return label;
    }

    public String getPayload() {
        return payload;
    }

    public String toCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\"%s\",", timestamp));
        builder.append(String.format("\"%s\",", sessionID));
        builder.append(String.format("\"%s\",", type.name()));
        builder.append(String.format("\"%s\",", label));
        builder.append(String.format("\"%s\"", payload));
        return  builder.toString();
    }
}
