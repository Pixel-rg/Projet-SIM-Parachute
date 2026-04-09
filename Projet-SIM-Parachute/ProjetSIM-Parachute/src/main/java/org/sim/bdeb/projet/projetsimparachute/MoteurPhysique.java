package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

import static org.sim.bdeb.projet.projetsimparachute.Gravite.GRAVITE;


public class MoteurPhysique {
    private Gravite gravite;
    private ResistanceAir resistanceAir;
    private static final double VITESSESECURITAIRE = 56;
    private double tempsOptimal;
    private double tempsTotal = 0;
    private double surface = 0.6;

    public MoteurPhysique() {
        //Abishanth, Jalal et Zakarya
        gravite = new Gravite();
        resistanceAir =new ResistanceAir();
    }

    public void update(Parachutiste parachutiste, double temps){

        tempsTotal = tempsTotal + temps;

        Point2D forceTotale = new Point2D(0,0);
        forceTotale = forceTotale.add(gravite.calculForceGravite(parachutiste));
        forceTotale = forceTotale.add(resistanceAir.calculForceResistanceAir(parachutiste));

        // Calcul de l'accélération du parachutiste par la 2em loi de Newton F = ma
        Point2D acceleration =  forceTotale.multiply(1.0/ parachutiste.getMasse());

        parachutiste.setVitesse(parachutiste.vitesse.add(acceleration.multiply(temps)));
        parachutiste.setPosition(parachutiste.position.add(parachutiste.vitesse.multiply(temps)));

        // Saisir le temps optimal d'ouvrir la parachute
        // Abishanth: J' ai aussi penser à tester à chaque instant pour trouver le temps optimal mais je ne suis pas sur
        if (!parachutiste.estOuvert() && parachutiste.vitesse.getY() >= VITESSESECURITAIRE) {
            parachutiste.ouvrirParachute();
            tempsOptimal = tempsTotal;
        } else if (!parachutiste.estOuvert() && parachutiste.vitesse.getY() >= calculerVitesseTerminale(parachutiste)){
            parachutiste.ouvrirParachute();
            tempsOptimal = tempsTotal;
        }
    }

    // Proposition de Zack. Abishanth et Zack ont codé cette partie


    public double calculerVitesseTerminale(Parachutiste parachutiste) {
        double grav = GRAVITE;

        // rho ici c standard on peut changer selon la temperature.
        double rho = 1.225;
        double masse = parachutiste.getMasse();
        double cd = parachutiste.getCoefficientTrainee();

        // Surface totale : corps (0.6 m²) + parachute si ouvert
        // Abishanth: aucune idée comment ca marche.

        // CODE DE ZACK

        // double surfaceTotale = 0.6 + (parachute.estOuvert() ? parachute.getSurface() : 0);
        //if (surfaceTotale <= 0 || cd <= 0) return 0;

        //Abishanth:  Solution ??

        if (!parachutiste.estOuvert()) {
            surface = resistanceAir.getSurface();
        }

        return Math.sqrt((2 * masse * grav) / (rho * surface * cd));
    }

}
