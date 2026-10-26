package com.mycompany.swingpaintprogram.command;

import com.mycompany.swingpaintprogram.model.Alakzat;
import com.mycompany.swingpaintprogram.model.Rajz;
import java.util.Objects;


public class TorolParancs implements Parancs {

    private final Rajz rajz;
    private final Alakzat alakzat;
    private int index = -1;

    public TorolParancs(Rajz rajz, Alakzat alakzat) {
        this.rajz = Objects.requireNonNull(rajz, "rajz");
        this.alakzat = Objects.requireNonNull(alakzat, "alakzat");
    }

    @Override
    public void vegrehajt() {
        int aktualisIndex = rajz.indexOf(alakzat);
        if (aktualisIndex < 0) {
            throw new IllegalStateException("A torlendo alakzat nem talalhato a rajzban.");
        }
        index = aktualisIndex;
        rajz.torol(alakzat);
    }

    @Override
    public void visszavon() {
        if (index < 0 || rajz.indexOf(alakzat) >= 0) {
            throw new IllegalStateException("A torles nem vonhato vissza ebben az allapotban.");
        }
        rajz.beszur(Math.min(index, rajz.meret()), alakzat);
    }
}
