package com.mycompany.swingpaintprogram.command;

import com.mycompany.swingpaintprogram.model.Alakzat;
import com.mycompany.swingpaintprogram.model.Rajz;
import java.util.Objects;

/** Adds a shape to a drawing and removes it when undone. */
public class HozzaadParancs implements Parancs {

    private final Rajz rajz;
    private final Alakzat alakzat;
    private int index = -1;

    public HozzaadParancs(Rajz rajz, Alakzat alakzat) {
        this.rajz = Objects.requireNonNull(rajz, "rajz");
        this.alakzat = Objects.requireNonNull(alakzat, "alakzat");
    }

    @Override
    public void vegrehajt() {
        if (index < 0) {
            index = rajz.meret();
        }
        if (rajz.indexOf(alakzat) >= 0) {
            throw new IllegalStateException("Az alakzat mar szerepel a rajzban.");
        }
        rajz.beszur(Math.min(index, rajz.meret()), alakzat);
    }

    @Override
    public void visszavon() {
        if (!rajz.torol(alakzat)) {
            throw new IllegalStateException("A hozzaadott alakzat nem talalhato a rajzban.");
        }
    }
}
