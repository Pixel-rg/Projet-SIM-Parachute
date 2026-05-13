package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

//Classe qui décrit les objets du décor
public abstract class ObjetEnvironnant extends ObjetPhysique {
    protected double largeur;
    protected double hauteur;

    public ObjetEnvironnant(Point2D position, Point2D vitesse, Point2D acceleration, double largeur, double hauteur) {
        super(position, vitesse, acceleration);
        this.largeur = largeur;
        this.hauteur = hauteur;
    }

}
