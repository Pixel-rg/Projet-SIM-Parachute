package org.sim.bdeb.projet.projetsimparachute;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.layout.VBox;
//Classe passerelle entre la vue(FenetrePrincipale) et la logique(Simulateur)
//Fourni les informations de la fenetrePrincipale jusqu'à la simulation

public class SimulationController {

    // --- ATTRIBUTS DE LOGIQUE ET TEMPS ---
    private AnimationTimer timer;
    private double multiplicateurVitesse = 1.0; // Par défaut à 1x
    private Simulateur simulateur;
    private FenetrePrincipale fenetre;

    // --- PARAMÈTRES UTILISATEUR ---
    //Paramètres modifiés par l'utilisateur qu'on doit communiquer à la simulation
    private double masseUtilisateur;
    private double surfaceUtilisateur;
    private double hauteurInitialeUtilisateur;

    // --- ÉTAT DE LA SIMULATION ---
    //Boutons début et fin de la simulation
    private boolean simulationEnCours;
    private boolean aReintialise = true;
    private boolean resetChrono;
    private int nbCliquerDemarrer;

    // --- CONSTRUCTEUR ---
    public SimulationController(FenetrePrincipale fenetre) {
        this.fenetre = fenetre;

        timer = new AnimationTimer() {
            long dernierTemps = System.nanoTime();

            @Override
            public void handle(long temps) {

                // 1. Synchronisation du chrono au démarrage ou après un reset
                if (resetChrono) {
                    dernierTemps = temps;
                    resetChrono = false;
                    return;
                }

                // 2. Calcul du deltaTemps réel (le temps écoulé entre deux rafraîchissements d'écran)
                // 1e-9 convertit les nanosecondes en secondes (environ 0.016s à 60 FPS)
                double deltaTempsReel = (temps - dernierTemps) * 1e-9;
                dernierTemps = temps;

                // --- SOLUTION AU PROBLÈME DU Infini :
                //Le x10 rendait l'accélération folle et explosait les valeurs

                // On définit combien de fois on veut répéter la simulation pendant cette frame.
                // Si multiplicateurVitesse = 10, on va faire 10 petites mises à jour physiques.
                int nombreDePas = (int) multiplicateurVitesse;

                if (simulateur != null) {
                    for (int i = 0; i < nombreDePas; i++) {
                        // On fait avancer la physique par petits bonds stables
                        // On passe 'deltaTempsReel' et non le temps multiplié !
                        simulateur.update(deltaTempsReel, getAltitude());

                        // Vérification de l'atterrissage à chaque petit pas
                        if (simulateur.getParachutiste().getPosition().getY() >= hauteurInitialeUtilisateur) {
                            arreterSimulation();
                            fenetre.onAterissage();
                            break; // On sort de la boucle for si on touche le sol
                        }
                    }

                    // 3. Mise à jour visuelle (une seule fois après les calculs physiques)
                    // Cela permet de garder les stats fluides sans saccades
                    fenetre.update();
                }
            }
        };
    }

    // --- GESTION DU CYCLE DE VIE (START, STOP, RESET) ---

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
            if (masseUtilisateur <= 0) masseUtilisateur = 70; // Valeur par défaut pour éviter le crash

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
        this.timer.stop();
        this.simulationEnCours = false;
        this.aReintialise = true;
        setMultiplicateurVitesse(1.0); // On le remet au défault
        this.resetChrono = true;
        this.nbCliquerDemarrer = 0;

        // On détruit l'ancien simulateur pour libérer la mémoire
        this.simulateur = null;

        // On remet les paramètres par défaut
        this.masseUtilisateur = 0;
        this.surfaceUtilisateur = 0;
        this.hauteurInitialeUtilisateur = 1000;

    }

    // --- SETTERS ---

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

    // --- GETTERS (POUR LA VUE ET LES STATISTIQUES) ---

    public boolean isSimulationEnCours() {
        return simulationEnCours;
    }

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

}