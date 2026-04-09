package org.sim.bdeb.projet.projetsimparachute;

import javafx.animation.AnimationTimer;

//Classe passerelle entre la vue(FenetrePrincipale) et la logique(Simulateur)
//Fourni les informations de la fenetrePrincipale jusqu'à la simulation
public class SimulationController {

    private AnimationTimer timer;
    private Simulateur simulateur;
    private FenetrePrincipale fenetre;

    //Paramètres modifiés par l'utilisateur qu'on doit communiquer à la simulation
    private double masseUtilisateur;
    private double surfaceUtilisateur;
    private double hauteurInitialeUtilisateur;

    //Paramètres qu'on doit communiquer du moteurPhysique au tableau des stats
    private double velocite;
    private double force;
    private double temps;

    //Boutons début et fin de la simulation
    private boolean simulationEnCours;

    public SimulationController(FenetrePrincipale fenetre) {

        this.fenetre = fenetre;


        timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;


                if (simulationEnCours) {
                    // On met à jour la physique/logique
                    simulateur.update(deltaTemps);
                    // On met à jour le dessin
                    fenetre.update();
                }

                dernierTemps = temps;
            }
        };
    }


    //Appeler quand on clique sur un bouton lancer
    public void lancerSimulation() {
        //créer un simulateur avec les paramètres de l'utilisateur

        this.simulationEnCours = true;
        simulateur = new Simulateur(masseUtilisateur,surfaceUtilisateur,hauteurInitialeUtilisateur);
        timer.start();
    }

    public void arreterSimulation() {
        // simulationEnCours = false;
        timer.stop();
        simulationEnCours = false;
    }

    public void setMasseUtilisateur(double masseUtilisateur) {
        this.masseUtilisateur = masseUtilisateur;
    }

    public void setSurfaceUtilisateur(double surfaceUtilisateur) {
        this.surfaceUtilisateur = surfaceUtilisateur;
    }

    public void setHauteurInitialeUtilisateur(double hauteurInitialeUtilisateur) {
        this.hauteurInitialeUtilisateur = hauteurInitialeUtilisateur;
    }

    //Quand le bouton "lancer" est appuyé, on met SimulationEnCours à true depuis la fenetrePrincipale
    public void setSimulationEnCours(boolean simulationEnCours) {
        this.simulationEnCours = simulationEnCours;
    }

    public boolean isSimulationEnCours() {
        return simulationEnCours;
    }


}
