package zus.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import zus.model.PlanetDto;
import zus.model.PropertyDto;
import zus.model.TransportationLineDto;
import zus.model.utility.JDBCUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static zus.model.utility.Common.showAlert;

public class TransportationSelectionView extends Stage {
    private final BorderPane root = new BorderPane();

    public TransportationSelectionView(PlanetDto selectedPlanet, int userId, PropertyDto purchasedProperty) {
        ComboBox<TransportationLineDto> propComboBox = new ComboBox<>();
        TextField tfCntTickets = new TextField();
        tfCntTickets.setPromptText("Unesite broj karata");

        Button btnPurchase = new Button("Kupi kartu");
        this.setTitle("Odabir prevoza do planete " + selectedPlanet.getNaziv());

        ArrayList<TransportationLineDto> lstLines = JDBCUtils.getAvailableTransportationLines(selectedPlanet.getId());

        propComboBox.getItems().addAll(lstLines);
        propComboBox.setConverter(new StringConverter<TransportationLineDto>() {
            @Override
            public String toString(TransportationLineDto prop) {
                if (prop != null) {
                    return prop.getLinija() + " - " + prop.getDtPolazak() + " vozilom " + prop.getVoziloMarkaModel();
                } else {
                    return "";
                }
            }

            @Override
            public TransportationLineDto fromString(String string) {
                return null;
            }
        });

        DatePicker datePicker1 = new DatePicker();
        datePicker1.setPromptText("Datum rođenja");

        DatePicker datePicker2 = new DatePicker();
        datePicker2.setPromptText("Datum rođenja");

        DatePicker datePicker3 = new DatePicker();
        datePicker3.setPromptText("Datum rođenja");

        TextField tfPassenger1FirstName = new TextField();
        tfPassenger1FirstName.setPromptText("Saputnik 1 - ime");
        TextField tfPassenger1LastName = new TextField();
        tfPassenger1LastName.setPromptText("Saputnik 1 - prezime");

        TextField tfPassenger2FirstName = new TextField();
        tfPassenger2FirstName.setPromptText("Saputnik 2 - ime");
        TextField tfPassenger2LastName = new TextField();
        tfPassenger2LastName.setPromptText("Saputnik 2 - prezime");

        TextField tfPassenger3FirstName = new TextField();
        tfPassenger3FirstName.setPromptText("Saputnik 3 - ime");
        TextField tfPassenger3LastName = new TextField();
        tfPassenger3LastName.setPromptText("Saputnik 3 - prezime");

        btnPurchase.setOnAction(e -> {
            TransportationLineDto selectedLine = propComboBox.getValue();
            int cntTicketInt = Integer.parseInt(tfCntTickets.getText());
            if (selectedLine != null) {
                if (JDBCUtils.purchaseFare(selectedLine.getId(), userId, cntTicketInt)) {
                    showAlert(Alert.AlertType.CONFIRMATION, "Čestitamo", "Kupovina je uspešna. Srećan put!");

                    LocalDateTime now = LocalDateTime.now();
                    LocalDate now1 = LocalDate.now();
                    String firstName1 = tfPassenger1FirstName.getText();
                    String lastName1 = tfPassenger1LastName.getText();
                    String firstName2 = tfPassenger2FirstName.getText();
                    String lastName2 = tfPassenger2LastName.getText();
                    String firstName3 = tfPassenger3FirstName.getText();
                    String lastName3 = tfPassenger3LastName.getText();

                    JDBCUtils.insertPassengers(purchasedProperty.getId(), firstName1, lastName1, now, now1, userId, selectedLine.getId());

                    if (!firstName1.isEmpty() && !lastName1.isEmpty()) {
                        LocalDate datumRodjenja1 = datePicker1.getValue();
                        JDBCUtils.insertPassengers(purchasedProperty.getId(), firstName1, lastName1, now, datumRodjenja1, null, selectedLine.getId());
                    }

                    if (!firstName2.isEmpty() && !lastName2.isEmpty()) {
                        LocalDate datumRodjenja2 = datePicker2.getValue();
                        JDBCUtils.insertPassengers(purchasedProperty.getId(), firstName2, lastName2, now, datumRodjenja2, null, selectedLine.getId());
                    }

                    if (!firstName3.isEmpty() && !lastName3.isEmpty()) {
                        LocalDate datumRodjenja3 = datePicker3.getValue();
                        JDBCUtils.insertPassengers(purchasedProperty.getId(), firstName3, lastName3, now, datumRodjenja3, null, selectedLine.getId());
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Greška", "Došlo je do greške prilikom kupovine karte.");
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Greška", "Nijedna karta nije izabrana.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(propComboBox, tfCntTickets, datePicker1, tfPassenger1FirstName, tfPassenger1LastName,
                datePicker2, tfPassenger2FirstName, tfPassenger2LastName,
                datePicker3, tfPassenger3FirstName, tfPassenger3LastName,
                btnPurchase);

        this.setHeight(900);
        this.setWidth(900);

        this.root.setCenter(vbox);
        super.setScene(new Scene(this.root));
    }
}
