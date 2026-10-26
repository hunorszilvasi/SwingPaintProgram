package com.mycompany.swingpaintprogram.command;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

/** Executes commands and keeps undo and redo history. */
public class ParancsKezelo {

    private final Deque<Parancs> visszavonhato = new ArrayDeque<>();
    private final Deque<Parancs> ujrahajthato = new ArrayDeque<>();

    public void futtat(Parancs parancs) {
        Objects.requireNonNull(parancs, "parancs").vegrehajt();
        visszavonhato.push(parancs);
        ujrahajthato.clear();
    }

    public void visszavon() {
        if (visszavonhato.isEmpty()) {
            return;
        }
        Parancs parancs = visszavonhato.pop();
        try {
            parancs.visszavon();
            ujrahajthato.push(parancs);
        } catch (RuntimeException e) {
            visszavonhato.push(parancs);
            throw e;
        }
    }

    public void ujra() {
        if (ujrahajthato.isEmpty()) {
            return;
        }
        Parancs parancs = ujrahajthato.pop();
        try {
            parancs.vegrehajt();
            visszavonhato.push(parancs);
        } catch (RuntimeException e) {
            ujrahajthato.push(parancs);
            throw e;
        }
    }

    public boolean vanVisszavonhato() {
        return !visszavonhato.isEmpty();
    }

    public boolean vanUjrahajthato() {
        return !ujrahajthato.isEmpty();
    }
}
