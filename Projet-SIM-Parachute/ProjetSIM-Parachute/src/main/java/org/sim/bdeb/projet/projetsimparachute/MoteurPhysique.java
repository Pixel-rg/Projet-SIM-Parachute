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
        gravite = new Gravite();
        resistanceAir = new ResistanceAir();
    }

    public void update(Parachutiste parachutiste, double temps) {
        tempsTotal += temps;

        Point2D forceTotale = new Point2D(0, 0);
        forceTotale = forceTotale.add(gravite.calculForceGravite(parachutiste));
        forceTotale = forceTotale.add(resistanceAir.calculForceResistanceAir(parachutiste));

        Point2D acceleration = forceTotale.multiply(1.0 / parachutiste.getMasse());

        parachutiste.setVitesse(parachutiste.vitesse.add(acceleration.multiply(temps)));
        parachutiste.setPosition(parachutiste.position.add(parachutiste.vitesse.multiply(temps)));

        // Ouvrir le parachute automatiquement si vitesse dépasse seuil sécuritaire
        if (!parachutiste.estOuvert() && parachutiste.vitesse.getY() >= VITESSESECURITAIRE) {
            parachutiste.ouvrirParachute();
            tempsOptimal = tempsTotal;
        } else if (!parachutiste.estOuvert() && parachutiste.vitesse.getY() >= calculerVitesseTerminale(parachutiste)) {
            parachutiste.ouvrirParachute();
            tempsOptimal = tempsTotal;
        }
    }

    public double calculerVitesseTerminale(Parachutiste parachutiste) {
        double rho = 1.225;
        double masse = parachutiste.getMasse();
        double cd = parachutiste.getCoefficientTrainee();

        if (!parachutiste.estOuvert()) {
            surface = resistanceAir.getSurface();
        }

        return Math.sqrt((2 * masse * GRAVITE) / (rho * surface * cd));
    }

    public double getTempsTotal() {
        return tempsTotal;
    }

    public double getTempsOptimal() {
        return tempsOptimal;
    }
}