package com.mycompany.swingpaintprogram.ui;

import com.mycompany.swingpaintprogram.command.HozzaadParancs;
import com.mycompany.swingpaintprogram.command.MozgatParancs;
import com.mycompany.swingpaintprogram.command.ParancsKezelo;
import com.mycompany.swingpaintprogram.command.SzinValtoParancs;
import com.mycompany.swingpaintprogram.command.TorolParancs;
import com.mycompany.swingpaintprogram.model.Alakzat;
import com.mycompany.swingpaintprogram.model.Ellipszis;
import com.mycompany.swingpaintprogram.model.Rajz;
import com.mycompany.swingpaintprogram.model.Teglalap;
import com.mycompany.swingpaintprogram.model.Vonal;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.geom.Rectangle2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Swing view and mouse interaction for the model-backed drawing area. */
public class RajzVaszon extends JPanel {

    private final Rajz rajz;
    private final ParancsKezelo parancsKezelo;
    private final Consumer<Alakzat> kijelolesValtozott;
    private final Runnable allapotValtozott;

    private RajzEszkoz eszkoz = RajzEszkoz.KIJELOLES;
    private Color aktualisSzin = Color.BLACK;
    private Alakzat kijelolt;
    private Point kezdoPont;
    private Point utolsoPont;
    private Alakzat mozgatott;
    private int mozgatasiDx;
    private int mozgatasiDy;

    public RajzVaszon(Rajz rajz, ParancsKezelo parancsKezelo,
            Consumer<Alakzat> kijelolesValtozott, Runnable allapotValtozott) {
        this.rajz = rajz;
        this.parancsKezelo = parancsKezelo;
        this.kijelolesValtozott = kijelolesValtozott;
        this.allapotValtozott = allapotValtozott;
        setBackground(Color.WHITE);
        setOpaque(true);
        setFocusable(true);
        setPreferredSize(new java.awt.Dimension(900, 600));

        MouseAdapter egerKezelo = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                kezdoPont = e.getPoint();
                utolsoPont = e.getPoint();
                if (eszkoz == RajzEszkoz.KIJELOLES) {
                    kijelolesBeallitasa(talalat(kezdoPont));
                    mozgatott = kijelolt;
                    mozgatasiDx = 0;
                    mozgatasiDy = 0;
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (kezdoPont == null) {
                    return;
                }
                if (eszkoz == RajzEszkoz.KIJELOLES && mozgatott != null) {
                    int dx = e.getX() - utolsoPont.x;
                    int dy = e.getY() - utolsoPont.y;
                    mozgatott.eltol(dx, dy);
                    mozgatasiDx += dx;
                    mozgatasiDy += dy;
                    utolsoPont = e.getPoint();
                } else {
                    utolsoPont = e.getPoint();
                }
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (kezdoPont == null) {
                    return;
                }
                if (eszkoz == RajzEszkoz.KIJELOLES) {
                    if (mozgatott != null && utolsoPont != null) {
                        int dx = e.getX() - utolsoPont.x;
                        int dy = e.getY() - utolsoPont.y;
                        mozgatott.eltol(dx, dy);
                        mozgatasiDx += dx;
                        mozgatasiDy += dy;
                    }
                    if (mozgatott != null && (mozgatasiDx != 0 || mozgatasiDy != 0)) {
                        mozgatott.eltol(-mozgatasiDx, -mozgatasiDy);
                        parancsKezelo.futtat(new MozgatParancs(mozgatott, mozgatasiDx, mozgatasiDy));
                        allapotValtozott.run();
                    }
                } else {
                    Alakzat ujAlakzat = ujAlakzat(kezdoPont, e.getPoint());
                    parancsKezelo.futtat(new HozzaadParancs(rajz, ujAlakzat));
                    kijelolesBeallitasa(ujAlakzat);
                    allapotValtozott.run();
                }
                kezdoPont = null;
                utolsoPont = null;
                mozgatott = null;
                repaint();
            }
        };
        addMouseListener(egerKezelo);
        addMouseMotionListener(egerKezelo);
    }

    public void setEszkoz(RajzEszkoz eszkoz) {
        this.eszkoz = eszkoz;
        setCursor(eszkoz == RajzEszkoz.KIJELOLES
                ? java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR)
                : java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));
    }

    public void setAktualisSzin(Color szin) {
        aktualisSzin = szin;
        if (kijelolt != null && !kijelolt.getSzin().equals(szin)) {
            parancsKezelo.futtat(new SzinValtoParancs(kijelolt, szin));
            allapotValtozott.run();
            repaint();
        }
    }

    public Color getAktualisSzin() {
        return aktualisSzin;
    }

    public void torolKijeloltet() {
        if (kijelolt == null) {
            return;
        }
        Alakzat torlendo = kijelolt;
        kijelolesBeallitasa(null);
        parancsKezelo.futtat(new TorolParancs(rajz, torlendo));
        allapotValtozott.run();
        repaint();
    }

    public void visszavon() {
        parancsKezelo.visszavon();
        kijelolesBeallitasa(null);
        allapotValtozott.run();
        repaint();
    }

    public void ujra() {
        parancsKezelo.ujra();
        kijelolesBeallitasa(null);
        allapotValtozott.run();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Alakzat alakzat : rajz.getAlakzatok()) {
                alakzat.rajzol(g2);
            }
            if (kezdoPont != null && eszkoz != RajzEszkoz.KIJELOLES) {
                Alakzat eloNezet = ujAlakzat(kezdoPont, utolsoPont == null ? kezdoPont : utolsoPont);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.65f));
                eloNezet.rajzol(g2);
            }
            if (kijelolt != null) {
                g2.setColor(new Color(40, 115, 220));
                g2.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                        10.0f, new float[]{5.0f, 4.0f}, 0.0f));
                g2.draw(kijelolesiKeret(kijelolt));
            }
        } finally {
            g2.dispose();
        }
    }

    private void kijelolesBeallitasa(Alakzat alakzat) {
        if (kijelolt != alakzat) {
            kijelolt = alakzat;
            kijelolesValtozott.accept(alakzat);
        }
    }

    private Alakzat talalat(Point pont) {
        List<Alakzat> alakzatok = rajz.getAlakzatok();
        for (int i = alakzatok.size() - 1; i >= 0; i--) {
            Alakzat alakzat = alakzatok.get(i);
            if (alakzat.tartalmaz(pont)) {
                return alakzat;
            }
        }
        return null;
    }

    private Alakzat ujAlakzat(Point kezdo, Point veg) {
        return switch (eszkoz) {
            case VONAL -> new Vonal(kezdo.x, kezdo.y, veg.x, veg.y, aktualisSzin, 2.0f);
            case TEGLALAP -> new Teglalap(kezdo.x, kezdo.y, veg.x, veg.y, aktualisSzin, 2.0f);
            case ELLIPSZIS -> new Ellipszis(kezdo.x, kezdo.y, veg.x, veg.y, aktualisSzin, 2.0f);
            case KIJELOLES -> throw new IllegalStateException("Kijelolesi eszkoz nem rajzol alakzatot.");
        };
    }

    private Rectangle2D kijelolesiKeret(Alakzat alakzat) {
        double x1;
        double y1;
        double x2;
        double y2;
        if (alakzat instanceof Vonal vonal) {
            x1 = vonal.getX1();
            y1 = vonal.getY1();
            x2 = vonal.getX2();
            y2 = vonal.getY2();
        } else if (alakzat instanceof Teglalap teglalap) {
            x1 = teglalap.getX1();
            y1 = teglalap.getY1();
            x2 = teglalap.getX2();
            y2 = teglalap.getY2();
        } else if (alakzat instanceof Ellipszis ellipszis) {
            x1 = ellipszis.getX1();
            y1 = ellipszis.getY1();
            x2 = ellipszis.getX2();
            y2 = ellipszis.getY2();
        } else {
            return new Rectangle2D.Double();
        }
        double padding = 5;
        double x = Math.min(x1, x2) - padding;
        double y = Math.min(y1, y2) - padding;
        double width = Math.max(Math.abs(x2 - x1), 1) + padding * 2;
        double height = Math.max(Math.abs(y2 - y1), 1) + padding * 2;
        return new Rectangle2D.Double(x, y, width, height);
    }
}
