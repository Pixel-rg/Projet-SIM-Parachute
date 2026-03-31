package org.sim.bdeb.projet.projetsimparachute;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Nuage extends ObjetPhysique {
    Image nuage = new Image("Nuage.png");
    ImageView nuageView = new ImageView(nuage);

    private static final double LARGEUR_ECRAN = 1080;
    private static final double LARGEUR_NUAGE = 300;

    public Nuage(Point2D position, Point2D vitesse) {
        super(position, vitesse);
        nuageView.setFitWidth(LARGEUR_NUAGE);
        nuageView.setFitHeight(150);
        nuageView.setX(position.getX());
        nuageView.setY(position.getY());
    }

    public void update() {
        position = position.add(vitesse);

        if (position.getX() + LARGEUR_NUAGE < 0) {
            position = new Point2D(LARGEUR_ECRAN, position.getY());
        }

        nuageView.setX(position.getX());
        nuageView.setY(position.getY());
    }

    public ImageView getNuageView() {
        return nuageView;
    }
}