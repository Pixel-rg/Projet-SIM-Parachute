package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

public class Parachutiste extends ObjetPhysique {
    private double masseTotale; //Masse de personnage + masse parachute, defini par l'utilisateur
    private double surface;
    private double coefficientTrainee;
    private boolean parachuteOuvert;

    public Parachutiste(Point2D position, Point2D vitesse, Point2D acceleration, double surface, double masse) {
        super(position, vitesse, acceleration);
        this.surface = surface;
        this.coefficientTrainee = 1;
        this.masseTotale = masse;
    }


    public double getSurface() {
        return surface;
    }

    public double getMasse() {
        return masseTotale;
    }

    public double getCoefficientTrainee() {
        return coefficientTrainee;
    }

    // Vérifie si le parachute est ouvert ou fermé
    public boolean estOuvert() {
        return parachuteOuvert;
    }

    public boolean ouvrirParachute() {
        return parachuteOuvert = true;
    }

}
