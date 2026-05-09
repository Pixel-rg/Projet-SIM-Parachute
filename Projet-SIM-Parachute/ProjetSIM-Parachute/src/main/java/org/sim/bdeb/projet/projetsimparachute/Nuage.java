package org.sim.bdeb.projet.projetsimparachute;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Nuage extends ObjetEnvironnant {
    private static final double LARGEUR_ECRAN = 1080;

    public Nuage(Point2D position, Point2D vitesse, Point2D acceleration) {
        super(position, vitesse, acceleration, "Nuage.png", 300, 150);
    }

    @Override
    public void update(double deltaTemps) {
        super.update(deltaTemps);

        // Faire spawn dans l'écran s'il dépasse
        if (position.getX() + largeur < 0) {
            this.position = new Point2D(LARGEUR_ECRAN, position.getY());
        }
    }
}