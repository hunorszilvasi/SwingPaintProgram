package com.mycompany.swingpaintprogram.ui;

public enum RajzEszkoz {
    KIJELOLES("Kijelölés / mozgatás"),
    VONAL("Vonal"),
    TEGLALAP("Téglalap"),
    ELLIPSZIS("Ellipszis");

    private final String megnevezes;

    RajzEszkoz(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    @Override
    public String toString() {
        return megnevezes;
    }
}
