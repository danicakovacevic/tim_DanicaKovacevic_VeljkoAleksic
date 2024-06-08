package zus.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import zus.model.PropertyDto;
import zus.model.TransportationLineDtoExtension;
import zus.model.utility.JDBCUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class PurchaseSummaryView extends Stage {
    private final BorderPane root = new BorderPane();

    public PurchaseSummaryView(int userId) {
        TableView<TransportationLineDtoExtension> transportationTableView = new TableView<>();
        TableColumn<TransportationLineDtoExtension, String> linijaColumn = new TableColumn<>("Linija");
        linijaColumn.setCellValueFactory(new PropertyValueFactory<>("linija"));
        TableColumn<TransportationLineDtoExtension, Integer> kapacitetColumn = new TableColumn<>("Kapacitet");
        kapacitetColumn.setCellValueFactory(new PropertyValueFactory<>("kapacitet"));
        TableColumn<TransportationLineDtoExtension, String> voziloColumn = new TableColumn<>("Vozilo Marka Model");
        voziloColumn.setCellValueFactory(new PropertyValueFactory<>("voziloMarkaModel"));
        TableColumn<TransportationLineDtoExtension, LocalDateTime> dtPolazakColumn = new TableColumn<>("Datum Polaska");
        dtPolazakColumn.setCellValueFactory(new PropertyValueFactory<>("dtPolazak"));
        TableColumn<TransportationLineDtoExtension, Integer> brojKupljenihKarataColumn = new TableColumn<>("Broj kupljenih karata");
        brojKupljenihKarataColumn.setCellValueFactory(new PropertyValueFactory<>("brojKupljenihKarata"));

        transportationTableView.getColumns().addAll(linijaColumn, kapacitetColumn, voziloColumn, dtPolazakColumn, brojKupljenihKarataColumn);

        TableView<PropertyDto> propertyTableView = new TableView<>();
        TableColumn<PropertyDto, String> nazivColumn = new TableColumn<>("Naziv");
        nazivColumn.setCellValueFactory(new PropertyValueFactory<>("naziv"));
        TableColumn<PropertyDto, String> kvadraturaColumn = new TableColumn<>("Kvadratura");
        kvadraturaColumn.setCellValueFactory(new PropertyValueFactory<>("kvadratura"));
        TableColumn<PropertyDto, String> tipColumn = new TableColumn<>("Tip");
        tipColumn.setCellValueFactory(new PropertyValueFactory<>("tip"));

        propertyTableView.getColumns().addAll(nazivColumn, kvadraturaColumn, tipColumn);

        ArrayList<TransportationLineDtoExtension> transportationLines = JDBCUtils.getAllFaresByUserId(userId);
        ArrayList<PropertyDto> properties = JDBCUtils.getAllPropertiesByUserId(userId);

        transportationTableView.getItems().addAll(transportationLines);
        propertyTableView.getItems().addAll(properties);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(transportationTableView, propertyTableView);

        this.setHeight(600);
        this.setWidth(800);

        this.root.setCenter(vbox);
        super.setScene(new Scene(this.root));
    }
}
