package com.mycompany.swingpaintprogram;

import com.mycompany.swingpaintprogram.ui.RajzAblak;
import javax.swing.SwingUtilities;

public class SwingPaintProgram {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RajzAblak().setVisible(true));
    }
}
