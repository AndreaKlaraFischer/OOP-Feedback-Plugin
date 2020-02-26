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
    private String requestMessage;

    public Answer(int number, long id, String message, String tutor, Date date, String requestText) {
        answerNumber = number;
        answerId = id;
        tutorName = tutor;
        answerDate = date;
        imageUrls = new ArrayList<>();
        answerMessage = extractImagesFromMessage(message);
        requestMessage = requestText;
    }

    public List getImageUrls() {
        return imageUrls;
    }

    //check with regex if there is an image in the message (images get presented as links in a certain syntax
    //For displaying the answers correctly without the image links, those get removed
    private String extractImagesFromMessage(String answerMessage) {
        String imageRegex = "!\\[(.*?)\\]\\((.*?)\\)";
        String urlRegex = "(?<=\\().*?(?=\\))";
        Pattern imagePattern = Pattern.compile(imageRegex, Pattern.CASE_INSENSITIVE);
        Pattern urlPattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher imageMatcher = imagePattern.matcher(answerMessage);

        while (imageMatcher.find()) {
            String match = answerMessage.substring(imageMatcher.start(0), imageMatcher.end(0));
            Matcher urlMatcher = urlPattern.matcher(match);

            while(urlMatcher.find()) {
                imageUrls.add(urlMatcher.group());
            }
        }
        answerMessage = answerMessage.replaceAll(imageRegex, "");
        return answerMessage;
    }

    public String getAnswerMessage() {
        return answerMessage;
    }

    public String  getTutorName() {
            return tutorName;
    }

    String getAnswerDate() {
        DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
        return dateFormat.format(answerDate);
    }

    long getAnswerId() {
        return answerId;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    @Override
    public String toString() {
        return "Msg " + answerId + answerMessage + " tutorname " + tutorName + " answerdate" + answerDate;
    }
}
