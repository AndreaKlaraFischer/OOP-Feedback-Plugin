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
        String nami = faker.harryPotter().character();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        anonymousName = "Anonyme/r " + firstName + " " + lastName;
        System.out.println("anonymousName: " + anonymousName);
        //controller.settingScreen.inputNameField.setText(anonymousName);
        controller.settingScreen.inputNameField.setText(nami);

    }
}
