package zus.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import zus.model.PlanetDto;
import zus.model.utility.JDBCUtils;

import java.util.ArrayList;

import static zus.model.utility.Common.showAlert;

public class PlanetSelectionView extends Stage {

    private final BorderPane root = new BorderPane();

    public PlanetSelectionView(int userId) {
        ComboBox<PlanetDto> planetComboBox = new ComboBox<>();
        Button btnSelectPlanet = new Button("Izaberi");
        Button btnViewPurchaseSummary = new Button("Zbirni pregled kupovina");

        this.setTitle("Odabir naseljivih planeta");

        ArrayList<PlanetDto> lstHabitablePlanets = JDBCUtils.getHabitablePlanets();

        planetComboBox.getItems().addAll(lstHabitablePlanets);
        planetComboBox.setConverter(new StringConverter<PlanetDto>() {
            @Override
            public String toString(PlanetDto planet) {
                if (planet != null) {
                    return planet.getNaziv() + " - " + planet.getOpis();
                } else {
                    return "";
                }
            }

            @Override
            public PlanetDto fromString(String string) {
                return null;
            }
        });

        btnSelectPlanet.setOnAction(e -> {
            PlanetDto selectedPlanet = planetComboBox.getValue();
            if (selectedPlanet != null) {
                PropertySelectionView propSelectionView = new PropertySelectionView(selectedPlanet, userId);
                propSelectionView.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Greška", "Nijedna planeta nije izabrana.");
            }
        });
        btnViewPurchaseSummary.setOnAction(e -> {
            PurchaseSummaryView purchaseSummaryView = new PurchaseSummaryView(userId);
            purchaseSummaryView.show();
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(planetComboBox, btnSelectPlanet, btnViewPurchaseSummary);

        this.setHeight(300);
        this.setWidth(300);

        this.root.setCenter(vbox);
        super.setScene(new Scene(this.root));
    }
}
