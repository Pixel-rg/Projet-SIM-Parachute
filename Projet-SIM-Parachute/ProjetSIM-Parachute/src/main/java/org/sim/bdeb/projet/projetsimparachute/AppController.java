package org.sim.bdeb.projet.projetsimparachute;

import javafx.stage.Stage;

//Application qui instancie la vue et le controller
public class AppController {
    private FenetrePrincipale fenetre;
    private SimulationController simulationController;
    private Stage stage;

    public AppController(Stage stage) {
        this.stage = stage;
    }


    public void demarrer() {
        // On crée d'abord la vue
        fenetre = new FenetrePrincipale(stage, null);

        // On crée le contrôleur en lui donnant la vue
        simulationController = new SimulationController(fenetre);

        // On branche le contrôleur à la vue pour qu'elle puisse lui envoyer des ordres
        fenetre.setSimulationController(simulationController);

    }

}
