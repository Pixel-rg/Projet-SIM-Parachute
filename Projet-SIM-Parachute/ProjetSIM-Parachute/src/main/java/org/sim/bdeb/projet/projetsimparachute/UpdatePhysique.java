package org.sim.bdeb.projet.projetsimparachute;

public class UpdatePhysique {
    private MoteurPhysique moteur;

    public UpdatePhysique(MoteurPhysique moteur){
        this.moteur = moteur;
    }

    public void update(Parachutiste parachutiste, double temps){
        moteur.update(parachutiste, temps);
    }
}
