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

    //Boutons début et fin de la simulation
    private boolean simulationEnCours;
    private boolean boutonDebuterSimulation = false;
    private boolean boutonPressed = false;

    public SimulationController(FenetrePrincipale fenetre) {

        this.fenetre = fenetre;


        timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                double deltaTemps = (temps - dernierTemps) * 1e-9;

                //Solution temporaire: Mikail
                if(boutonDebuterSimulation) {
                    lancerSimulation();
                    boutonDebuterSimulation = false;
                    simulationEnCours = true;
                    boutonPressed = true;

                    if (!boutonPressed) {
                        simulationEnCours = false;
                    }
                }
                fenetre.getVueAnimation().update();
                if(simulationEnCours){
                    simulateur.update(deltaTemps);
                }


                if(simulationEnCours){
                    //Abishanth:  Mauvais place?Cette methode est lie plus avec le button LANCER
                    lancerSimulation();
                }
                else {
                    arreterSimulation();
                }

                // Abishanth:J'assume simlateur.update c'est lorsque simulation est en cours? Donc, faudra le mettre dans le cas if

                //simlateur.update(deltaTemps);

                dernierTemps = temps;
            }
        };
    }

    //Appeler quand on clique sur un bouton lancer
    public void lancerSimulation() {
        //créer un simulateur avec les paramètres de l'utilisateur

        // Abishanth: uhhh on cree des simulateur à chaque instant? Il faut juste un seul.
        simulateur = new Simulateur(masseUtilisateur,surfaceUtilisateur,hauteurInitialeUtilisateur);
        // correction: simulationEnCours = true;
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
}
