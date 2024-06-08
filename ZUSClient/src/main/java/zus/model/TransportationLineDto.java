package zus.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransportationLineDto {
    private int id;
    private String linija;
    private int kapacitet;
    private String voziloMarkaModel;
    private LocalDateTime dtPolazak;
    private int destPlanetaId;


    public int getId() {
        return id;
    }

    public String getLinija() {
        return linija;
    }

    public TransportationLineDto(int id, String linija, int kapacitet, String voziloMarkaModel, LocalDateTime dtPolazak, int destPlanetaId) {
        this.id = id;
        this.linija = linija;
        this.kapacitet = kapacitet;
        this.voziloMarkaModel = voziloMarkaModel;
        this.dtPolazak = dtPolazak;
        this.destPlanetaId = destPlanetaId;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public String getVoziloMarkaModel() {
        return voziloMarkaModel;
    }

    public String getDtPolazak() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm");
        return dtPolazak.format(formatter);
    }

    public int getDestPlanetaId() {
        return destPlanetaId;
    }


}
