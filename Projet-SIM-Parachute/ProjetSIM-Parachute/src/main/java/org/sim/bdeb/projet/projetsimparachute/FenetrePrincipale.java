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

    private String styleOriginalVert; // Variable pour stocker ton design SceneBuilder

    public FenetrePrincipale(Stage stage, SimulationController simulation) {
        this.parametres = new InterfaceParametres();
        this.animation = new VueAnimation();
        this.stat = new VueStatistique();
        this.simulationController = simulation;
        this.stage = stage;

        creerFenetre();

        //Créer la scène à chaque fois que la fenetre est instancié, donc chaque fois quon démarre le appController
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
        double tempsOpt = simulationController.getTempsOptimal();


        // Mettre à jour les stats
        stat.update(vitesse, altitude, temps, force);

        parametres.updateTempsOptimal(tempsOpt);

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
            // 1. Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Titre.fxml"));

            // 2. Transformer le FXML en un objet Java (structureTitre devient un StackPane)
            Parent structureTitre = loader.load();

            // 3. Appliquer les réglages de positionnement du BorderPane
            setAlignment(structureTitre, Pos.CENTER);

            // 4. Placer le titre en haut de la fenêtre
            this.setTop(structureTitre);

        } catch (IOException e) {
            // En cas d'erreur (fichier mal nommé ou mal placé)
            System.out.println("Impossible de charger le fichier FXML du titre : " + e.getMessage());

        }
    }

    private void configurerDemarrer() {
        try {
            // 1. Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Demarrer.fxml"));
            loader.setController(this);


            // 2. Transformer le FXML en un objet Java (structureTitre devient un StackPane)
            Parent structureTitre = loader.load();

            // 3. Appliquer les réglages de positionnement du BorderPane
            setAlignment(structureTitre, Pos.CENTER);

            styleOriginalVert = demarrer.getStyle();
            verifierBoutonDemarrer();
            // 4. Placer le titre en haut de la fenêtre
            this.setBottom(structureTitre);


        } catch (IOException e) {
            // En cas d'erreur (fichier mal nommé ou mal placé)
            System.out.println("Impossible de charger le fichier FXML du tableau de bord : " + e.getMessage());

        }
    }

    private void verifierBoutonDemarrer() {

        //Configuration de la touche démarrer
        demarrer.setOnAction(event -> {
            if (simulationController == null) return;

            if (simulationController.isSimulationEnCours()) {
                simulationController.setSimulationEnCours(false);
                simulationController.arreterSimulation();
                demarrer.setStyle(styleOriginalVert);
                demarrer.setText("Démarrer");
            } else {

                if (!parametres.getTextMasse().getText().isEmpty() && !parametres.getTextSurface().getText().isEmpty()
                        && !parametres.getTextAltitudeInitiale().getText().isEmpty()) {
                    simulationController.setSimulationEnCours(true);
                    simulationController.lancerSimulation();
                    boutonDemarrerEnRouge();
                }

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
        //Fond en dégradé léger rouge vif → button arreter
        demarrer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FF3B30, #FF6B6B);" +
                        "-fx-background-radius: 20;"
        );
        demarrer.setText("Arrêter");
    }


    private void creerFenetre() {
        //        configurerBoutonLancer();

        //EVENTS pour donner les paramètres au Controller
        transfererValeurMasse();
        transfererValeurSurface();
        transfererValeurAltitude();

        this.setLeft(parametres);
        this.setCenter(animation);
        this.setRight(stat);

        configurerTitre();
        configurerDemarrer();
    }

    // ------------------INTERFACE PARAMETRES-----------------------

    //    Savoir quand les valeurs des parametres sont modifiées et prévenir SimulationController de modifier la physique

    public void transfererValeurMasse() {
        //Interface fonctionnelle
        //champ de texte à surveiller (getTextMasse) --> prendre valeur --> donne-le à la méthode setMasseUtilisateur de mon contrôleur.
        transfererValeur(parametres.getTextMasse(), valeur -> {
            if (simulationController != null) simulationController.setMasseUtilisateur(valeur);
        });
    }

    public void transfererValeurSurface() {
        transfererValeur(parametres.getTextSurface(), valeur -> {
            if (simulationController != null) simulationController.setSurfaceUtilisateur(valeur);
        });
    }

    public void transfererValeurAltitude() {
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
                    //mettre la masse en double
                    //Abishanth:  il arrive quoi si simulation controller est null? on va crash je crois?
                    // correction pour debogage:
                    // if (simulationController != null) { }
                    methode.accept(valeur);
                } catch (NumberFormatException ex) {
                    // Gestion d'erreur si ce n'est pas un chiffre

                    // Abishanth: faudra mettre du code, comme une sorte de message?
                    // System.out.println("Masse invalide : " + texteEntre);                }
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

    public InterfaceParametres getParametres() {
        return parametres;
    }
}