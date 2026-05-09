package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

public class Avion extends ObjetEnvironnant {

    public Avion(Point2D position, Point2D vitesse, Point2D acceleration) {

        super(position, vitesse, acceleration, "Avion.png", 623, 313);
    }

    @Override
    public void update(double deltaTemps) {
        super.update(deltaTemps);

    }
}