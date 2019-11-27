package requests;

import config.Constants;
import controller.Controller;

public class BranchNameCreator {

    private Controller controller;
    private int counter;

    public BranchNameCreator(Controller controller) {
        this.controller = controller;
        counter = 0;
        controller.XMLFileReader.modifyCounter(counter);
        controller.XMLFileReader.readCounterValueFromXML();
        counter = controller.XMLFileReader.readCounterValueFromXML();
    }

    public String createBranchName(String studentName, String requestCounter, String requestDate) {
        studentName = removeWhitespacesFromStudentName(studentName);
        requestDate  = formatDateForBranchName(requestDate);
        return "Branch-" +
                studentName +
                Constants.HYPHEN +
                requestCounter +
                Constants.HYPHEN +
                requestDate;
    }

    private String removeWhitespacesFromStudentName(String studentName) {
        for(int i = 0; i < studentName.length(); i++ ){
            studentName = studentName.replaceAll("\\s+","");
        }
        return studentName;
    }

    //Hier werden alle Satzzeichen entfernt und nur die ersten vier Ziffern rausgeholt und dann noch die letzten 4.
    private String formatDateForBranchName(String requestDate) {
        for (int i = 0; i < requestDate.length(); i++) {
            requestDate = requestDate.replaceAll("\\p{Punct}", "");
        }
        String date = requestDate.substring(0,4);
        String day = requestDate.substring(requestDate.length() - 4);
        requestDate = date + Constants.HYPHEN + day;
        return requestDate;
    }

    public String incrementRequestCounter() {
        counter ++;
        controller.XMLFileReader.modifyCounter(counter);
        return String.valueOf(counter);
    }
}
