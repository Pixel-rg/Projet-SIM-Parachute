package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

public class Simulateur {
    private Parachutiste parachutiste;
    private MoteurPhysique moteurPhysique;

    public Simulateur(double masseChoisie, double hauteurInitiale, double surfacePara) {
        this.parachutiste = new Parachutiste(
                new Point2D(0, 0),   // position Y = 0 (distance parcourue depuis départ)
                new Point2D(0, 0),
                surfacePara,
                masseChoisie
        );
        this.moteurPhysique = new MoteurPhysique();
    }

    public void update(double deltaTemps) {
        moteurPhysique.update(parachutiste, deltaTemps);
    }

    public Parachutiste getParachutiste() {
        return parachutiste;
    }

    // Exposer le temps total du moteur physique
    public double getTempsTotal() {
        return moteurPhysique.getTempsTotal();
    }


    // La méthode suivante permet à SimulationController d'accéder à la donnée calculé dans le MoteurPhysique
    public double getTempsOptimal(){
        return moteurPhysique.getTempsOptimal();
    }
}