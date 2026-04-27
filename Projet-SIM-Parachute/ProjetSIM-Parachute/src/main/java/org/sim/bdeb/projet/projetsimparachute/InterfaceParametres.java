package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

    private boolean peutEcrire = true;

    public InterfaceParametres() {

        configurerInterface();

    }

    public void updateTempsOptimal(double tempsOpt) {
        if (labelTempsOptimal != null) {
            labelTempsOptimal.setText(String.format("%.1f s", tempsOpt));
        }
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

            seulementIntervallePermis(textMasse,40,100);
            seulementIntervallePermis(textAltitudeInitiale,3000,6000);
            seulementIntervallePermis(textSurface,5,40);

        } catch (IOException e) {
            System.err.println("Erreur de chargement des paramètres : " + e.getMessage());
        }
    }

    public void empecherEcrire(){

        if(!peutEcrire){    //peutEcrire est false si bouton démarrer a été cliqué. Cliquez sur réintialiser pour remettre true
            textMasse.setDisable(true);
            textAltitudeInitiale.setDisable(true);
            textSurface.setDisable(true);
        }
        else{
            textMasse.setDisable(false);
            textAltitudeInitiale.setDisable(false);
            textSurface.setDisable(false);
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

    private void seulementIntervallePermis(TextField champ, int min, int max) {

        //TextFormatter: intercepte chaque touche pressée avant qu'elle ne soit affichée.
        //Source:
        champ.setTextFormatter(new TextFormatter<>(change -> {
            // On récupère ce que serait le texte final si on acceptait le changement
            String nouveauTexte = change.getControlNewText();

            // autoriser le champ vide pour que l'utilisateur puisse effacer son texte
            if (nouveauTexte.isEmpty()) {
                return change;
            }

            // Mettre String en int
            int valeur = Integer.parseInt(nouveauTexte);

            // 3. valeur doit être plus petit que le max
            if (valeur <= max) {
                return change;
            }

            return null; //Si retourne change, l'action de l'utilisateur est accepté , null = c'est refusé
        }));

        // quand l'utilisateur finit d'interagir avec le champ.
        champ.focusedProperty().addListener((observation, etaitFocus, estFocus) -> {
            // Si estFocus est faux, cela veut dire que l'utilisateur a cliqué ailleurs, donc il faut verifier champ
            if (!estFocus) {
                String texte = champ.getText();

                int valeur = Integer.parseInt(texte);

                if (texte.isEmpty()||valeur <min) {
                    // Si le champ est vide à la sortie, on force la valeur minimale ou si la valeur finale est inférieure au minimum autorisé
                    champ.setText(String.valueOf(min));

                }
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

    public void setPeutEcrire(boolean peutEcrire) {
        this.peutEcrire = peutEcrire;
    }

    public boolean isPeutEcrire() {
        return peutEcrire;
    }
}
