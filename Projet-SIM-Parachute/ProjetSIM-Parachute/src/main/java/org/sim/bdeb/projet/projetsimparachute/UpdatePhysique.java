package org.sim.bdeb.projet.projetsimparachute;

public class UpdatePhysique {
    private MoteurPhysique moteur;

    public UpdatePhysique(MoteurPhysique moteur){
        this.moteur = moteur;
    }
    // ABISHANTH: J AI UN METHODE POUR PRENDRE ALTITUDE LIVE
    public void update(Parachutiste parachutiste, double temps, double altitude){
        moteur.update(parachutiste, temps,altitude);
    }
}
