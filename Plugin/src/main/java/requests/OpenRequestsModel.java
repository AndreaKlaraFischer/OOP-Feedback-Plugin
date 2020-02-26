package requests;

import controller.Controller;
import org.kohsuke.github.GHIssue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenRequestsModel {
    public Controller controller;
    public List<GHIssue> openRequestList;
    private int openRequestCounter;

    public OpenRequestsModel(Controller controller) throws IOException {
        this.controller = controller;
        openRequestList = new ArrayList<>();
        controller.XMLFileReader.modifyOpenRequestsCounter(openRequestCounter);
        openRequestCounter = controller.XMLFileReader.readOpenRequestsValueFromXML();
    }

    public String incrementOpenRequestsNumber() throws IOException {
        openRequestCounter++;
        controller.XMLFileReader.modifyOpenRequestsCounter(openRequestCounter);
        return String.valueOf(openRequestCounter);
    }

    public String decrementOpenRequestsNumber() throws IOException {
        openRequestCounter--;
        controller.XMLFileReader.modifyOpenRequestsCounter(openRequestCounter);
        return String.valueOf(openRequestCounter);
    }
}
