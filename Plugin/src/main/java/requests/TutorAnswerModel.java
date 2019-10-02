package requests;
import config.Constants;
//TODO: Mit ID abgleichen!
public class TutorAnswerModel {

    //Equivalent to request class
    /*TODO: Idee: Wenn der Tutor seine Antwort schreibt, dann soll er seinen Namen als Label einfügen
    --> Wenn die Antwort dann angeschaut wird, soll angezeigt werden: Beantwortet von "Andrea Fischer" --> Konstanten
    --> get Label (aber nur das)
    */

    public TutorAnswerModel() {

    }

    public void sendAnswer() {

    }

    private void createAnswer() {
     //TODO: neuen String erstellen mit getLabel so und so mit Namen
        //ein bisschen overengineert, aber wenn kein neues Label angegeben wurde (also Labels unter drei), dann "Antwort von Tutor" (default Kontante)
        //wenn neues Label angegeben wurde, dann "Antwort von Hans Wurscht") --> Variable
        //TODO: aus den answerTitlen soll dann die Liste gebaut werden!
        String answerTitle = Constants.ANSWER_TITEL_BEGINNING; //+Label
    }

    //TODO: Geänderten Code commiten
    private void pullCode() {
        try {
           // PullCommand pullCommand = git.pull();
            // pullCommand.call();
        } catch (Exception e){
            System.out.println("Pull Exeption + " + e.toString());
        }
    }

    //getClosedAt()


}
