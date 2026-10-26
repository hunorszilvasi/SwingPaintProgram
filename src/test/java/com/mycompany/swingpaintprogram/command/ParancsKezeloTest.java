package com.mycompany.swingpaintprogram.command;

import com.mycompany.swingpaintprogram.model.Rajz;
import com.mycompany.swingpaintprogram.model.Teglalap;
import java.awt.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParancsKezeloTest {

    @Test
    void vegrehajtVisszavonEsUjraHozzaadasnal() {
        Rajz rajz = new Rajz();
        Teglalap teglalap = new Teglalap(0, 0, 20, 20, Color.BLACK, 2);
        ParancsKezelo kezelo = new ParancsKezelo();

        kezelo.futtat(new HozzaadParancs(rajz, teglalap));
        assertEquals(1, rajz.meret());
        kezelo.visszavon();
        assertEquals(0, rajz.meret());
        kezelo.ujra();
        assertEquals(1, rajz.meret());
    }

    @Test
    void ujParancsUritjaAzUjraVeremet() {
        Rajz rajz = new Rajz();
        ParancsKezelo kezelo = new ParancsKezelo();
        kezelo.futtat(new HozzaadParancs(rajz, new Teglalap(0, 0, 10, 10, Color.BLACK, 1)));
        kezelo.visszavon();

        kezelo.futtat(new HozzaadParancs(rajz, new Teglalap(20, 20, 30, 30, Color.BLUE, 1)));

        assertFalse(kezelo.vanUjrahajthato());
        assertEquals(1, rajz.meret());
    }

    @Test
    void uresElmenyekNemValtoztatjakMegAzAllapotot() {
        Rajz rajz = new Rajz();
        ParancsKezelo kezelo = new ParancsKezelo();

        kezelo.visszavon();
        kezelo.ujra();

        assertEquals(0, rajz.meret());
        assertFalse(kezelo.vanVisszavonhato());
        assertFalse(kezelo.vanUjrahajthato());
    }

    @Test
    void torlesVisszavonasaMegorziAzAlakzatSorrendjet() {
        Rajz rajz = new Rajz();
        Teglalap elso = new Teglalap(0, 0, 10, 10, Color.BLACK, 1);
        Teglalap masodik = new Teglalap(20, 20, 30, 30, Color.BLUE, 1);
        rajz.hozzaad(elso);
        rajz.hozzaad(masodik);
        ParancsKezelo kezelo = new ParancsKezelo();

        kezelo.futtat(new TorolParancs(rajz, elso));
        kezelo.visszavon();

        assertEquals(elso, rajz.getAlakzatok().get(0));
        assertEquals(masodik, rajz.getAlakzatok().get(1));
    }

    @Test
    void mozgatasVisszavonasaEsUjraVegrehajtasa() {
        Teglalap teglalap = new Teglalap(5, 6, 20, 30, Color.BLACK, 1);
        ParancsKezelo kezelo = new ParancsKezelo();
        MozgatParancs parancs = new MozgatParancs(teglalap, 7, -3);

        kezelo.futtat(parancs);
        assertEquals(12, teglalap.getX1());
        assertEquals(3, teglalap.getY1());
        kezelo.visszavon();
        assertEquals(5, teglalap.getX1());
        assertEquals(6, teglalap.getY1());
        kezelo.ujra();
        assertEquals(12, teglalap.getX1());
        assertEquals(3, teglalap.getY1());
    }

    @Test
    void szinvaltoztatasVisszavonhato() {
        Teglalap teglalap = new Teglalap(0, 0, 10, 10, Color.BLACK, 1);
        ParancsKezelo kezelo = new ParancsKezelo();

        kezelo.futtat(new SzinValtoParancs(teglalap, Color.RED));
        assertEquals(Color.RED, teglalap.getSzin());
        kezelo.visszavon();
        assertEquals(Color.BLACK, teglalap.getSzin());
    }

    @Test
    void ujraVegrehajthatoTorlesVisszaallitjaAzAlakzatot() {
        Rajz rajz = new Rajz();
        Teglalap teglalap = new Teglalap(0, 0, 10, 10, Color.BLACK, 1);
        rajz.hozzaad(teglalap);
        ParancsKezelo kezelo = new ParancsKezelo();

        kezelo.futtat(new TorolParancs(rajz, teglalap));
        kezelo.visszavon();
        kezelo.ujra();

        assertTrue(rajz.getAlakzatok().isEmpty());
    }
}
