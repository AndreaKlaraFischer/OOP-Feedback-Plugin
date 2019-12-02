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
        //TODO: Das noch richtig machen!
        boolean isTimeForQuestionnaire = false;

        String savedDate = controller.XMLFileReader.readDateFromXML();
        //String currentDateLong = controller.getCurrentDate();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
        Date currentTime = new Date();
       String currentDateShort  =  simpleDateFormat.format(currentTime);


       // String expectedQuestionnaireDate =
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(currentDateShort));
        //calendar.add(Calendar.DATE, Constants.DAYS_BETWEEN_QUESTIONNAIRES);
        calendar.add(Calendar.DATE, 3);
        // Date expectedQuestionnaireDate = simpleDateFormat.parse(calendar.getTime().toString());
        String expectedQuestionnaireDate = simpleDateFormat.format(calendar.getTime());
        System.out.println("expectedTime nach dem addieren: " + expectedQuestionnaireDate);

        //TODO: Oder größer!! Testen, was passiert wenn "gestern" erwartet grwesen wäre (hier mit Dates arbeiten statt mit Strings)
        if (expectedQuestionnaireDate.equals(currentDateShort))  {
            isTimeForQuestionnaire = true;


        }
        return isTimeForQuestionnaire;
    }
}
