package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Classe gérant la représentation graphique de la simulation (Vue).
 * Elle hérite de Pane pour permettre le placement libre des éléments.
 */
public class VueAnimation extends Pane {

    // --- CONSTANTES DE POSITIONNEMENT ET DE DIMENSIONS ---
    public static final double POSITIONX_PARAMETRE = 211;
    public static final double POSITIONX_STAT = 808;
    public static final double POSITIONY_TITRE = 65;
    public static final double POSITIONY_DEMARRER = 535;

    // Délimitation de la zone de mouvement du parachutiste à l'écran
    private static final double Y_HAUT = 40;
    private static final double Y_BAS = 300;
    private static final double Y_SOL_FINAL = 400; // Position d'arrêt au sol

    // Configuration visuelle du parachutiste (X fixe pour simuler la chute verticale)
    private static final double PARA_X = 185;
    private static final double PARA_WIDTH = 225;
    private static final double PARA_HEIGHT = 225;
    private static final double PARA_SOL_WIDTH = 222;
    private static final double PARA_SOL_HEIGHT = 125;

    // Résolution de la fenêtre d'animation
    private static final double SCREEN_WIDTH = 1080;
    private static final double SCREEN_HEIGHT = 720;

    // Seuils logiques pour les effets visuels (apparition du sol, recyclage des nuages)
    private static final double SEUIL_APPARITION_SOL = 720;
    private static final double SEUIL_NUAGES_DISPARAISSENT = 200;
    private static final double LIMITE_SORTIE_NUAGE = -150;
    private static final double OFFSET_NUAGE_INVISIBLITE = -500;

    // Facteurs pour rendre le mouvement fluide et naturel
    private static final double FACTEUR_VITESSE_VISUELLE = 0.2;
    private static final double EFFET_NATUREL_AVION = 0.2;

    // --- ATTRIBUTS : IMAGES (RESSOURCES) ET VUES ---
    private final Image background = new Image("Ciel.png");
    private final Image solAtterissage = new Image("Sol.png");
    private final Image parachuteFerme = new Image("ParachuteFerme.png");
    private final Image parachuteOuvert = new Image("ParachuteOuvert.png");
    private final Image parachuteAtterit = new Image("ParachutisteAtterit.png");

    private final ImageView cielVue = new ImageView(background);
    private final ImageView solVue = new ImageView(solAtterissage);
    private final ImageView parachutisteVue = new ImageView(parachuteFerme);

    // --- DICTIONNAIRE DE LIAISON MVC ---
    // Associer un objet (modèle) à son image (View)
    private final Map<ObjetEnvironnant, ImageView> vuesObjets = new HashMap<>();

    private Avion avion;
    private ArrayList<Nuage> nuages = new ArrayList<>();
    private boolean estAuSol = false;

    // --- CONSTRUCTEUR ---
    public VueAnimation() {
        initialiserDecors();
        initialiserParachutiste();
        creerNuages();

        // Initialisation de l'avion
        this.avion = new Avion(new Point2D(PARA_X / 6, Y_HAUT), new Point2D(0, 0), new Point2D(0, 0));
        lierNouveauVisuel(avion, "Avion.png");

        // Ajout du parachutiste en dernier pour qu'il apparaisse au-dessus des décors
        this.getChildren().add(parachutisteVue);
    }

    /**
     * Associer à chaque objet un ImageView et l'enregistrer dans le dictionnaire.
     * modèle = L'objet (Avion, Nuage)
     * cheminImage = Le nom du fichier image
     */
    private void lierNouveauVisuel(ObjetEnvironnant modele, String cheminImage) {
        ImageView iv = new ImageView(new Image(cheminImage));
        iv.setFitWidth(modele.largeur);
        iv.setFitHeight(modele.hauteur);
        iv.setX(modele.getPosition().getX());
        iv.setY(modele.getPosition().getY());

        vuesObjets.put(modele, iv); // Enregistrement du lien
        this.getChildren().add(iv); // Ajout au Pane
    }

    private void initialiserDecors() {
        cielVue.setFitWidth(SCREEN_WIDTH);
        cielVue.setFitHeight(SCREEN_HEIGHT);
        solVue.setFitWidth(SCREEN_WIDTH);
        solVue.setFitHeight(SCREEN_HEIGHT);
        solVue.setY(SCREEN_HEIGHT); // Le sol commence "sous" l'écran
        this.getChildren().addAll(cielVue, solVue);
    }

    private void initialiserParachutiste() {
        parachutisteVue.setVisible(false);
        parachutisteVue.setFitWidth(PARA_WIDTH);
        parachutisteVue.setFitHeight(PARA_HEIGHT);
        parachutisteVue.setX(PARA_X);
        parachutisteVue.setY(Y_HAUT);
    }

    private void creerNuages() {
        for (int i = 0; i < 5; i++) {
            // Génération de positions et vitesses aléatoires pour le réalisme
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);
            double randomX = ThreadLocalRandom.current().nextDouble(211, 500);
            double randomY = ThreadLocalRandom.current().nextDouble(0, 300);

            Nuage n = new Nuage(new Point2D(randomX - 200, randomY), new Point2D(0, -randomVitesse), new Point2D(0, 0));
            nuages.add(n);
            lierNouveauVisuel(n, "Nuage.png"); //Lier le nuage à son ImageView qui le représente
        }
    }

    /**
     * Met à jour l'ensemble de l'animation à chaque frame.
     */
    public void update(double vitessePara, double altitude, double hautInit, double facteur) {
        // Calcul de la progression de la chute (0.0 à 1.0)
        double progress = 1.0 - (altitude / hautInit);
        // Translation de la progression vers une position Y à l'écran
        double yEcran = Y_HAUT + progress * (Y_BAS - Y_HAUT);

        if (!estAuSol) {
            parachutisteVue.setY(yEcran);
        }

        // Détection de l'atterrissage
        if (altitude <= 0) {
            parachutisteVue.setY(Y_SOL_FINAL);
            estAuSol = true;
        }

        // Animation de l'ascension du sol
        if (altitude < SEUIL_APPARITION_SOL) {
            solVue.setY(525 - (10 - altitude) * 2 - solVue.getFitHeight());
        } else {
            solVue.setY(SCREEN_HEIGHT - solVue.getFitHeight());
        }

        // Mise à jour de la logique des objets environnants et synchronisation visuelle
        mettreAJourPositionsObjets(vitessePara, altitude, facteur);
        synchroniserVisuels();
    }

    // Calcule le déplacement des objets du décor dans le modèle physique.
    private void mettreAJourPositionsObjets(double vitessePara, double altitude, double facteur) {
        // Calcul de la vitesse à laquelle le décor défile
        double vitesseDefilement = vitessePara * FACTEUR_VITESSE_VISUELLE * facteur;

        // Mise à jour de la position de l'avion (il s'éloigne vers le haut)
        avion.setPosition(new Point2D(avion.getPosition().getX(),
                avion.getPosition().getY() - vitesseDefilement + EFFET_NATUREL_AVION));

        for (Nuage n : nuages) {
            // Déplacement vertical des nuages
            n.setPosition(new Point2D(n.getPosition().getX(), n.getPosition().getY() - vitesseDefilement));

            // Logique de "Respawn" des nuages pour une simulation infinie
            if (n.getPosition().getY() < LIMITE_SORTIE_NUAGE) {
                if (altitude > SEUIL_NUAGES_DISPARAISSENT) {
                    // Réapparaissent en bas avec un X aléatoire
                    n.setPosition(new Point2D(ThreadLocalRandom.current().nextDouble(0, 800), SCREEN_HEIGHT));
                } else {
                    // Deviennent invisibles si on est trop près du sol
                    n.setPosition(new Point2D(n.getPosition().getX(), OFFSET_NUAGE_INVISIBLITE));
                }
            }
        }
    }


    // Synchronise les ImageView de la scène avec leurs coordonnées
    private void synchroniserVisuels() {
        for (Map.Entry<ObjetEnvironnant, ImageView> entry : vuesObjets.entrySet()) {
            ObjetEnvironnant modele = entry.getKey();
            ImageView vue = entry.getValue();

            // On applique la position calculée dans le modèle à la vue
            vue.setX(modele.getPosition().getX());
            vue.setY(modele.getPosition().getY());
        }
    }

    //Change l'image du parachutiste selon son état physique.
    public void dessinerParachutiste(boolean paraOuvert) {
        parachutisteVue.setX(PARA_X);

        if (estAuSol) {
            parachutisteVue.setFitHeight(PARA_SOL_HEIGHT);
            parachutisteVue.setFitWidth(PARA_SOL_WIDTH);
            parachutisteVue.setImage(parachuteAtterit);
        } else {
            parachutisteVue.setFitHeight(PARA_HEIGHT);
            if (paraOuvert) {
                parachutisteVue.setImage(parachuteOuvert);
            } else {
                parachutisteVue.setImage(parachuteFerme);
            }
        }
    }

    /**
     * Affiche ou masque les éléments lors du début ou de la fin de simulation.
     */
    public void setVisibleSimulation(boolean visible) {
        parachutisteVue.setVisible(visible);

        ImageView ivAvion = vuesObjets.get(avion);
        if (ivAvion != null) ivAvion.setVisible(visible);
    }
}