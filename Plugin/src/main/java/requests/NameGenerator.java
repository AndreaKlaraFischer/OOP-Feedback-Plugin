package requests;

import com.github.javafaker.Faker;
import controller.Controller;

public class NameGenerator {
    private Controller controller;

    public NameGenerator(Controller controller) {
        this.controller = controller;
    }

    //https://rieckpil.de/howto-generate-random-data-in-java-using-java-faker/
    public void generateAnonymousName() {
        String anonymousName = "";
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        anonymousName = "Anonym " + firstName + " " + lastName;
        controller.settingScreen.inputNameField.setText(anonymousName);
    }
}
