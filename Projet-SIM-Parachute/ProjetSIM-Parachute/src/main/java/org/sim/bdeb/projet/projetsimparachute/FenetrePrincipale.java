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
    private Button lancer; //Quand appuyé dessus, on fait simulationController.boutonDebuterAnimation(true);
    // et quand on reclique dessus, setBoutonPressed(false);

    private SimulationController simulationController;
    private Stage stage;

    @FXML
    private Button demarrer;
    /* Fond en dégradé léger rouge vif → button arreter
    -fx-background-color: linear-gradient(to bottom, #FF3B30, #FF6B6B);
    -fx-text-fill: #FFFFFF;
    -fx-background-radius: 8;
    -fx-font-weight: bold;
    */

    @FXML
    private Button reintialiser;

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

    //--------------- INTERFACE GÉNÉRALE ------------------

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

            // 2. Transformer le FXML en un objet Java (structureTitre devient un StackPane)
            Parent structureTitre = loader.load();

            // 3. Appliquer les réglages de positionnement du BorderPane
            setAlignment(structureTitre, Pos.CENTER);

            // 4. Placer le titre en haut de la fenêtre
            this.setBottom(structureTitre);

        } catch (IOException e) {
            // En cas d'erreur (fichier mal nommé ou mal placé)
            System.out.println("Impossible de charger le fichier FXML du tableau de bord : " + e.getMessage());

        }
    }


//    private void configurerBoutonLancer() {
//        //Initialisation du bouton avec un texte
//        lancer = new Button("Démarrer la Simulation");
//        //Configuration de la touche
//        lancer.setOnAction(event -> {
//            if (simulationController != null) {
//                simulationController.setSimulationEnCours(true);
//                //lancer.setDisable(true); // On désactive le bouton pendant la chute
//                //Mikail: Il vaut mieux le laisser recommencer la simulation quand il veut à la place d'attendre
//            }
//        });
//
//        //On l'ajoute a l'interface (par exemple en haut, ou dans InterfaceParametres)
//        this.setBottom(lancer);
//    }

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


    // ----------------ANIMATION------------------------

    public void mettreAJourPosition(double x, double y) {
        animation.dessinerParachutiste(x, y);
    }


    // ------------------INTERFACE PARAMETRES-----------------------

    //    Savoir quand les valeurs des parametres sont modifiées et prévenir SimulationController de modifier la physique
    private void transfererValeurMasse() {
        //Interface fonctionnelle
        //champ de texte à surveiller (getTextMasse) --> prendre valeur --> donne-le à la méthode setMasseUtilisateur de mon contrôleur.

        transfererValeur(parametres.getTextMasse(), valeur -> simulationController.setMasseUtilisateur(valeur));
    }

    private void transfererValeurSurface() {
        transfererValeur(parametres.getTextSurface(),valeur -> simulationController.setSurfaceUtilisateur(valeur));
    }

    private void transfererValeurAltitude() {
        transfererValeur(parametres.getTextAltitudeInitiale(),valeur -> simulationController.setSurfaceUtilisateur(valeur));

    }

    private void transfererValeur(TextField getValeur, java.util.function.DoubleConsumer methodeDuController) { //fonction entrée en paramètre

        getValeur.setOnKeyReleased((e) -> {

            String texteEntre = getValeur.getText(); //Récupérer valeur en String

            if (!texteEntre.isEmpty()) {
                try {
                    double nouvelleValeur = Double.parseDouble(texteEntre); //mettre la masse en double
                    //Abishanth:  il arrive quoi si simulation controller est null? on va crash je crois?
                    // correction pour debogage:
                    // if (simulationController != null) { }
                    methodeDuController.accept(nouvelleValeur); //bouton "Lancer". moment que le code dans la flèche est exécutée

                } catch (NumberFormatException ex) {
                    // Gestion d'erreur si ce n'est pas un chiffre

                    // Abishanth: faudra mettre du code, comme une sorte de message?
                    // System.out.println("Masse invalide : " + texteEntre);
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
