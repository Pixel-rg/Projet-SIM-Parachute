package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.VBox;

import java.io.IOException;


public class InterfaceParametres extends VBox {

    @FXML
    private TextField textMasse;
    @FXML
    private Label labelTempsOptimal;
    @FXML
    private TextField textAltitudeInitiale;
    @FXML
    private TextField textSurface;
    /*
    Paramètres avec lesquels l'utilisateur pourra jouer:
    - masse parachutiste  - surface(air)  - Hauteur initial
     */

    private boolean peutEcrire = true;

    public InterfaceParametres() {

        configurerInterface();

    }

    public void updateTempsOptimal(double tempsOpt) {
        if (Double.isNaN(tempsOpt)) {
            labelTempsOptimal.setText("---"); // Indique que la donnée est en attente si pas encore calculée
        } else {
            labelTempsOptimal.setText(String.format("%.2f s", tempsOpt));
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


            //Imposer des restrictions de champs de textes permis
            seulementChiffres(textMasse);
            seulementChiffres(textAltitudeInitiale);
            seulementChiffres(textSurface);

            seulementIntervallePermis(textMasse, 40, 100);
            seulementIntervallePermis(textAltitudeInitiale, 3000, 6000);
            seulementIntervallePermis(textSurface, 5, 40);

        } catch (IOException e) {
            System.err.println("Erreur de chargement des paramètres : " + e.getMessage());
        }
    }

    //Empecher d'écrire si la simulation est en cours
    //peutEcrire est false si bouton démarrer a été cliqué. Cliquez sur réintialiser pour remettre true
    public void empecherEcrire() {

        if (!peutEcrire) {
            textMasse.setDisable(true);
            textAltitudeInitiale.setDisable(true);
            textSurface.setDisable(true);
        } else {
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
        // Le TextFormatter intercepte chaque changement (touche pressée, coller, effacer)
        champ.setTextFormatter(new TextFormatter<>(change -> {
            String nouveauTexte = change.getControlNewText();

            //  Autoriser le champ vide pour que l'utilisateur puisse effacer et retaper
            if (nouveauTexte.isEmpty()) {
                return change;
            }

            //  Vérifier si c'est un nombre valide avant de parser
            // Le regex "-?\\d*" permet les chiffres
            if (!nouveauTexte.matches("-?\\d*")) {
                return null; // Refuse le caractère si ce n'est pas un chiffre
            }

            try {
                //  Conversion
                int valeur = Integer.parseInt(nouveauTexte);

                //  Vérification de l'intervalle
                // On accepte le changement seulement si on ne dépasse pas le max

                if (valeur <= max) {
                    return change;
                }
            } catch (NumberFormatException e) {
                // En cas de nombre trop grand pour un int
                return null;
            }

            return null; // Refuse le changement si aucune condition n'est respectée
        }));

        // 5. Validation finale  (pour le MIN)
        champ.focusedProperty().addListener((obs, ancien, nouveau) -> {
            if (!nouveau) { // Si l'utilisateur clique ailleurs
                String texte = champ.getText();
                if (texte.isEmpty()) {
                    champ.setText(String.valueOf(min));
                } else {
                    int valeur = Integer.parseInt(texte);
                    if (valeur < min) {
                        champ.setText(String.valueOf(min));
                    }
                }
            }
        });
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

    public void setPeutEcrire(boolean peutEcrire) {
        this.peutEcrire = peutEcrire;
    }
}
