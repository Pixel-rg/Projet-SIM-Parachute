package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class VueStatistique extends VBox {


    public VueStatistique(){

        configurerInterface();
    }


    public void update(){

    }

    //Trouver une facon déviter le copier coller avec cette méthode et configurerParametres
    private void configurerInterface() {
        try {
            // Charger le fichier normalement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistique.fxml"));

            //branche variables @FXML aux composants du FXML
            loader.setController(this);
            VBox contenu = loader.load();

            // Ajouter ce contenu à InterfaceParametres
            this.getChildren().add(contenu);

        } catch (IOException e) {
            System.err.println("Erreur de chargement des statistiques : " + e.getMessage());
        }
    }

}
