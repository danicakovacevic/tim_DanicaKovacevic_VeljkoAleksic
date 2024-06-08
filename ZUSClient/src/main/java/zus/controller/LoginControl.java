package zus.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import zus.model.UserDto;
import zus.model.utility.JDBCUtils;
import zus.view.PlanetSelectionView;

import static zus.model.utility.Common.showAlert;

public class LoginControl extends Parent implements EventHandler<ActionEvent> {

    private TextField tfUserName = new TextField();
    private TextField tfPassword = new TextField();


    public LoginControl(TextField tfUserName, TextField tfPassword) {
        this.tfUserName = tfUserName;
        this.tfPassword = tfPassword;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        String userName = this.tfUserName.getText().trim();
        String pwd = this.tfPassword.getText().trim();

        if(userName == null || pwd == null || userName == "" || pwd == ""){
            showAlert(Alert.AlertType.ERROR, "Greška", "Unesite pravilno kredencijale!");
        }
        UserDto user = JDBCUtils.authenticateUser(userName, pwd);
        if(user == null)
        {
            showAlert(Alert.AlertType.ERROR, "Greška", "Neispravna lozinka i/ili korisničko ime.");
        }
        else {
            PlanetSelectionView planetSelectionView = new PlanetSelectionView(user.getId());
            planetSelectionView.show();

            ((Node) actionEvent.getSource()).getScene().getWindow().hide();
        }
    }

    @Override
    public Node getStyleableNode() {
        return null;
    }
}
