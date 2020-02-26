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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.SIMPLE_DATE_FORMAT);
        Date currentTime = new Date();
        String currentDateShortString  =  simpleDateFormat.format(currentTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(savedDate));
        //calculate 3 days to the saved date
        calendar.add(Calendar.DATE, Constants.DAYS_BETWEEN_QUESTIONNAIRES);
        String expectedQuestionnaireDateString = simpleDateFormat.format(calendar.getTime());

        //If 3 or more days have passed since the saved date in the config file, the questionnaire has to be shown
        if(currentDateShortString.compareTo(expectedQuestionnaireDateString) > 0 || currentDateShortString.compareTo(expectedQuestionnaireDateString) == 0) {
            isTimeForQuestionnaire = true;
        }
        return isTimeForQuestionnaire;
    }
}
