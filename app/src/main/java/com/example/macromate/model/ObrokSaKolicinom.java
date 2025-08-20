package com.example.macromate.model;

public class ObrokSaKolicinom {
    private Obrok obrok;
    private float kolicina;

    public ObrokSaKolicinom() {
        this.kolicina = 1.0f;
    }

    public ObrokSaKolicinom(Obrok obrok, float kolicina) {
        this.obrok = obrok;
        this.kolicina = kolicina;
    }

    public Obrok getObrok() { return obrok; }
    public void setObrok(Obrok obrok) { this.obrok = obrok; }

    public float getKolicina() { return kolicina; }
    public void setKolicina(float kolicina) { this.kolicina = kolicina; }

    public float getCalculatedKcal() {
        return obrok != null ? obrok.getKcal() * kolicina : 0;
    }

    public float getCalculatedFat() {
        return obrok != null ? obrok.getFat() * kolicina : 0;
    }

    public float getCalculatedCarb() {
        return obrok != null ? obrok.getCarb() * kolicina : 0;
    }

    public float getCalculatedProtein() {
        return obrok != null ? obrok.getProtein() * kolicina : 0;
    }

    public String getDisplayName() {
        if (obrok == null) return "";
        return obrok.getIme() + " (" + (kolicina * 100) + "g)";
    }

    @Override
    public String toString() {
        return "ObrokSaKolicinom{" +
                "obrok=" + obrok +
                ", kolicina=" + kolicina +
                '}';
    }
}
