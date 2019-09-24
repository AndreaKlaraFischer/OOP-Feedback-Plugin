package requests;

import java.net.NetworkInterface;
import java.net.SocketException;
//import java.io.File;
import java.util.UUID;

public class IDCreater {
    //TODO: UUID, MAC Adresse
    //https://github.com/OOP-Regensburg/PluginHelper/blob/3329a75df571797d6a67c19821d2f36fb0d085cd/src/de/ur/mi/pluginhelper/logger/Log.java#L89


    String id = UUID.randomUUID().toString();
    private String createUUID() {
        String uuid = UUID.randomUUID().toString();
        System.out.println("Random UUID String = " + uuid);
        return uuid;

    }

    //Methode übernommen von Alex
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
}
