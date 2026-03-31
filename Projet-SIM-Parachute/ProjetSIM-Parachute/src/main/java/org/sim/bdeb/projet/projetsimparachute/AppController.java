package org.sim.bdeb.projet.projetsimparachute;

import javafx.application.Platform;
import javafx.stage.Stage;

public class AppController {
    private FenetrePrincipale fenetre;
    private SimulationController simulationController;
    private Stage stage;

    public AppController(Stage stage) {
        this.stage = stage;
    }

    public FenetrePrincipale getFenetre() {
        return fenetre;
    }

    public void demarrer(){

        //Abishanth: Je crois qu'il faut creer la fenetre avant puisque sinon simulationContro... prend fenetre null
        // Quelqu'un pourrait confirmer svp. Si j'ai tord enlever les 3 lignes de codes suivant

        fenetre = new FenetrePrincipale(stage, null);
        simulationController = new SimulationController(fenetre);
        fenetre.setSimulationController(simulationController);

        //simulationController = new SimulationController(fenetre);
        //fenetre = new FenetrePrincipale(stage, simulationController);
    }

    public void quitter(){
        Platform.exit();
    }
}
