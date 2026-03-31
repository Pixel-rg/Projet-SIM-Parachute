package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light;

public class ResistanceAir {
    private double densiteAir = 1.225; //Fixe selon la hauteur du parachutiste
    private static final double SURFACECORPS = 0.6; // varie de 0.5 à 0.7
    private double surface;

    public Point2D calculForceResistanceAir( Parachutiste parachutiste){
        double vitesseVertical = parachutiste.vitesse.getY();
        double coefficientTrainee = parachutiste.getCoefficientTrainee();

        // Si le parachute est ouvert, on calcul la surface du parachute et on met le coefficient de traîné à 1,75. Sinon
        // on garde le coefficient à 1,0. Dans les deux cas on calcul la surface totale (parachute et parachutiste)
        if (!parachutiste.estOuvert()) {
            surface = SURFACECORPS;
        } else {
            surface = calculSurfaceParaOuvert(parachutiste);
            coefficientTrainee = 1.75;
        }

        // La force gravitionnelle est appliqué vers le bas et de sens opposé à la force verticale.
        // Le signe négatif permet de trouver la force totale qui permet de calculer l'accéleration
        double forceVertical = -0.5*densiteAir*coefficientTrainee*surface*Math.abs(Math.pow(vitesseVertical,2));
        return new Point2D(0, forceVertical);
    }

    //Calcul de la surface frontale lorsque le parachute est ouvert
    public double calculSurfaceParaOuvert(Parachutiste parachute){
        return SURFACECORPS + parachute.getSurface();
    }

    public double getSurface() {
        return surface;
    }
}
