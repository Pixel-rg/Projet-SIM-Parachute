package org.sim.bdeb.projet.projetsimparachute;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainJavaFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        AppController appController = new AppController(stage);
        appController.demarrer();


        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
