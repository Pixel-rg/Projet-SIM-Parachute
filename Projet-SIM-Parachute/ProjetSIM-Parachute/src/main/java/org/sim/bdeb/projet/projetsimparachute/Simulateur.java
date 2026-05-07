package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

public class Simulateur {
    private Parachutiste parachutiste;
    private MoteurPhysique moteurPhysique;

    public Simulateur(double masseChoisie, double hauteurInitiale, double surfacePara) {
        this.parachutiste = new Parachutiste(
                new Point2D(0, 0),   // position Y = 0 (distance parcourue depuis départ)
                new Point2D(0, 0),
                new Point2D(0,0),
                surfacePara,
                masseChoisie
        );
        this.moteurPhysique = new MoteurPhysique();

    }
    // ABISHANTH: J AI UN METHODE POUR PRENDRE ALTITUDE LIVE
    public void update(double deltaTemps, double altitude) {
        moteurPhysique.update(parachutiste, deltaTemps,altitude);
    }

    public Parachutiste getParachutiste() {
        return parachutiste;
    }

    // Exposer le temps total du moteur physique
    public double getTempsTotal() {
        return moteurPhysique.getTempsTotal();
    }

    public void setTempsTotal(double tempsTotal){
        moteurPhysique.setTempsTotal(tempsTotal);
    }


    // La méthode suivante permet à SimulationController d'accéder à la donnée calculé dans le MoteurPhysique
    public double getTempsOptimal(){
        return moteurPhysique.getTempsOptimal();
    }
}