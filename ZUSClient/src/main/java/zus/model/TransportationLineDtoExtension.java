package zus.model;

import java.time.LocalDateTime;

public class TransportationLineDtoExtension extends TransportationLineDto {
    private int brojKupljenihKarata;
    public TransportationLineDtoExtension(int id, String linija, int kapacitet, String voziloMarkaModel, LocalDateTime dtPolazak, int destPlanetaId, int brojKupljenihKarata) {
        super(id, linija, kapacitet, voziloMarkaModel, dtPolazak, destPlanetaId);
        this.brojKupljenihKarata = brojKupljenihKarata;
    }
    public int getBrojKupljenihKarata() {
        return brojKupljenihKarata;
    }

    public void setBrojKupljenihKarata(int brojKupljenihKarata) {
        this.brojKupljenihKarata = brojKupljenihKarata;
    }
}
