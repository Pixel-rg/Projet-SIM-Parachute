package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
public class VueAnimation extends Pane {

    // --- CONSTANTES ---
    public static final double POSITIONX_PARAMETRE = 211;
    public static final double POSITIONX_STAT = 808;
    public static final double POSITIONY_TITRE = 65;
    public static final double POSITIONY_DEMARRER = 535;

    // Zone d'animation verticale sur l'écran
    private static final double Y_HAUT = 40;
    private static final double Y_BAS = 300;
    private static final double Y_SOL_FINAL = 400; // Position finale du para au sol

    // Position X fixe du parachutiste au centre de l'animation
    private static final double PARA_X = 185;
    private static final double PARA_WIDTH = 225;
    private static final double PARA_HEIGHT = 225;
    private static final double PARA_SOL_WIDTH = 222;
    private static final double PARA_SOL_HEIGHT = 125;

    private static final double SCREEN_WIDTH = 1080;
    private static final double SCREEN_HEIGHT = 720;

    // Constantes de simulation et seuils
    private static final double SEUIL_APPARITION_SOL = 720;
    private static final double SEUIL_NUAGES_DISPARAISSENT = 200;
    private static final double LIMITE_SORTIE_NUAGE = -150;
    private static final double OFFSET_NUAGE_INVISIBLITE = -500;
    private static final double FACTEUR_VITESSE_VISUELLE = 0.2;
    private static final double EFFET_NATUREL_AVION = 0.2;

    // --- ATTRIBUTS : IMAGES ET VUES ---
    private final Image background = new Image("Ciel.png");
    private final Image solAtterissage = new Image("Sol.png");
    private final Image parachuteFerme = new Image("ParachuteFerme.png");
    private final Image parachuteOuvert = new Image("ParachuteOuvert.png");
    private final Image parachuteAtterit = new Image("ParachutisteAtterit.png");

    private final ImageView cielVue = new ImageView(background);
    private final ImageView solVue = new ImageView(solAtterissage);
    private final ImageView parachutisteVue = new ImageView(parachuteFerme);

    // --- ATTRIBUTS : LOGIQUE ET OBJETS ---
    private final ArrayList<Nuage> nuages = new ArrayList<>();
    private final Avion avion = new Avion(new Point2D(PARA_X / 6, Y_HAUT), new Point2D(0, 0), new Point2D(0, 0));
    private boolean estAuSol = false;

    // --- CONSTRUCTEUR ---
    public VueAnimation() {
        initialiserDecors();
        initialiserParachutiste();
        spawnerNuages();

        // L'avion sera devant les nuages car ajouté après
        this.getChildren().add(avion.getImageView());
        this.getChildren().add(parachutisteVue);
    }

    // --- INITIALISATION ---
    private void initialiserDecors() {
        cielVue.setFitWidth(SCREEN_WIDTH);
        cielVue.setFitHeight(SCREEN_HEIGHT);

        // Le sol doit être initialement hors de vue ou tout en bas
        solVue.setFitWidth(SCREEN_WIDTH);
        solVue.setFitHeight(SCREEN_HEIGHT);
        solVue.setY(SCREEN_HEIGHT);

        this.getChildren().addAll(cielVue, solVue);
    }

    private void initialiserParachutiste() {
        parachutisteVue.setVisible(false); // On le voit pas au début (est dans l'avion)
        parachutisteVue.setFitWidth(PARA_WIDTH);
        parachutisteVue.setFitHeight(PARA_HEIGHT);
        parachutisteVue.setX(PARA_X);
        parachutisteVue.setY(Y_HAUT);
    }

    private void spawnerNuages() {
        for (int i = 0; i < 5; i++) {
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);
            double randomX = ThreadLocalRandom.current().nextDouble(211, 500);
            double randomY = ThreadLocalRandom.current().nextDouble(0, 300);

            Nuage n = new Nuage(new Point2D(randomX - 200, randomY), new Point2D(0, -randomVitesse), new Point2D(0, 0));
            nuages.add(n);
            this.getChildren().add(n.getImageView());
        }
    }

    public void update(double vitessePara, double altitude, double hautInit, double facteur) {
        double progress = 1.0 - (altitude / hautInit);
        double yEcran = Y_HAUT + progress * (Y_BAS - Y_HAUT);

        // Gestion de la position verticale
        if (!estAuSol) {
            parachutisteVue.setY(yEcran);
        }

        if (altitude <= 0) {
            parachutisteVue.setY(Y_SOL_FINAL);
            estAuSol = true;
        }

        // Faire apparaître le sol quand on approche de 0m
        if (altitude < SEUIL_APPARITION_SOL) {
            solVue.setY(525 - (10 - altitude) * 2 - solVue.getFitHeight()); // Le sol monte graduellement
        } else {
            solVue.setY(SCREEN_HEIGHT - solVue.getFitHeight());
        }

        mettreAJourPositionsObjets(vitessePara, altitude, facteur);
    }

    private void mettreAJourPositionsObjets(double vitessePara, double altitude, double facteur) {
        double vitesseDefilement = vitessePara * FACTEUR_VITESSE_VISUELLE * facteur;

        avion.getImageView().setY(avion.getImageView().getY() - vitesseDefilement + EFFET_NATUREL_AVION);

        // Déplacement des nuages
        for (Nuage n : nuages) {
            // 1. Les nuages montent
            n.getImageView().setY(n.getImageView().getY() - vitesseDefilement);

            // 2. Logique de réapparition (Respawn)
            if (n.getImageView().getY() < LIMITE_SORTIE_NUAGE) {
                if (altitude > SEUIL_NUAGES_DISPARAISSENT) {
                    n.getImageView().setY(SCREEN_HEIGHT);
                    n.getImageView().setX(ThreadLocalRandom.current().nextDouble(0, 800));
                } else {
                    n.getImageView().setY(OFFSET_NUAGE_INVISIBLITE);
                }
            }
        }
    }

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

    public void setVisibleSimulation(boolean visible) {
        parachutisteVue.setVisible(visible);
        avion.getImageView().setVisible(visible);
    }
}