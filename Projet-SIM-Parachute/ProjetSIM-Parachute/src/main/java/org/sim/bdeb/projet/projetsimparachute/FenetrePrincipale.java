package org.sim.bdeb.projet.projetsimparachute;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class FenetrePrincipale extends BorderPane {

    // --- COMPOSANTS DE L'INTERFACE ---
    private InterfaceParametres parametres;
    private VueAnimation animation;
    private VueStatistique stat;

    // --- CONTRÔLEURS ET STAGE ---
    private SimulationController simulationController;
    private Stage stage;

    // --- ÉLÉMENTS FXML ---
    @FXML
    private Button demarrer;

    @FXML
    private Button reintialiser;

    @FXML
    private Button boutonFoisUn;

    @FXML
    private Button boutonFoisDix;

    // --- STYLES ET ÉTAT ---
    private String styleOriginalVert; // Variable pour stocker ton design SceneBuilder


    // --- CONSTRUCTEUR ---
    public FenetrePrincipale(Stage stage, SimulationController simulation) {
        this.parametres = new InterfaceParametres();
        this.animation = new VueAnimation();
        this.stat = new VueStatistique();
        this.simulationController = simulation;
        this.stage = stage;

        creerFenetre();

        Scene scene = new Scene(this, 1000, 720);

        //Créer la scène à chaque fois que la fenetre est instancié, donc chaque fois qu'on démarre le AppController

        this.stage.setTitle("Simulateur de Parachute");
        this.stage.setResizable(false);
        this.stage.setScene(scene);
    }

    // --- MÉTHODE DE MISE À JOUR ---
    public void update() {
        if (simulationController == null) return;

        //Le controller fourni les données à la vue
        double vitesse = simulationController.getVitesseParachutiste();
        double altitude = simulationController.getAltitude();
        double temps = simulationController.getTempsTotal();
        double force = simulationController.getForce();
        boolean paraOuvert = simulationController.getParachuteOuvert();
        double hauteurInitiale = simulationController.getHauteurInitiale();
        double tempsOpt = simulationController.getTempsOptimal();


        // Mettre à jour les stats
        double facteur = simulationController.getMultiplicateurVitesse(); //Accélerer les nuages
        stat.update(vitesse, altitude, temps, force);

        parametres.updateTempsOptimal(tempsOpt);

        // Mettre à jour l'animation
        animation.update(vitesse, altitude, hauteurInitiale, facteur);
        animation.dessinerParachutiste(paraOuvert);
    }

    // --- GESTION DU CYCLE DE VIE ---

    // Appelé par SimulationController quand le parachutiste touche le sol
    public void onAterissage() {
        if (demarrer != null) {
            demarrer.setStyle(styleOriginalVert);
            demarrer.setText("Démarrer");
        }
    }

    private void creerFenetre() {
        //EVENTS pour donner les paramètres au Controller
        initialiserTransfertParametres();

        this.setLeft(parametres);
        this.setCenter(animation);
        this.setRight(stat);

        configurerTitre();
        configurerBarreDeControle();
        configurerBoutonReinitialiser();
    }

    // --- CONFIGURATION DES SECTIONS (FXML) ---

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

    private void configurerBarreDeControle() {
        try {
            // Créer le conteneur horizontal
            HBox barreHorizontale = new HBox();
            barreHorizontale.setAlignment(Pos.CENTER);

            // Charger le bloc Démarrer
            FXMLLoader loaderDemarrer = new FXMLLoader(getClass().getResource("Demarrer.fxml"));
            loaderDemarrer.setController(this);
            Parent interfaceDemarrer = loaderDemarrer.load();

            // Charger le bloc Accélérer
            FXMLLoader loaderAccelerer = new FXMLLoader(getClass().getResource("Accelerer.fxml"));
            loaderAccelerer.setController(this);
            Parent boutonsAccelerer = loaderAccelerer.load();

            // Configuration des actions
            configurerActionsBoutonsVitesse();

            // L'ordre d'ajout ici détermine l'ordre à l'écran
            barreHorizontale.getChildren().addAll(interfaceDemarrer, boutonsAccelerer);

            this.setBottom(barreHorizontale);

            // Initialisation de la logique du bouton
            if (demarrer != null) {
                styleOriginalVert = demarrer.getStyle();
                configurerActionBoutonDemarrer();
            }

        } catch (IOException e) {
            System.out.println("Erreur de chargement des interfaces : " + e.getMessage());
        }
    }


    // --- CONFIGURATION DES ACTIONS  ---

    private void configurerActionsBoutonsVitesse() {
        // Action pour le bouton x1
        boutonFoisUn.setOnAction(event -> {
            if (simulationController != null) {
                simulationController.setMultiplicateurVitesse(1.0);
            }
        });

        // Action pour le bouton x10
        boutonFoisDix.setOnAction(event -> {
            if (simulationController != null) {
                simulationController.setMultiplicateurVitesse(10.0);
            }
        });
    }

    private void configurerActionBoutonDemarrer() {

        //Configuration de la touche démarrer
        demarrer.setOnAction(event -> {
            if (simulationController == null) return;

            if (simulationController.isSimulationEnCours()) {
                simulationController.setSimulationEnCours(false);
                simulationController.arreterSimulation();
                demarrer.setStyle(styleOriginalVert);
                demarrer.setText("Démarrer");

            } else {
                if (siChampsNonVides()) {

                    // On récupère les valeurs finales (qui ont été corrigées par les validateurs)
                    double masseFinale = Double.parseDouble(parametres.getTextMasse().getText());
                    double surfaceFinale = Double.parseDouble(parametres.getTextSurface().getText());
                    double altitudeFinale = Double.parseDouble(parametres.getTextAltitudeInitiale().getText());

                    // On force la mise à jour du contrôleur avec les vrais chiffres affichés
                    simulationController.setMasseUtilisateur(masseFinale);
                    simulationController.setSurfaceUtilisateur(surfaceFinale);
                    simulationController.setHauteurInitialeUtilisateur(altitudeFinale);

                    simulationController.setSimulationEnCours(true);
                    simulationController.lancerSimulation();
                    appliquerStyleBoutonArreter();
                }
            }
        });
    }

    //Vérifier que l'utilisateur a rempli les champs de textes avant de fourni les données au modèle
    private boolean siChampsNonVides() {
        return !parametres.getTextMasse().getText().isEmpty() && !parametres.getTextSurface().getText().isEmpty()
                && !parametres.getTextAltitudeInitiale().getText().isEmpty();
    }


    private void configurerBoutonReinitialiser() {
        reintialiser.setOnAction(event -> {
            if (simulationController == null) return;

            // Arrêter la physique et remettre les calculs à zéro
            simulationController.arreterSimulation();
            simulationController.reinitialiserPhysique();

            // Créer de nouvelles instances des vues
            this.parametres = new InterfaceParametres();
            this.animation = new VueAnimation();
            this.stat = new VueStatistique();

            creerFenetre();
        });
    }

    private void appliquerStyleBoutonArreter() {
        //Fond en dégradé léger rouge vif → button arreter
        demarrer.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FF3B30, #FF6B6B);" +
                        "-fx-background-radius: 20;"
        );
        demarrer.setText("Arrêter");
    }


    // --- INTERFACE PARAMETRES (TRANSFERT DE DONNÉES) ---

    // Savoir quand les valeurs des parametres sont modifiées et prévenir SimulationController de modifier la physique

    private void initialiserTransfertParametres() {
        transfererValeurMasse();
        transfererValeurSurface();
        transfererValeurAltitude();
    }

    public void transfererValeurMasse() {

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

        transfererValeur(parametres.getTextAltitudeInitiale(), valeur -> {
            if (simulationController != null) simulationController.setHauteurInitialeUtilisateur(valeur);
        });
    }

    //Entrer une fonction en paramètre, afin d'optimiser le code
    private void transfererValeur(TextField champ, java.util.function.DoubleConsumer methode) {
        champ.setOnKeyReleased(e -> {
            String texte = champ.getText();
            if (!texte.isEmpty()) {
                try {
                    double valeur = Double.parseDouble(texte);

                    methode.accept(valeur);
                } catch (NumberFormatException ex) {

                }
            }
        });
    }

    // --- GETTERS & SETTERS ---

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