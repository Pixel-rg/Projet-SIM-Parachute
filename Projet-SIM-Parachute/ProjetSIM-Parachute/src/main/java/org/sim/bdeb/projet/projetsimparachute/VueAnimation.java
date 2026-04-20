package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VueAnimation extends Pane {

    private ArrayList<Nuage> nuages = new ArrayList<>();
    private Image background = new Image("Ciel.png");
    private ImageView cielVue = new ImageView(background);

    private Image parachuteFerme = new Image("ParachuteFerme.png");
    private Image parachuteOuvert = new Image("ParachuteOuvert.png");
    private ImageView parachutisteVue = new ImageView(parachuteFerme);

    public static final double POSITIONX_PARAMETRE = 211;
    public static final double POSITIONX_STAT = 808;
    public static final double POSITIONY_TITRE = 65;
    public static final double POSITIONY_DEMARRER = 535;

    // Zone d'animation verticale sur l'écran (en pixels)
    private static final double Y_HAUT = 40;
    private static final double Y_BAS = 430;

    // Position X fixe du parachutiste au centre de l'animation
    private static final double PARA_X = 185;
    private static final double PARA_WIDTH = 225;

    public VueAnimation() {
        cielVue.setFitWidth(1080);
        cielVue.setFitHeight(720);
        this.getChildren().add(cielVue);

        spawnerNuages();

        parachutisteVue.setFitWidth(PARA_WIDTH);
        parachutisteVue.setFitHeight(225);
        parachutisteVue.setX(PARA_X);
        parachutisteVue.setY(Y_HAUT);
        this.getChildren().add(parachutisteVue);
    }

    private void spawnerNuages() {
        for (int i = 0; i < 5; i++) {
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);
            double randomX = ThreadLocalRandom.current().nextDouble(211, 500);
            double randomY = ThreadLocalRandom.current().nextDouble(0,300);
            Nuage n = new Nuage(new Point2D(randomX - 200, randomY), new Point2D(0, -randomVitesse));
            nuages.add(n);
            this.getChildren().add(n.getNuageView());
        }
    }

    /**
     * Met à jour la scène.
     * @param vitesseParachutiste vitesse physique (m/s) — utilisée pour la vitesse des nuages
     * @param altitudeActuelle    altitude actuelle du parachutiste (m)
     * @param hauteurInitiale     altitude de départ (m)
     */
    public void update(double vitesseParachutiste, double altitudeActuelle, double hauteurInitiale) {
        // Déplacer le parachutiste visuellement (0 = haut de l'écran, hauteurInitiale = bas)
        double progress = 1.0 - (altitudeActuelle / hauteurInitiale); // 0.0 (départ) → 1.0 (sol)
        double yEcran = Y_HAUT + progress * (Y_BAS - Y_HAUT);
        parachutisteVue.setY(yEcran);

        // Nuages montent à une vitesse proportionnelle à la vitesse du parachutiste
        double vitesseNuages = Math.max(0.5, vitesseParachutiste * 0.02);

        List<Nuage> horsEcran = new ArrayList<>();

        for (Nuage n : nuages) {
            n.setVitesse(new Point2D(0, -vitesseNuages));
            n.update();
            if (n.getPosition().getY() < POSITIONY_TITRE - 200) {
                horsEcran.add(n);
            }
        }

        for (Nuage n : horsEcran) {
            this.getChildren().remove(n.getNuageView());
            nuages.remove(n);

            double randomX = ThreadLocalRandom.current().nextDouble(POSITIONX_PARAMETRE - 250, POSITIONX_STAT - 250);
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);
            Nuage nouveau = new Nuage(new Point2D(randomX, 540), new Point2D(0, -vitesseNuages *0.02));
            nuages.add(nouveau);
            this.getChildren().add(nouveau.getNuageView());
        }
    }

    public void dessinerParachutiste(boolean paraOuvert) {
        parachutisteVue.setX(PARA_X);
        if (paraOuvert) {
            parachutisteVue.setImage(parachuteOuvert);
        } else {
            parachutisteVue.setImage(parachuteFerme);
        }
    }
}