package com.mycompany.swingpaintprogram.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

/** A drawable, movable vector shape in the drawing model. */
public abstract class Alakzat {

    private Color szin;
    private float vonalvastagsag;

    protected Alakzat(Color szin, float vonalvastagsag) {
        this.szin = java.util.Objects.requireNonNull(szin, "szin");
        setVonalvastagsag(vonalvastagsag);
    }

    public abstract void rajzol(Graphics2D g);

    public abstract boolean tartalmaz(Point p);

    public abstract void eltol(int dx, int dy);

    public Color getSzin() {
        return szin;
    }

    public void setSzin(Color szin) {
        this.szin = java.util.Objects.requireNonNull(szin, "szin");
    }

    public float getVonalvastagsag() {
        return vonalvastagsag;
    }

    public void setVonalvastagsag(float vonalvastagsag) {
        if (!Float.isFinite(vonalvastagsag) || vonalvastagsag <= 0) {
            throw new IllegalArgumentException("A vonalvastagsagnak pozitivnak kell lennie.");
        }
        this.vonalvastagsag = vonalvastagsag;
    }

    protected Graphics2D stilusozottMasolat(Graphics2D g) {
        Graphics2D masolat = (Graphics2D) g.create();
        masolat.setColor(szin);
        masolat.setStroke(new BasicStroke(vonalvastagsag, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        return masolat;
    }
}
