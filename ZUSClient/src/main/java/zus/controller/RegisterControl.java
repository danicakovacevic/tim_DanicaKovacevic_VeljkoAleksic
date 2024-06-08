package zus.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import zus.model.utility.JDBCUtils;

import static zus.model.utility.Common.showAlert;

public class RegisterControl implements EventHandler<ActionEvent> {

    private TextField tfNewUserUserName = new TextField();
    private TextField tfNewUserFirstName = new TextField();
    private TextField tfNewUserLastName = new TextField();
    private TextField tfNewUserPassword = new TextField();

    public RegisterControl(TextField tfNewUserUserName, TextField tfNewUserFirstName, TextField tfNewUserLastName, TextField tfNewUserPassword) {
        this.tfNewUserUserName = tfNewUserUserName;
        this.tfNewUserFirstName = tfNewUserFirstName;
        this.tfNewUserLastName = tfNewUserLastName;
        this.tfNewUserPassword = tfNewUserPassword;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String newUserUserName = this.tfNewUserUserName.getText().trim();
        String newUserFirstName = this.tfNewUserFirstName.getText().trim();
        String newUserLastName = this.tfNewUserLastName.getText().trim();
        String newUserPassword = this.tfNewUserPassword.getText().trim();

        if(newUserUserName == null || newUserUserName == "" ||
           newUserFirstName == null || newUserFirstName == "" ||
           newUserLastName == null || newUserLastName == "" ||
           newUserPassword == null || newUserPassword == "") {
            showAlert(Alert.AlertType.ERROR, "Greška", "Unesite pravilno kredencijale!");
        }

        if(JDBCUtils.registerNewUser(newUserUserName, newUserFirstName, newUserLastName, newUserPassword)){
            showAlert(Alert.AlertType.CONFIRMATION, "Dobrodošli", "Korisnik uspešno registrovan!");
        }
        else{
            showAlert(Alert.AlertType.ERROR, "Greška", "Korisničko ime već postoji u sistemu.");
        }
    }
}
