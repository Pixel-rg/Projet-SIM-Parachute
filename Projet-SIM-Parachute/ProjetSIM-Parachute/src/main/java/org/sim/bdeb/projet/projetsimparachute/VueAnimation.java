package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class VueAnimation extends Pane {

    private Image[] parachutiste;
    private ImageView[]parachutisteView;


    private Image nuageImage;
    private ImageView nuage;
    private ArrayList<Nuage> nuages = new ArrayList<>();
    private Image background = new Image("Ciel.png");
    private ImageView CielVue = new ImageView(background);
    public static final double  POSITIONX_PARAMETRE = 211;
    public static final double POSITIONX_STAT = 808;

    public static final double POSITIONY_TITRE = 65;
    public static final double POSITIONY_DEMARRER = 535;

    private double randomX = ThreadLocalRandom.current().nextDouble(POSITIONX_PARAMETRE, POSITIONX_STAT);
    private double randomY = ThreadLocalRandom.current().nextDouble(POSITIONY_TITRE, POSITIONY_DEMARRER);

    public VueAnimation() {
        CielVue.setFitWidth(1080);
        CielVue.setFitHeight(720);
        this.getChildren().add(CielVue);
        spawnerNuages();
    }

    private void spawnerNuages() {
        for (int i = 0; i < 3; i++) {
            double randomX = ThreadLocalRandom.current().nextDouble(POSITIONX_PARAMETRE, POSITIONX_STAT);
            double randomY = ThreadLocalRandom.current().nextDouble(POSITIONY_TITRE, POSITIONY_DEMARRER);
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);

            Nuage n = new Nuage(new Point2D(randomX, randomY), new Point2D(-randomVitesse, 0));
            nuages.add(n);
            this.getChildren().add(n.getNuageView());
        }
    }

    public void update() {
        boolean offscreen = false;

        for (Nuage n : nuages) {
            n.update();
            if (n.getPosition().getX() + 300 < 0) {
                offscreen = true;
            }
        }

        if (offscreen) {
            for (Nuage n : nuages) {
                this.getChildren().remove(n.getNuageView());
            }
            nuages.clear();
            spawnerNuages();
        }
    }

        //this.getChildren().add(corpsParachutiste);


    public void dessinerParachutiste(double x, double y) {

    }
}