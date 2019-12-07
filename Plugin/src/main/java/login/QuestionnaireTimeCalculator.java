package login;


import config.Constants;
import controller.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class QuestionnaireTimeCalculator {
    private Controller controller;
    public QuestionnaireTimeCalculator(Controller controller) {
        this.controller = controller;
    }

    //https://stackoverflow.com/questions/12087419/adding-days-to-a-date-in-java
    public boolean calculateQuestionnaireDelta() throws ParseException {
        boolean isTimeForQuestionnaire = false;

        String savedDate = controller.XMLFileReader.readDateFromXML();
        //String currentDateLong = controller.getCurrentDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);

        Date currentTime = new Date();

       String currentDateShortString  =  simpleDateFormat.format(currentTime);



        Calendar calendar = Calendar.getInstance();
       // calendar.setTime(simpleDateFormat.parse(currentDateShortString));
        calendar.setTime(simpleDateFormat.parse(savedDate));
        System.out.println("Saved Date: " + savedDate);
        calendar.add(Calendar.DATE, Constants.DAYS_BETWEEN_QUESTIONNAIRES);
       String expectedQuestionnaireDateString = simpleDateFormat.format(calendar.getTime());

        System.out.println("expectedTime nach dem addieren: " + expectedQuestionnaireDateString);

        //TODO: Das vielleicht noch ein bisschen schöner machen, damit man nicht so oft parst.
        Date currentDateShort = simpleDateFormat.parse(currentDateShortString);
        System.out.println("Date current nach parsen: " + currentDateShort);
        Date expectedQuestionnaireDate = simpleDateFormat.parse(expectedQuestionnaireDateString);
        System.out.println("Date expected nach parsen: " + expectedQuestionnaireDate);

        //Wenn der Abstand 3 Tage ist oder größer
        //Future Work: Das an die Tage anpassen, immer runterrechnen auf die Zeit, die noch übrig bleibt
        if(currentDateShortString.compareTo(expectedQuestionnaireDateString) > 0 || currentDateShortString.compareTo(expectedQuestionnaireDateString) == 0) {
            System.out.println(currentDateShortString + ">" +  expectedQuestionnaireDateString);
            isTimeForQuestionnaire = true;
        }
        return isTimeForQuestionnaire;
    }
}
