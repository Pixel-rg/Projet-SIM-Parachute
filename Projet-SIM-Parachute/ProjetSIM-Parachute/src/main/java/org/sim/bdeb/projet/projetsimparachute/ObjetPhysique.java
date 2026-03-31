package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;


public abstract class ObjetPhysique {
    //Jalal
    protected Point2D position;
    protected Point2D vitesse;

    public ObjetPhysique(Point2D position, Point2D vitesse) {
        this.position = position;
        this.vitesse = vitesse;

    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void setVitesse(Point2D vitesse) {
        this.vitesse = vitesse;
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getVitesse() {
        return vitesse;
    }
}
