package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;


public class InterfaceParametres extends VBox {

    @FXML private TextField textMasse;
    @FXML private Label labelTempsOptimal;
    @FXML private TextField textAltitudeInitiale;
    @FXML private TextField textSurface;
    /*
    Paramètres avec lesquels l'utilisateur pourra jouer:
    - masse parachutiste  - surface(air)  - Hauteur initial
     */


    public InterfaceParametres() {

        configurerInterface();

    }




    private void configurerInterface() {
        try {
            // Charger le fichier normalement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Parametres.fxml"));

            //branche variables @FXML aux composants du FXML
            loader.setController(this);
            VBox contenu = loader.load();

            // Ajouter ce contenu à InterfaceParametres
            this.getChildren().add(contenu);


            seulementChiffres(textMasse);
            seulementChiffres(textAltitudeInitiale);
            seulementChiffres(textSurface);

        } catch (IOException e) {
            System.err.println("Erreur de chargement des paramètres : " + e.getMessage());
        }
    }

    private void seulementChiffres(TextField champ) {
        // source: https://docs.oracle.com/javase/tutorial/uiswing/events/actionlistener.html

        // Détecter le changement de texte:
        champ.textProperty().addListener((observable, ancienTexte, nouvTexte) -> {

            // Si le nouveau texte n'est pas un digit, virgule ou point + accepter que digit apres premier caractère et 1 virgule/point
            if (!nouvTexte.isEmpty() && !nouvTexte.matches("\\d+([.]\\d*)?")) {

                // forcer le champ à revenir à l'ancienne version
                champ.setText(ancienTexte);

            }
        });
    }

    public void designInterface(){
        //Coder l'interface graphique de l'interfaceParametres. Ajoutez des méthodes annexes
    }

    public TextField getTextMasse() {
        return textMasse;
    }

    public TextField getTextAltitudeInitiale() {
        return textAltitudeInitiale;
    }

    public TextField getTextSurface() {
        return textSurface;
    }

    public void setLabelTempsOptimal(Label labelTempsOptimal) {
        this.labelTempsOptimal = labelTempsOptimal;
    }
}
