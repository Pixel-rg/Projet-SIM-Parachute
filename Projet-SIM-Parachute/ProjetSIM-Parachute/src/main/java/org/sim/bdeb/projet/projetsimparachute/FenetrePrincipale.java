package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class FenetrePrincipale extends BorderPane {

    private InterfaceParametres parametres;
    private VueAnimation animation;
    private VueStatistique stat;

    private SimulationController simulationController;
    private Stage stage;

    @FXML
    private Button demarrer;

    @FXML
    private Button reintialiser;

    private String styleOriginalVert;

    public FenetrePrincipale(Stage stage, SimulationController simulation) {
        this.parametres = new InterfaceParametres();
        this.animation = new VueAnimation();
        this.stat = new VueStatistique();
        this.simulationController = simulation;
        this.stage = stage;

        creerFenetre();

        Scene scene = new Scene(this, 1000, 700);
        this.stage.setTitle("Simulateur de Parachute");
        this.stage.setScene(scene);
    }

    public void update() {
        if (simulationController == null) return;

        double vitesse = simulationController.getVitesseParachutiste();
        double altitude = simulationController.getAltitude();
        double temps = simulationController.getTempsTotal();
        double force = simulationController.getForce();
        boolean paraOuvert = simulationController.getParachuteOuvert();
        double hauteurInitiale = simulationController.getHauteurInitiale();

        // Mettre à jour les stats
        stat.update(vitesse, altitude, temps, force);

        // Mettre à jour l'animation
        animation.update(vitesse, altitude, hauteurInitiale);
        animation.dessinerParachutiste(paraOuvert);
    }

    // Appelé par SimulationController quand le parachutiste touche le sol
    public void onAterissage() {
        if (demarrer != null) {
            demarrer.setStyle(styleOriginalVert);
            demarrer.setText("Démarrer");
        }
    }

    private void configurerTitre() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Titre.fxml"));
            Parent structureTitre = loader.load();
            setAlignment(structureTitre, Pos.CENTER);
            this.setTop(structureTitre);
        } catch (IOException e) {
            System.out.println("Impossible de charger le fichier FXML du titre : " + e.getMessage());
        }
    }

    private void configurerDemarrer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Demarrer.fxml"));
            loader.setController(this);
            Parent structureDemarrer = loader.load();
            setAlignment(structureDemarrer, Pos.CENTER);

            styleOriginalVert = demarrer.getStyle();
            verifierBoutonDemarrer();
            configurerBoutonReinitialiser();

            this.setBottom(structureDemarrer);
        } catch (IOException e) {
            System.out.println("Impossible de charger le fichier FXML du tableau de bord : " + e.getMessage());
        }
    }

    private void verifierBoutonDemarrer() {
        demarrer.setOnAction(event -> {
            if (simulationController == null) return;

            if (simulationController.isSimulationEnCours()) {
                simulationController.setSimulationEnCours(false);
                simulationController.arreterSimulation();
                demarrer.setStyle(styleOriginalVert);
                demarrer.setText("Démarrer");
            } else {
                simulationController.setSimulationEnCours(true);
                simulationController.lancerSimulation();
                boutonDemarrerEnRouge();
            }
        });
    }

    private void configurerBoutonReinitialiser() {
        reintialiser.setOnAction(event -> {
            if (simulationController == null) return;
            simulationController.arreterSimulation();
            demarrer.setStyle(styleOriginalVert);
            demarrer.setText("Démarrer");
            // Remettre les stats à zéro
            stat.update(0, 0, 0, 0);
        });
    }

    private void boutonDemarrerEnRouge() {
        demarrer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FF3B30, #FF6B6B);" +
                        "-fx-background-radius: 20;"
        );
        demarrer.setText("Arrêter");
    }

    private void creerFenetre() {
        transfererValeurMasse();
        transfererValeurSurface();
        transfererValeurAltitude();

        this.setLeft(parametres);
        this.setCenter(animation);
        this.setRight(stat);

        configurerTitre();
        configurerDemarrer();
    }

    private void transfererValeurMasse() {
        transfererValeur(parametres.getTextMasse(), valeur -> {
            if (simulationController != null) simulationController.setMasseUtilisateur(valeur);
        });
    }

    private void transfererValeurSurface() {
        transfererValeur(parametres.getTextSurface(), valeur -> {
            if (simulationController != null) simulationController.setSurfaceUtilisateur(valeur);
        });
    }

    private void transfererValeurAltitude() {
        // BUG FIXÉ: était setSurfaceUtilisateur, doit être setHauteurInitialeUtilisateur
        transfererValeur(parametres.getTextAltitudeInitiale(), valeur -> {
            if (simulationController != null) simulationController.setHauteurInitialeUtilisateur(valeur);
        });
    }

    private void transfererValeur(TextField champ, java.util.function.DoubleConsumer methode) {
        champ.setOnKeyReleased(e -> {
            String texte = champ.getText();
            if (!texte.isEmpty()) {
                try {
                    double valeur = Double.parseDouble(texte);
                    methode.accept(valeur);
                } catch (NumberFormatException ex) {
                    // Valeur invalide, on ignore
                }
            }
        });
    }

    public VueAnimation getVueAnimation() {
        return animation;
    }

    public void setSimulationController(SimulationController simulationController) {
        this.simulationController = simulationController;
    }
}