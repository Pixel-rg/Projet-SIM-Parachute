package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
public class Simulateur {
    //Zakarya et Abishanth
    private Parachutiste parachutiste;
    private MoteurPhysique moteurPhysique;

    public Simulateur(double masseChoisie, double hauteurInitiale, double surfacePara) {
        // 1. Initialisation du parachutiste à l'altitude donnée
        // On lui donne une surface et un coefficient de traînée (ex: 1.75 pour un parachute)
        this.parachutiste = new Parachutiste(new Point2D(0, hauteurInitiale), new Point2D(0,0), surfacePara, 50);
        // 3. Le moteur qui fera les calculs
        this.moteurPhysique = new MoteurPhysique();
    }



    /**
     * Cette méthode sera appelée 60 fois par seconde par le SimulationController
     */
    public void update(double deltaTemps) {
        // On demande au moteur de calculer la nouvelle position du parachutiste
        moteurPhysique.update(parachutiste, deltaTemps);

        // On synchronise la position du parachute sur celle du parachutiste
        parachutiste.setPosition(parachutiste.getPosition());
    }

    // Getters pour que la Vue puisse savoir où dessiner le cercle
    public Parachutiste getParachutiste() { return parachutiste; }
}