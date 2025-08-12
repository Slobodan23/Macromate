package com.example.macromate.model;

public class Obrok {

    public static final String TABLE_NAME = "obrok";
    public static final String FIELD_ID = "id";
    public static final String FIELD_IME = "ime";
    public static final String FIELD_KCAL = "kcal";
    public static final String FIELD_FAT = "fat";
    public static final String FIELD_CARB = "carb";
    public static final String FIELD_PROTEIN = "protein";




    private Long id;
    private String ime;
    private Float kcal;
    private Float fat;
    private Float carb;
    private Float protein;


    // Default constructor
    public Obrok() {
    }

    // Parameterized constructor
    public Obrok(Long id, String ime, Float kcal, Float fat, Float carb, Float protein) {
        this.id = id;
        this.ime = ime;
        this.kcal = kcal;
        this.fat = fat;
        this.carb = carb;
        this.protein = protein;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public Float getKcal() { return kcal; }
    public void setKcal(Float kcal) { this.kcal = kcal; }

    public Float getFat() { return fat; }
    public void setFat(Float fat) { this.fat = fat; }

    public Float getCarb() { return carb; }
    public void setCarb(Float carb) { this.carb = carb; }

    public Float getProtein() { return protein; }
    public void setProtein(Float protein) { this.protein = protein; }

    @Override
    public String toString() {
        return "Obrok{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", kcal=" + kcal +
                ", fat=" + fat +
                ", carb=" + carb +
                ", protein=" + protein +
                '}';
    }
}
