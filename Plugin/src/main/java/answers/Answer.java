package answers;

import config.Constants;
import org.kohsuke.github.GHCommit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Answer {
    private int answerNumber;
    private long answerId;
    private String answerMessage;
    private String tutorName;
    private Date answerDate;
    private List<String> imageUrls;
    private boolean hasChanges;
    //2.12.
    private String requestMessage;

    //02.12. Test
    //public Answer(int number, long id, String message, String tutor, Date date) {
    public Answer(int number, long id, String message, String tutor, Date date, String requestText) {
        //public Answer(int number, long id, String message, String tutor, Date date, String requestText, boolean changes) {
        answerNumber = number;
        answerId = id;
        tutorName = tutor;
        answerDate = date;
        imageUrls = new ArrayList<>();
        answerMessage = extractImagesFromMessage(message);
        //2.12.
        requestMessage = requestText;
        hasChanges = false;

        System.out.println("answerMessage:" + answerMessage);
        System.out.println("imageUrls." + imageUrls);
    }

    public List getImageUrls() {
        return imageUrls;
    }

    public String extractImagesFromMessage(String answerMessage) {
        System.out.println("Ganze Nachricht: " + answerMessage);
        //ArrayList<String> matches = new ArrayList<>();
        String imageRegex = "!\\[(.*?)\\]\\((.*?)\\)";
        String urlRegex = "(?<=\\().*?(?=\\))";
        Pattern imagePattern = Pattern.compile(imageRegex, Pattern.CASE_INSENSITIVE);
        Pattern urlPattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher imageMatcher = imagePattern.matcher(answerMessage);

        while (imageMatcher.find()) {
            String match = answerMessage.substring(imageMatcher.start(0), imageMatcher.end(0));
            System.out.println("match: " + match);

            Matcher urlMatcher = urlPattern.matcher(match);

            while(urlMatcher.find()) {
                System.out.println("Ich komme in die zweite While-Schleife");
                imageUrls.add(urlMatcher.group());
            }
        }
        answerMessage = answerMessage.replaceAll(imageRegex, "");
        System.out.println("Nicht mehr ganze Nachricht: " + answerMessage);
        return answerMessage;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public String  getTutorName() {
        //26.11. Auskommentiert, um den Test mit closedBy und so zu machen.
        //if(tutorName == null) {
            //return "Anonymer Tutor";
       // } else {
            return tutorName;
        //}
    }

    public String getAnswerDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        return dateFormat.format(answerDate);
    }

    public long getAnswerId() {
        return answerId;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public boolean isHasChanges() {
        return hasChanges;
    }

    @Override
    public String toString() {
        return "Msg " + answerId + answerMessage + " tutorname " + tutorName + " answerdate" + answerDate;
    }
}
