package org.sim.bdeb.projet.projetsimparachute;

import javafx.animation.AnimationTimer;

public class SimulationController {

    private AnimationTimer timer;
    private Simulateur simulateur;
    private FenetrePrincipale fenetre;

    private double masseUtilisateur;
    private double surfaceUtilisateur;
    private double hauteurInitialeUtilisateur;

    private boolean simulationEnCours;

    public SimulationController(FenetrePrincipale fenetre) {
        this.fenetre = fenetre;

        timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {
                double deltaTemps = (temps - dernierTemps) * 1e-9;
                dernierTemps = temps;

                if (simulationEnCours && simulateur != null) {
                    simulateur.update(deltaTemps);
                    fenetre.update();

                    // Arrêter quand le parachutiste touche le sol (position Y >= altitude initiale)
                    if (simulateur.getParachutiste().getPosition().getY() >= hauteurInitialeUtilisateur) {
                        arreterSimulation();
                        fenetre.onAterissage();
                    }
                }
            }
        };
    }

    public void lancerSimulation() {
        this.simulationEnCours = true;
        simulateur = new Simulateur(masseUtilisateur, hauteurInitialeUtilisateur, surfaceUtilisateur);
        timer.start();
    }

    public void arreterSimulation() {
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

    public void setSimulationEnCours(boolean simulationEnCours) {
        this.simulationEnCours = simulationEnCours;
    }

    public boolean isSimulationEnCours() {
        return simulationEnCours;
    }

    // Getters pour la vue et les stats
    public double getVitesseParachutiste() {
        if (simulateur == null) return 0;
        return simulateur.getParachutiste().getVitesse().getY();
    }

    public boolean getParachuteOuvert() {
        if (simulateur == null) return false;
        return simulateur.getParachutiste().estOuvert();
    }

    public double getAltitude() {
        if (simulateur == null) return 0;
        // Position Y du parachutiste = distance parcourue depuis le départ
        // Altitude restante = hauteur initiale - distance parcourue
        return Math.max(0, hauteurInitialeUtilisateur - simulateur.getParachutiste().getPosition().getY());
    }

    public double getTempsTotal() {
        if (simulateur == null) return 0;
        return simulateur.getTempsTotal();
    }

    public double getForce() {
        if (simulateur == null) return 0;
        return simulateur.getParachutiste().getMasse() * 9.81; // approximation F = mg
    }

    public double getHauteurInitiale() {
        return hauteurInitialeUtilisateur;
    }
}