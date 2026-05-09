package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ObjetEnvironnant extends ObjetPhysique {
    protected ImageView imageView;
    protected double largeur;
    protected double hauteur;

    public ObjetEnvironnant(Point2D position, Point2D vitesse, Point2D acceleration, String imagePath, double largeur, double hauteur) {
        super(position, vitesse, acceleration);

        this.largeur = largeur;
        this.hauteur = hauteur;

        // Centralisation du chargement de l'image
        Image image = new Image(imagePath);
        this.imageView = new ImageView(image);

        // Configuration de base
        this.imageView.setFitWidth(largeur);
        this.imageView.setFitHeight(hauteur);
        this.imageView.setX(position.getX());
        this.imageView.setY(position.getY());
    }

    @Override
    public void update(double deltaTemps) {
        super.update(deltaTemps); // Appelle la physique (position = position + vitesse)

        // Synchronise toujours la vue sur la position physique
        imageView.setX(position.getX());
        imageView.setY(position.getY());
    }

    public ImageView getImageView() {
        return imageView;
    }
}
