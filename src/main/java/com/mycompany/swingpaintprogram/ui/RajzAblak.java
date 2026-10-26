package com.mycompany.swingpaintprogram.ui;

import com.mycompany.swingpaintprogram.command.ParancsKezelo;
import com.mycompany.swingpaintprogram.model.Alakzat;
import com.mycompany.swingpaintprogram.model.Rajz;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

/** Main application window and toolbar. */
public class RajzAblak extends JFrame {

    private final ParancsKezelo parancsKezelo = new ParancsKezelo();
    private final Rajz rajz = new Rajz();
    private final JButton visszavonasGomb = new JButton("Visszavonás");
    private final JButton ujraGomb = new JButton("Újra");
    private final JButton szinGomb = new JButton("Szín");
    private final JLabel allapotCimke = new JLabel("Válassz eszközt, majd rajzolj a vászonra.");
    private final RajzVaszon vaszon;

    public RajzAblak() {
        super("Egyszerű vektoros rajzprogram");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        vaszon = new RajzVaszon(rajz, parancsKezelo, this::kijelolesFrissitese, this::vegrehajtasFrissitese);
        add(eszkOztarLetrehozasa(), BorderLayout.NORTH);
        add(vaszon, BorderLayout.CENTER);
        add(allapotsorLetrehozasa(), BorderLayout.SOUTH);
        billentyuParancsokBeallitasa();
        vegrehajtasFrissitese();
        pack();
        setLocationRelativeTo(null);
    }

    private JToolBar eszkOztarLetrehozasa() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        ButtonGroup eszkozCsoport = new ButtonGroup();
        eszkozGomb(toolbar, eszkozCsoport, "Kijelölés", RajzEszkoz.KIJELOLES, true);
        eszkozGomb(toolbar, eszkozCsoport, "Vonal", RajzEszkoz.VONAL, false);
        eszkozGomb(toolbar, eszkozCsoport, "Téglalap", RajzEszkoz.TEGLALAP, false);
        eszkozGomb(toolbar, eszkozCsoport, "Ellipszis", RajzEszkoz.ELLIPSZIS, false);
        toolbar.addSeparator();
        szinGomb.setFocusable(false);
        szinGomb.setBackground(vaszon.getAktualisSzin());
        szinGomb.setForeground(Color.WHITE);
        szinGomb.addActionListener(e -> szinValasztasa());
        toolbar.add(szinGomb);
        JButton torlesGomb = new JButton("Törlés");
        torlesGomb.setFocusable(false);
        torlesGomb.setToolTipText("Kijelölt alakzat törlése (Delete)");
        torlesGomb.addActionListener(e -> vaszon.torolKijeloltet());
        toolbar.add(torlesGomb);
        toolbar.addSeparator();
        visszavonasGomb.setFocusable(false);
        visszavonasGomb.addActionListener(e -> vaszon.visszavon());
        toolbar.add(visszavonasGomb);
        ujraGomb.setFocusable(false);
        ujraGomb.addActionListener(e -> vaszon.ujra());
        toolbar.add(ujraGomb);
        return toolbar;
    }

    private void eszkozGomb(JToolBar toolbar, ButtonGroup csoport, String felirat, RajzEszkoz eszkoz, boolean kijelolt) {
        JToggleButton gomb = new JToggleButton(felirat, kijelolt);
        gomb.setFocusable(false);
        gomb.addActionListener(e -> {
            vaszon.setEszkoz(eszkoz);
            allapotCimke.setText(eszkoz == RajzEszkoz.KIJELOLES
                    ? "Kattints egy alakzatra a kijelöléshez és mozgatáshoz."
                    : felirat + " rajzolása: húzd az egeret a vásznon.");
            vaszon.requestFocusInWindow();
        });
        csoport.add(gomb);
        toolbar.add(gomb);
    }

    private JPanel allapotsorLetrehozasa() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        panel.add(allapotCimke);
        return panel;
    }

    private void billentyuParancsokBeallitasa() {
        kotBillentyuhoz("undo", KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), vaszon::visszavon);
        kotBillentyuhoz("redo", KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), vaszon::ujra);
        kotBillentyuhoz("delete", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), vaszon::torolKijeloltet);
    }

    private void kotBillentyuhoz(String nev, KeyStroke billentyu, Runnable muvelet) {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                muvelet.run();
            }
        };
        vaszon.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(billentyu, nev);
        vaszon.getActionMap().put(nev, action);
    }

    private void szinValasztasa() {
        Color szin = JColorChooser.showDialog(this, "Szín kiválasztása", vaszon.getAktualisSzin());
        if (szin != null) {
            vaszon.setAktualisSzin(szin);
            szinGomb.setBackground(szin);
            szinGomb.setForeground(szin.getRed() + szin.getGreen() + szin.getBlue() < 384 ? Color.WHITE : Color.BLACK);
        }
    }

    private void kijelolesFrissitese(Alakzat alakzat) {
        allapotCimke.setText(alakzat == null
                ? "Nincs kijelölt alakzat."
                : "Alakzat kijelölve. Mozgatás: húzás. Törlés: Delete.");
    }

    private void vegrehajtasFrissitese() {
        visszavonasGomb.setEnabled(parancsKezelo.vanVisszavonhato());
        ujraGomb.setEnabled(parancsKezelo.vanUjrahajthato());
    }
}
