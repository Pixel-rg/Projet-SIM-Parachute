package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

public class Parachutiste extends ObjetPhysique{
    //Zakarya
    private double masseTotale; //Masse de personnage + masse parachute, defini par lutilisateur
    private double surface;
    private double coefficientTrainee;
    private boolean parachuteOuvert;

    // il faudra avoir un coeff trainee fixe pour garder la situation plus realiste
    //Mikail: Oui, coefficient de trainée augmente dans le parachute est ouvert
    public Parachutiste(Point2D position, Point2D vitesse, double surface, double masse){
        super(position,vitesse);
        this.surface = surface;
        this.coefficientTrainee = 1;
        this.masseTotale = masseTotale;
    }

    public double getSurface() {
        return surface;
    }

    public double getMasse() {
        return masseTotale;
    }

    public void setMasseTotale(double masseTotale) {
        this.masseTotale = masseTotale;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public void setCoefficientTrainee(double coefficientTrainee) {
        this.coefficientTrainee = coefficientTrainee;
    }

    public double getCoefficientTrainee() {
        return coefficientTrainee;
    }

    public boolean estOuvert() {
        return parachuteOuvert;
    }
    public boolean ouvrirParachute() {
        return parachuteOuvert = true;
    }
}
