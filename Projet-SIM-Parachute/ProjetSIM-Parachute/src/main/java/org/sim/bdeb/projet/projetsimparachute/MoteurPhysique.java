package org.sim.bdeb.projet.projetsimparachute;

import javafx.geometry.Point2D;

import static org.sim.bdeb.projet.projetsimparachute.Gravite.GRAVITE;

public class MoteurPhysique {
    private Gravite gravite;
    private ResistanceAir resistanceAir;
    //Source de la vitesse sécuritaire (m/s):
    //https://parachutevoltige.com/fr/blogue/faits-divers/les-5-mythes-les-plus-repandus-dans-le-monde-du-parachutisme/#:~:text=La%20vitesse%20terminale%20de%20chute,ou%20de%201000%20pieds%2Fminute.
    private static final double VITESSESECURITAIRE = 56;
    private double tempsOptimal;
    private double tempsTotal = 0;
    private double surface = 0.6;
    private double altitude;



    public MoteurPhysique() {
        gravite = new Gravite();
        resistanceAir = new ResistanceAir();
    }

    public void update(Parachutiste parachutiste, double temps,double altitudeActuelle) {

            this.tempsTotal += temps;

        Point2D forceTotale = new Point2D(0, 0);
        forceTotale = forceTotale.add(gravite.calculForceGravite(parachutiste));
        forceTotale = forceTotale.add(resistanceAir.calculForceResistanceAir(parachutiste));

        Point2D acceleration = forceTotale.multiply(1.0 / parachutiste.getMasse());

        parachutiste.setAcceleration(acceleration);
        parachutiste.update(temps);

        // Ouvrir le parachute automatiquement si vitesse dépasse seuil sécuritaire
        // Dès que la vitesse du parachutiste atteint la vitesse sécuritaire ou celle de la
        // vitesse terminale du parachutiste, on applique
        // à cet instant précis la variable du temps optimal.

        //ABISHANTH: IDK WHERE THE BUG IS PLS HELP CLASS I CHANGED WAS METHODS : MOTEURPHYS, SIMULATEUR, SIMCONTROLLER,UPDATE PHYSIQUE

        if (!parachutiste.estOuvert() && altitudeActuelle <= calculerAltitudeMin(parachutiste)) {
            System.out.println(altitudeActuelle);
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
        // Source: https://tpeps7.wordpress.com/2015/01/12/la-resistance-de-lair/
        return Math.sqrt((2 * masse * GRAVITE) / (rho * surface * cd));
    }

    public double calculerAltitudeMin(Parachutiste parachutiste) {
        double vTerminale = calculerVitesseTerminale(parachutiste);
        double altitudeSecuritaire = 762; //https://skydivecalifornia.com/blog/when-skydivers-pull-parachutes/
        double transitionEntreOuverture = 4; //https://www.ecole-parachutisme.fr/guide-saut-en-parachute/vitesse-chute-libre

        //altitude minimale = altitude sécuritaire + distance de délai de la transition
        return altitudeSecuritaire + transitionEntreOuverture * vTerminale;

    }

    public double getTempsTotal() {
        return tempsTotal;
    }

    public void setTempsTotal(double tempsTotal){
        this.tempsTotal = tempsTotal;
    }


    //La méthode suivante permet de retourner la valeur du temps optimal
    //calculé précedemment.
    public double getTempsOptimal() {
        return tempsOptimal;
    }
}