package config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

//TODO: Angeln gehen
//TODO: Im Homeverzeichnis vom Benutzer einen Ordner anlegen und Daten dort abspeichern (weiter: Betriebssysteme abfragen) Windows: app data
//TODO: in "LogManager" nachschauen: File getLogPath()
//TODO: Beim Starten des Plugins einen neuen Ordner beim Benutzer anlegen (abfragen, ob er da schon ist)
@State(name = "statiFisch", storages = {@Storage("fischifisch.xml")})
public class SettingsService implements PersistentStateComponent<SettingsService.State> {
    //Subklasse
    static class State {
        //Hier kann man die Sachen reinschreiben, die ich persistieren will: zB hardWareAdresse, RequestID, Studierendenname
        //Hier Variablen anlegen, getter und setter Methoden dafür im Service schreiben
        //TODO: Für Datenschutz: Sagen, was wir machen, was gespeichert wird --> Notizen machen, alles dokumentieren
        //TODO: Unterscheiden zwischen paar dauerhaft zu speichernden Sachen wie Name (muss ersetzt werden können)
        public String value;
        public String name;
    }

    private State state;

    public SettingsService()
    {
        state = new State();
    }

    public void setValue(String value)
    {
        state.value = value;
    }

    public String getValue()
    {
        return state.value;
    }


    @Override
    public void noStateLoaded()
    {
        System.out.println("noStateLoaded()");
    }

    public State getState() {
        System.out.println("getState()");
        return state;
    }

    //Wird automatisch aufgerufen im Hintergrund (zB. wenn xml-file geändert wurde)
    public void loadState(State state) {
        System.out.println("state loaded...");
        this.state = state;
    }

    public static SettingsService getInstance() {
        return ServiceManager.getService(SettingsService.class);
    }
}