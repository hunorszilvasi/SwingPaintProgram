package com.mycompany.swingpaintprogram.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

/** An unfilled rectangle, defined by two opposite corners. */
public class Teglalap extends Alakzat {

    private double x1;
    private double y1;
    private double x2;
    private double y2;

    public Teglalap(double x1, double y1, double x2, double y2, Color szin, float vonalvastagsag) {
        super(szin, vonalvastagsag);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void rajzol(Graphics2D g) {
        Graphics2D masolat = stilusozottMasolat(g);
        try {
            masolat.draw(alakzatGeometria());
        } finally {
            masolat.dispose();
        }
    }

    @Override
    public boolean tartalmaz(Point p) {
        Rectangle2D hatar = alakzatGeometria();
        double tolerancia = Math.max(4.0, getVonalvastagsag() / 2.0 + 2.0);
        return new Rectangle2D.Double(
                hatar.getX() - tolerancia,
                hatar.getY() - tolerancia,
                hatar.getWidth() + tolerancia * 2,
                hatar.getHeight() + tolerancia * 2).contains(p);
    }

    @Override
    public void eltol(int dx, int dy) {
        x1 += dx;
        x2 += dx;
        y1 += dy;
        y2 += dy;
    }

    private Rectangle2D alakzatGeometria() {
        return new Rectangle2D.Double(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x2 - x1), Math.abs(y2 - y1));
    }

    public double getX1() { return x1; }
    public double getY1() { return y1; }
    public double getX2() { return x2; }
    public double getY2() { return y2; }
}
