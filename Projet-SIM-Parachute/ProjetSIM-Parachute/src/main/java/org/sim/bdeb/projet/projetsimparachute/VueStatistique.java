package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class VueStatistique extends VBox {

    @FXML private Label temps;
    @FXML private Label velocite;
    @FXML private Label altitude;
    @FXML private Label force;

    public VueStatistique() {
        configurerInterface();
    }

    // Appelé à chaque frame par FenetrePrincipale.update()
    public void update(double vitesse, double altitudeVal, double tempsVal, double forceVal) {
        velocite.setText(String.format("%.1f ", vitesse));
        altitude.setText(String.format("%.0f", altitudeVal));
        temps.setText(String.format("%.1f s", tempsVal));
        force.setText(String.format("%.1f", forceVal));
        System.out.println(tempsVal);
    }

    private void configurerInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Statistique.fxml"));
            loader.setController(this);
            VBox contenu = loader.load();
            this.getChildren().add(contenu);
        } catch (IOException e) {
            System.err.println("Erreur de chargement des statistiques : " + e.getMessage());
        }
    }
}