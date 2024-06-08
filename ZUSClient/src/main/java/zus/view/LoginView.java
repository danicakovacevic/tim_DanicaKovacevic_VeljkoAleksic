package zus.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zus.controller.LoginControl;
import zus.controller.RegisterControl;

public class LoginView extends Stage {
    private final BorderPane root = new BorderPane();
    private final TextField tfUserName = new TextField();
    private final TextField tfPassword = new TextField();
    private final Button btLogin = new Button("Login");
    private final Button btRegister = new Button("Register");

    public LoginView() {
        initialize(true);
    }

private void initialize(Boolean isFirstInit){
    super.setTitle("ZUS Client");

    this.btLogin.setOnAction(new LoginControl(this.tfUserName, this.tfPassword));

    this.setWidth(700);
    this.setHeight(300);

    this.root.setCenter(this.setHBox());

    btRegister.setOnAction(event -> {
        RegisterNewUser();
    });

    if(isFirstInit){
        Scene scene = new Scene(this.root);
        super.setScene(scene);
    }
}

    private HBox setHBox() {
        HBox hbox = new HBox(10, new Label("User name:"), this.tfUserName,
                new Label("Password:"), this.tfPassword,
                this.btLogin, this.btRegister);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private void RegisterNewUser(){
        VBox registrationForm = new VBox(10);

        TextField tfNewUserUserName = new TextField();
        TextField tfNewUserFirstName = new TextField();
        TextField tfNewUserLastName = new TextField();
        TextField tfNewUserPassword = new TextField();

        Button btSaveData = new Button("Kreiraj nalog");
        Button btBack = new Button("Nazad");

        btSaveData.setOnAction(new RegisterControl(tfNewUserUserName, tfNewUserFirstName, tfNewUserLastName, tfNewUserPassword));
        btBack.setOnAction(event -> this.initialize(false));

        tfNewUserUserName.setPromptText("Unesite korisničko ime...");
        tfNewUserFirstName.setPromptText("Vaše ime...");
        tfNewUserLastName.setPromptText("Vaše prezime...");
        tfNewUserPassword.setPromptText("Lozinka...");

        this.setWidth(700);
        this.setHeight(400);

        this.root.setPadding(new Insets(30,30,30,30));

        registrationForm.getChildren().addAll(tfNewUserUserName, tfNewUserFirstName, tfNewUserLastName, tfNewUserPassword, btSaveData, btBack);
        registrationForm.setPadding(new Insets(30, 30, 30, 30));

        root.setCenter(registrationForm);
    }
}
