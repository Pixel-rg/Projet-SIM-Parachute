package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;



//AJOUTER ATTRIBUTS image afin de ne pas faire de copier collé entre les classes
//parachutiste, nuage et avion
public abstract class ObjetPhysique {
    //Jalal
    protected Point2D position;
    protected Point2D vitesse;
    protected Point2D acceleration;

    public ObjetPhysique(Point2D position, Point2D vitesse, Point2D acceleration) {
        this.position = position;
        this.vitesse = vitesse;
        this.acceleration = acceleration;
    }

    public void update(double temps) {

        this.vitesse = this.vitesse.add(acceleration.multiply(temps));
        this.position = this.position.add(vitesse.multiply(temps));
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    public void setVitesse(Point2D vitesse) {
        this.vitesse = vitesse;
    }
    public void setAcceleration(Point2D acceleration) {
        this.acceleration = acceleration;
    }

    public Point2D getAcceleration() {
        return acceleration;
    }

    public Point2D getPosition() {
        return position;
    }

    public Point2D getVitesse() {
        return vitesse;
    }
}
