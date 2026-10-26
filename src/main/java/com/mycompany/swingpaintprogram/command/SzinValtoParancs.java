package com.mycompany.swingpaintprogram.command;

import com.mycompany.swingpaintprogram.model.Alakzat;
import java.awt.Color;
import java.util.Objects;

/** Changes a shape's color and remembers the previous color for undo. */
public class SzinValtoParancs implements Parancs {

    private final Alakzat alakzat;
    private final Color regiSzin;
    private final Color ujSzin;

    public SzinValtoParancs(Alakzat alakzat, Color ujSzin) {
        this.alakzat = Objects.requireNonNull(alakzat, "alakzat");
        this.regiSzin = alakzat.getSzin();
        this.ujSzin = Objects.requireNonNull(ujSzin, "ujSzin");
    }

    @Override
    public void vegrehajt() {
        alakzat.setSzin(ujSzin);
    }

    @Override
    public void visszavon() {
        alakzat.setSzin(regiSzin);
    }
}
