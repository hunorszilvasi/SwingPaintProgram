package com.mycompany.swingpaintprogram.command;

import com.mycompany.swingpaintprogram.model.Alakzat;
import java.util.Objects;

/** Moves a shape by a stored delta; undo applies the inverse delta. */
public class MozgatParancs implements Parancs {

    private final Alakzat alakzat;
    private final int dx;
    private final int dy;

    public MozgatParancs(Alakzat alakzat, int dx, int dy) {
        this.alakzat = Objects.requireNonNull(alakzat, "alakzat");
        Math.negateExact(dx);
        Math.negateExact(dy);
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void vegrehajt() {
        alakzat.eltol(dx, dy);
    }

    @Override
    public void visszavon() {
        alakzat.eltol(Math.negateExact(dx), Math.negateExact(dy));
    }
}
