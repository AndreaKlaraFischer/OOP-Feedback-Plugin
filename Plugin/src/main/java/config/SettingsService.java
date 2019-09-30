package config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

//TODO: Angeln gehen 
@State(name = "statiFisch", storages = {@Storage("fischifisch.xml")})
public class SettingsService implements PersistentStateComponent<SettingsService.State> {
    //Subklasse
    static class State
    {
        //Hier kann man die Sachen reinschreiben, die ich persistieren will: zB hardWareAdresse, RequestID, Studierendenname
        //Hier Variablen anlegen, getter und setter Methoden dafür im Service schreiben
        public String value;
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