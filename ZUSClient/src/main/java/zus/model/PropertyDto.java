package zus.model;

public class PropertyDto {
    private int id;
    private String naziv;
    private String kvadratura;
    private String tip;
    private boolean jeRaspoloziv;

    public PropertyDto(int id, String naziv, String kvadratura, String tip, boolean jeRaspoloziv) {
        this.id = id;
        this.naziv = naziv;
        this.kvadratura = kvadratura;
        this.tip = tip;
        this.jeRaspoloziv = jeRaspoloziv;
    }

    public int getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getKvadratura() {
        return kvadratura;
    }

    public String getTip() {
        return tip;
    }

    public boolean isJeRaspoloziv() {
        return jeRaspoloziv;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setKvadratura(String kvadratura) {
        this.kvadratura = kvadratura;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setJeRaspoloziv(boolean jeRaspoloziv) {
        this.jeRaspoloziv = jeRaspoloziv;
    }
}
