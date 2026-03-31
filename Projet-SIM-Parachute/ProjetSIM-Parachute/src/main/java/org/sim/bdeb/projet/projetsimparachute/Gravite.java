package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

public class Gravite{

    // la gravite sur Terre

    // Mis la constante public pour acceder à la valeur. calcul de vitesse terminale
    public static final double GRAVITE = 9.81;

    public Point2D calculForceGravite(Parachutiste parachutiste){
        return new Point2D(0, parachutiste.getMasse() * GRAVITE);
    }
}
