package com.example.macromate.model;

public class Korisnik {

    public static final String TABLE_NAME = "korisnik";
    public static final String FIELD_ID = "id";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_LOZINKA = "lozinka";
    public static final String FIELD_IME = "ime";
    public static final String FIELD_PREZIME = "prezime";
    public static final String FIELD_GODINE = "godine";
    public static final String FIELD_KILAZA = "kilaza";
    public static final String FIELD_VISINA = "visina";
    public static final String FIELD_CALORIES_TARGET = "calories_target";
    public static final String FIELD_PROTEIN_TARGET = "protein_target";
    public static final String FIELD_CARBS_TARGET = "carbs_target";
    public static final String FIELD_FAT_TARGET = "fat_target";

    private Long id;
    private String email;
    private String lozinka;
    private String ime;
    private String prezime;
    private int godine;
    private Float kilaza;
    private int visina;
    private Float caloriesTarget;
    private Float proteinTarget;
    private Float carbsTarget;
    private Float fatTarget;

    public Korisnik() {

        this.caloriesTarget = 2000.0f;
        this.proteinTarget = 150.0f;
        this.carbsTarget = 250.0f;
        this.fatTarget = 65.0f;
    }


    public Korisnik(Long id, String email, String lozinka, String ime, String prezime,
                    int godine, Float kilaza, int visina) {
        this.id = id;
        this.email = email;
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.godine = godine;
        this.kilaza = kilaza;
        this.visina = visina;

        this.caloriesTarget = 2000.0f;
        this.proteinTarget = 150.0f;
        this.carbsTarget = 250.0f;
        this.fatTarget = 65.0f;
    }


    public Korisnik(Long id, String email, String lozinka, String ime, String prezime,
                    int godine, Float kilaza, int visina, Float caloriesTarget,
                    Float proteinTarget, Float carbsTarget, Float fatTarget) {
        this.id = id;
        this.email = email;
        this.lozinka = lozinka;
        this.ime = ime;
        this.prezime = prezime;
        this.godine = godine;
        this.kilaza = kilaza;
        this.visina = visina;
        this.caloriesTarget = caloriesTarget;
        this.proteinTarget = proteinTarget;
        this.carbsTarget = carbsTarget;
        this.fatTarget = fatTarget;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLozinka() { return lozinka; }
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public int getGodine() { return godine; }
    public void setGodine(int godine) { this.godine = godine; }

    public Float getKilaza() { return kilaza; }
    public void setKilaza(Float kilaza) { this.kilaza = kilaza; }

    public int getVisina() { return visina; }
    public void setVisina(int visina) { this.visina = visina; }

    public Float getCaloriesTarget() { return caloriesTarget; }
    public void setCaloriesTarget(Float caloriesTarget) { this.caloriesTarget = caloriesTarget; }

    public Float getProteinTarget() { return proteinTarget; }
    public void setProteinTarget(Float proteinTarget) { this.proteinTarget = proteinTarget; }

    public Float getCarbsTarget() { return carbsTarget; }
    public void setCarbsTarget(Float carbsTarget) { this.carbsTarget = carbsTarget; }

    public Float getFatTarget() { return fatTarget; }
    public void setFatTarget(Float fatTarget) { this.fatTarget = fatTarget; }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", lozinka='" + lozinka + '\'' +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", godine=" + godine +
                ", kilaza=" + kilaza +
                ", visina=" + visina +
                ", caloriesTarget=" + caloriesTarget +
                ", proteinTarget=" + proteinTarget +
                ", carbsTarget=" + carbsTarget +
                ", fatTarget=" + fatTarget +
                '}';
    }
}
