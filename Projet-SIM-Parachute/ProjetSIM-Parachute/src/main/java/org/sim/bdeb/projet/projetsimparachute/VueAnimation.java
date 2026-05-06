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
    private Image solAtterissage = new Image("Sol.png");
    private ImageView cielVue = new ImageView(background);
    private ImageView solVue = new ImageView(solAtterissage);

    private Avion avion = new Avion(new Point2D(PARA_X/6,Y_HAUT),new Point2D(0,0),new Point2D(0,0));

    private Image parachuteFerme = new Image("ParachuteFerme.png");
    private Image parachuteOuvert = new Image("ParachuteOuvert.png");
    private ImageView parachutisteVue = new ImageView(parachuteFerme);

    public static final double POSITIONX_PARAMETRE = 211;
    public static final double POSITIONX_STAT = 808;
    public static final double POSITIONY_TITRE = 65;
    public static final double POSITIONY_DEMARRER = 535;

    // Zone d'animation verticale sur l'écran
    private static final double Y_HAUT = 40;
    private static final double Y_BAS = 430;

    // Position X fixe du parachutiste au centre de l'animation
    private static final double PARA_X = 185;
    private static final double PARA_WIDTH = 225;

    public VueAnimation() {

        parachutisteVue.setVisible(false); //On le voit pas au début (est dans l'avion)

        cielVue.setFitWidth(1080);
        cielVue.setFitHeight(720);

        // Le sol doit être initialement hors de vue ou tout en bas
        solVue.setFitWidth(1080);
        solVue.setFitHeight(200); // Réduire la hauteur
        solVue.setY(720);         // Le placer sous l'écran au début

        //le dernier ajouté est au-dessus
        this.getChildren().addAll(cielVue, solVue);

        spawnerNuages();

        // L'avion sera devant les nuages car ajouté après
        this.getChildren().add(avion.getAvionView());

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
            Nuage n = new Nuage(new Point2D(randomX - 200, randomY), new Point2D(0, -randomVitesse),new Point2D(0,0));
            nuages.add(n);
            this.getChildren().add(n.getNuageView());
        }
    }


    public void update(double vitessePara, double alt, double hautInit, double facteur) {
        double progress = 1.0 - (alt / hautInit);
        double yEcran = Y_HAUT + progress * (Y_BAS - Y_HAUT);
        parachutisteVue.setY(yEcran);

        // Faire apparaître le sol quand on approche de 0m
        if (alt < 100) {
            solVue.setY(720 - (100 - alt) * 2); // Le sol monte graduellement
        } else {
            solVue.setY(720);
        }

        double vitesseDefilement = vitessePara * 0.2 * facteur;
        avion.getAvionView().setY(avion.getAvionView().getY() - vitesseDefilement + 0.2); //+0,2 pour effet naturel

        // Déplacement des nuages
        double vitesseVisuelle = vitessePara * 0.2 * facteur;
        for (Nuage n : nuages) {
            n.getNuageView().setY(n.getNuageView().getY() - vitesseVisuelle);
            if (n.getNuageView().getY() < -150) {
                n.getNuageView().setY(720);
                n.getNuageView().setX(ThreadLocalRandom.current().nextDouble(0, 800));
            }
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

    //afficher parachutiste quand on lance la simulation
    public void setVisibleSimulation(boolean visible) {
        parachutisteVue.setVisible(visible);
        avion.getAvionView().setVisible(visible);
    }


}