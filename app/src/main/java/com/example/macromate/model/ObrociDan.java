package com.example.macromate.model;

import java.util.Date;
import java.util.List;

public class ObrociDan {
    public static final String TABLE_NAME = "obroci_dan";
    public static final String FIELD_ID = "id";
    public static final String FIELD_DORUCAK_IDS = "dorucak_ids";
    public static final String FIELD_RUCAK_IDS = "rucak_ids";
    public static final String FIELD_VECERA_IDS = "vecera_ids";
    public static final String FIELD_UZINA_IDS = "uzina_ids";
    public static final String FIELD_DATUM = "datum";

    private Long id;
    private List<Obrok> dorucak;
    private List<Obrok> rucak;
    private List<Obrok> vecera;
    private List<Obrok> uzina;
    private Date datum;

    public ObrociDan(List<Obrok> dorucak, List<Obrok> rucak, List<Obrok> vecera, List<Obrok> uzina, Date datum) {
        this.dorucak = dorucak;
        this.rucak = rucak;
        this.vecera = vecera;
        this.uzina = uzina;
        this.datum = datum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Obrok> getDorucak() {
        return dorucak;
    }

    public void setDorucak(List<Obrok> dorucak) {
        this.dorucak = dorucak;
    }

    public List<Obrok> getRucak() {
        return rucak;
    }

    public void setRucak(List<Obrok> rucak) {
        this.rucak = rucak;
    }

    public List<Obrok> getVecera() {
        return vecera;
    }

    public void setVecera(List<Obrok> vecera) {
        this.vecera = vecera;
    }

    public List<Obrok> getUzina() {
        return uzina;
    }

    public void setUzina(List<Obrok> uzina) {
        this.uzina = uzina;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }
}