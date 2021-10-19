package be.vdab.luigi.domain;

import java.time.LocalDate;

public class Persoon {
    private final String voornaam;
    private final String familieNaam;
    private final int aantalKinderen;
    private final boolean gehuwd;
    private final LocalDate geboorte;
    private final Adres adres;

    public Persoon(String voornaam, String familieNaam, int aantalKinderen, boolean gehuwd, LocalDate geboorte, Adres adres) {
        this.voornaam = voornaam;
        this.familieNaam = familieNaam;
        this.aantalKinderen = aantalKinderen;
        this.gehuwd = gehuwd;
        this.geboorte = geboorte;
        this.adres = adres;
    }

    public String getNaam() {
        return voornaam + ' ' + familieNaam;
    }

    public int getAantalKinderen() {
        return aantalKinderen;
    }

    public boolean isGehuwd() {
        return gehuwd;
    }

    public LocalDate getGeboorte() {
        return geboorte;
    }

    public Adres getAdres() {
        return adres;
    }
}
