package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Avion extends ObjetPhysique {
    Image avion = new Image("Avion.png");
    ImageView avionView = new ImageView(avion);

    private static final double LARGEUR_ECRAN = 1080;
    private static final double LARGEUR_AVION = 623;

    public Avion(Point2D position, Point2D vitesse, Point2D acceleration) {
        super(position, vitesse, acceleration);
        avionView.setFitWidth(LARGEUR_AVION);
        avionView.setFitHeight(313);
        avionView.setX(position.getX());
        avionView.setY(position.getY());
    }

    public void update(double deltaTemps) {
        super.update(deltaTemps);
        position = position.add(vitesse);

        if (position.getX() + LARGEUR_AVION < 0) {
            return;
        }

        avionView.setX(position.getX());
        avionView.setY(position.getY());
    }

    public ImageView getAvionView() {
        return avionView;
    }

}
