package config;

public class Constants {
    //Repozugang
    public static final String REPO_URL = "https://github.com/OOP-Feedback-Test/OOP-Feedback-2.0.git";
   // public static final String REPO_NAME = "OOP-Feedback/OOP-Feedback";
    public static final String REPO_NAME = "OOP-Feedback-Test/OOP-Feedback-2.0";
    public static final String REPO_LOGIN = "OOP-Feedback-Test";
    public static final String REPO_PASSWORD = "FredFeedback1920";
    //directories
    public static final String SCREENSHOT_FOLDER = "/screenshots";
    public static final String TUTOR_COMMENTS_FOLDER = "/tutor_comments";
    public static final String CLONED_REPO_FOLDER = "/clonedBranch";
    public static final String CLONED_SRC_FOLDER = TUTOR_COMMENTS_FOLDER + CLONED_REPO_FOLDER + "/src";
    //Textelemente für UI
    public static final String ISSUE_TITLE_BEGINNING = "Anfrage von";
    public static final String ISSUE_TITLE_DATE = "vom";
    public static final String SEPARATOR = " ";
    public static final String HYPHEN = "-";
    public static final String COMMON_LABEL_BEGIN = "_";
    public static final String ANSWER_TITLE_BEGINNING = "Antwort von Tutor/in";
    public static final String FEEDBACK_FOR_FEEDBACK = "Feedback zum Feedback:";
    public static final String FEEDBACK_LABEL_HELPFUL = "hilfreich";
    public static final String FEEDBACK_LABEL_NOT_HELPFUL = "nicht hilfreich";
    public static final String FEEDBACK_LABEL_NEUTRAL = "neutral";
    public static final String GITHUB_ITALIC = "_";
    public static final String GITHUB_BOLD_AND_ITALIC = "***";
    public static final String PROBLEM_SOLVED_SUCCESSFULLY = "Problem gelöst";

    public static final String REGISTRATION_WELCOME_TEXT = "Wähle hier ein Passwort, um deine persönlichen Daten zu schützen. Es muss mindestens 8 Zeichen lang sein.";
    //Email
    public static final String EMAIL_ADDRESS_LIST_TUTORS = "andrea.fischer@stud.uni-regensburg.de";
    public static final String EMAIL_SUBJECT = "Neue Anfrage im Repo";
    public static final String EMAIL_SUBJECT_ANSWER = "Neue Antwort von Tutor";
    public static final String EMAIL_BODY = "Es wurde eine neue Anfrage von einem Studierenden gestellt,\nsie befindet sich in eurem Repo auf GitHub. \n \n";
    public static final String EMAIL_BODY_INFORMATION = "Das ist eine automatisch generierte Email, bitte nicht darauf antworten.";
    public static final String EMAIL_SENDER_ADDRESS = "andrea.fischer@stud.uni-regensburg.de";
    public static final String EMAIL_SENDER_PASSWORD = "FredFeedback19";
    public static final String EMAIL_SENDER_USER = "fia06900";
    public static final String HOST = "smtp.uni-regensburg.de";
    public static final String PORT = "587";
    //Date
    public static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    //Config
    public static final String CONFIG_FILE_PATH = "/.idea/personalConfig.xml";
    //Password
    public static final int MINIMUM_PASSWORD_LENGTH = 8;
    //Threading
    public static final long THREAD_MILLISECONDS = 10000;
}
