package org.sim.bdeb.projet.projetsimparachute;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;

//Classe passerelle entre la vue(FenetrePrincipale) et la logique(Simulateur)
//Fourni les informations de la fenetrePrincipale jusqu'à la simulation

public class SimulationController {

    private AnimationTimer timer;
    private double multiplicateurVitesse = 1.0; // Par défaut à 1x
    private Simulateur simulateur;
    private FenetrePrincipale fenetre;

    //Paramètres modifiés par l'utilisateur qu'on doit communiquer à la simulation
    private double masseUtilisateur;
    private double surfaceUtilisateur;
    private double hauteurInitialeUtilisateur;

    //Boutons début et fin de la simulation
    private boolean simulationEnCours;
    private boolean aReintialise = true;
    private boolean resetChrono;
    private int nbCliquerDemarrer;

    public SimulationController(FenetrePrincipale fenetre) {
        this.fenetre = fenetre;

        timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                // On synchronise dernierTemps avec le temps actuel de JavaFX.
                if (resetChrono) {
                    dernierTemps = temps;
                    resetChrono = false;
                    return;
                }

                // Calcul du delta temps
                double deltaTemps = (temps - dernierTemps) * 1e-9;
                dernierTemps = temps;

                double deltaTempsSimule = deltaTemps * multiplicateurVitesse;

                // Mise à jour de la physique
                if (simulateur != null) {
                    simulateur.update(deltaTempsSimule);
                    fenetre.update();

                    if (simulateur.getParachutiste().getPosition().getY() >= hauteurInitialeUtilisateur) {
                        arreterSimulation();
                        fenetre.onAterissage();
                    }
                }
            }
        };
    }


    //Appeler quand on clique sur un bouton démarrer
    public void lancerSimulation() {
        nbCliquerDemarrer++;

        this.simulationEnCours = true;


        // FORCE la réinitialisation du chrono pour la prochaine frame du timer
        this.resetChrono = true;

        fenetre.getParametres().setPeutEcrire(false);
        fenetre.getParametres().empecherEcrire();

        fenetre.getVueAnimation().setVisibleSimulation(true); //Afficher le parachutiste

        if (aReintialise) {
            simulateur = new Simulateur(masseUtilisateur, hauteurInitialeUtilisateur, surfaceUtilisateur);
        }


        aReintialise = false;

        timer.start();
    }

    public void arreterSimulation() {
        timer.stop();
        simulationEnCours = false;
    }

    public void reinitialiserPhysique() {
        // 1. Reset des paramètres de configuration (Force l'utilisateur à retaper)
        this.masseUtilisateur = 0;
        this.surfaceUtilisateur = 0;
        this.hauteurInitialeUtilisateur = 0;

        // 2. Reset du temps et du moteur
        simulateur.setTempsTotal(0);
        this.simulationEnCours = false;
        this.resetChrono = true;

        // 3. Reset du Parachutiste (L'objet physique)
        Parachutiste parachutiste = simulateur.getParachutiste();
        parachutiste.setVitesse(new Point2D(0, 0));
        parachutiste.setPosition(new Point2D(0, 0)); // Il revient au sol (0,0)
        parachutiste.setParachuteOuvert(false);

        // Attention : Ne mets pas la surface à 0 ici si elle est utilisée
        // immédiatement dans un calcul de division dans le moteur.
        parachutiste.setSurface(0);
        parachutiste.setCoefficientTrainee(0);
    }

    // changer la vitesse de simulation
    public void setMultiplicateurVitesse(double facteur) {
        this.multiplicateurVitesse = facteur;
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

    //La méthode suivante permet à la classe FenetrePrincipale d'accéder au temps optimal.
    //Si le simulateur n'est pas encore créé (avant de mettre Démarrer), elle retourne 0.
    public double getTempsOptimal() {
        if (simulateur == null) return 0;
        return simulateur.getTempsOptimal();
    }

    public double getMultiplicateurVitesse() {
        return multiplicateurVitesse;
    }

    public Point2D getAccelerationParachutiste(){
        return simulateur.getParachutiste().getAcceleration();
    }
}