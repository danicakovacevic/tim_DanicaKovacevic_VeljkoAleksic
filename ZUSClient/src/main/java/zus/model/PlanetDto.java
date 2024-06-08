package zus.model;

import java.time.LocalDate;

public class PlanetDto {
    public final int Id;
    public String Naziv;
    public String Opis;
    public Boolean JeNaseljiva;
    public Boolean JePlaneta; //ili satelit
    public LocalDate CreatedAtDt;

    public PlanetDto(int id, String naziv, String opis, Boolean jeNaseljiva, Boolean jePlaneta, LocalDate createdAtDt) {
        Id = id;
        Naziv = naziv;
        Opis = opis;
        JeNaseljiva = jeNaseljiva;
        JePlaneta = jePlaneta;
        CreatedAtDt = createdAtDt;
    }

    public String getOpis() {
        return Opis;
    }

    public String getNaziv() {
        return Naziv;
    }

    public int getId() {
        return Id;
    }
}
