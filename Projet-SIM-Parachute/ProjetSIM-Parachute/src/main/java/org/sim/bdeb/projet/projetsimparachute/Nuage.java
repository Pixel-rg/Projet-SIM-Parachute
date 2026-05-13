package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Nuage extends ObjetEnvironnant {
    private static final double LARGEUR_ECRAN = 1080;

    public Nuage(Point2D position, Point2D vitesse, Point2D acceleration) {
        // Définit une taille fixe de 300x150
        super(position, vitesse, acceleration, 300, 150);
    }

    @Override
    public void update(double deltaTemps) {
        // Applique le mouvement physique (position += vitesse * temps)
        super.update(deltaTemps);

        // Si le nuage sort complètement par la gauche (x + largeur < 0),
        // on le fait réapparaître à l'extrême droite de l'écran.
        if (position.getX() + largeur < 0) {
            this.position = new Point2D(LARGEUR_ECRAN, position.getY());
        }
    }
}