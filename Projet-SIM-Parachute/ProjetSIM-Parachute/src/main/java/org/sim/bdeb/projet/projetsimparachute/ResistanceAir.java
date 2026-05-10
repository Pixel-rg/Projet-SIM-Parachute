package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.effect.Light;

public class ResistanceAir {
    private double densiteAir = 1.225; //https://www.isobudgets.com/fr/moist-air-density/
    private static final double SURFACECORPS = 0.7; // https://www.sciencefacts.net/terminal-velocity-of-a-human.html
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
        //Source: https://engineerexcel.com/air-resistance-calculator/
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
