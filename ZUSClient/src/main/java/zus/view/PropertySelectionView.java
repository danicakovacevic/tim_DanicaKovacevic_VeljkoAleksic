package zus.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import zus.model.PlanetDto;
import zus.model.PropertyDto;
import zus.model.utility.JDBCUtils;

import java.util.ArrayList;

import static zus.model.utility.Common.showAlert;

public class PropertySelectionView extends Stage {
    private final BorderPane root = new BorderPane();

    public PropertySelectionView(PlanetDto selectedPlanet, int userId) {
        ComboBox<PropertyDto> propComboBox = new ComboBox<>();
        Button btnPurchase = new Button("Kupi nekretninu");
        this.setTitle("Odabir nekretnina na planeti" + selectedPlanet.getNaziv());

        ArrayList<PropertyDto> lstProperties = JDBCUtils.getPropertiesOnPlanet(selectedPlanet.getId());

        propComboBox.getItems().addAll(lstProperties);
        propComboBox.setConverter(new StringConverter<PropertyDto>() {
            @Override
            public String toString(PropertyDto prop) {
                if (prop != null) {
                    return prop.getNaziv() + " - " + prop.getTip() + " od " + prop.getKvadratura() + " m^2.";
                } else {
                    return "";
                }
            }
            @Override
            public PropertyDto fromString(String string) {
                return null;
            }
        });

        btnPurchase.setOnAction(e -> {
            PropertyDto selectedProperty = propComboBox.getValue();
            if (selectedProperty != null) {
                if(JDBCUtils.purchaseProperty(selectedProperty.getId(), userId)){
                    showAlert(Alert.AlertType.CONFIRMATION, "Čestitamo", "Nekretnina je uspešno kupljena.");
                    TransportationSelectionView transportationSelectionView = new TransportationSelectionView(selectedPlanet, userId, selectedProperty);
                    transportationSelectionView.show();
                }
                else {
                    showAlert(Alert.AlertType.ERROR, "Greška", "Došlo je do greške prilikom kupovine jedinice.");
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Greška", "Nijedna nekretnina nije izabrana.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(propComboBox, btnPurchase);

        this.setHeight(300);
        this.setWidth(300);

        this.root.setCenter(vbox);
        super.setScene(new Scene(this.root));
    }
}
