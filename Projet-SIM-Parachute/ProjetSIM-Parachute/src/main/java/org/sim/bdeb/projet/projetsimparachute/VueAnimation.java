package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
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



    public VueAnimation() {
        CielVue.setFitWidth(1080);
        CielVue.setFitHeight(720);
        this.getChildren().add(CielVue);
        spawnerNuages();
    }

    private void spawnerNuages() {
        // code pour nuages
        for (int i = 0; i < 5; i++) {
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);
            double randomX = ThreadLocalRandom.current().nextDouble(211, 500);
            Nuage n = new Nuage(new Point2D(randomX - 200, 535), new Point2D(0, -randomVitesse)); // négatif = monte, a CHANGER APRES
            nuages.add(n);
            this.getChildren().add(n.getNuageView());
        }
    }

    public void update() {
        //Gerer hors-ecran nuages
        List<Nuage> horsEcran = new ArrayList<>();

        for (Nuage n : nuages) {
            n.update();
            if (n.getPosition().getY() < POSITIONY_TITRE - 200 ) {
                horsEcran.add(n);
            }

        }

        for (Nuage n : horsEcran) {
            this.getChildren().remove(n.getNuageView());
            nuages.remove(n);

            // Respawn en bas
            double randomX = ThreadLocalRandom.current().nextDouble(POSITIONX_PARAMETRE-250, POSITIONX_STAT-250);
            double randomY = 540;
            double randomVitesse = ThreadLocalRandom.current().nextDouble(0.5, 2.5);

            Nuage nouveau = new Nuage(new Point2D(randomX, 540), new Point2D(0, -randomVitesse)); // a changer, set to vitesse parchutiste lors de la chute
            nuages.add(nouveau);
            this.getChildren().add(nouveau.getNuageView());
        }
    }

        //this.getChildren().add(corpsParachutiste);


    public void dessinerParachutiste(double x, double y) {

    }
}