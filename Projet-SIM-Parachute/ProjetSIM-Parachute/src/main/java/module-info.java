module org.sim.bdeb.projet.projetsimparachute {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens org.sim.bdeb.projet.projetsimparachute to javafx.fxml;
    exports org.sim.bdeb.projet.projetsimparachute;
}