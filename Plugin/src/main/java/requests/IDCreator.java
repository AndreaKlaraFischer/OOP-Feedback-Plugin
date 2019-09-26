package requests;

import org.jetbrains.annotations.NotNull;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.UUID;

//import java.io.File;

public class IDCreator {

    //Muss das void bleiben?
    public void createRequestID() {
        //TODO: UUID, MAC Adresse

        //Hier ID aus uuid und MAC Adresse zusammen bauen
        String uuid = createUUID();
        String hardwareAddress = getHardwareAddress();
        String seperator = " - ";
        String requestId = uuid + seperator + hardwareAddress;
        System.out.println(requestId);

    }

    @NotNull
    private String createUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid;

    }
    //https://github.com/OOP-Regensburg/PluginHelper/blob/3329a75df571797d6a67c19821d2f36fb0d085cd/src/de/ur/mi/pluginhelper/logger/Log.java#L89
    //Methode übernommen von Alex

    @NotNull
    private String getHardwareAddress() {
        //return "Hardware Adresse";
        try {
           byte[] address = NetworkInterface.getNetworkInterfaces().nextElement().getHardwareAddress();
          /* StringBuilder addressBuilder = new StringBuilder();
           //Hier ist der Fehler!! address ist null
           for (int i  = 0; i < address.length; i++) {
                addressBuilder.append(String.format("%02X%s", address[i], (i < address.length - 1) ? "-" : ""));
            }
            return addressBuilder.toString();*/
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "unknown";
    }
}
