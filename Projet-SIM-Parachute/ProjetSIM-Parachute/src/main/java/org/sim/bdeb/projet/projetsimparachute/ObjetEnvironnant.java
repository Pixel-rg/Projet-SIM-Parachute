package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class ObjetEnvironnant extends ObjetPhysique {
    protected double largeur;
    protected double hauteur;

    public ObjetEnvironnant(Point2D position, Point2D vitesse, Point2D acceleration, double largeur, double hauteur) {
        super(position, vitesse, acceleration);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

}
