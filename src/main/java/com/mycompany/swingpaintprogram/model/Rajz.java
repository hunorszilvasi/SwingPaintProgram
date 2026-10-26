package com.mycompany.swingpaintprogram.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** The drawing document. It has no dependency on Swing components. */
public class Rajz {

    private final List<Alakzat> alakzatok = new ArrayList<>();

    public List<Alakzat> getAlakzatok() {
        return Collections.unmodifiableList(alakzatok);
    }

    public void hozzaad(Alakzat alakzat) {
        alakzatok.add(Objects.requireNonNull(alakzat, "alakzat"));
    }

    public void beszur(int index, Alakzat alakzat) {
        alakzatok.add(index, Objects.requireNonNull(alakzat, "alakzat"));
    }

    public boolean torol(Alakzat alakzat) {
        return alakzatok.remove(alakzat);
    }

    public int indexOf(Alakzat alakzat) {
        return alakzatok.indexOf(alakzat);
    }

    public int meret() {
        return alakzatok.size();
    }
}
