module dev.jlipka.pickly {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires static lombok;
    requires java.sql;
    requires morfologik.polish;
    requires morfologik.stemming;

    requires org.slf4j;


    // Export your controller packages
    exports dev.jlipka.pickly.controller;
    exports dev.jlipka.pickly.controller.components; // Add this line

    // Open the packages to javafx.fxml
    opens dev.jlipka.pickly.controller;
    opens dev.jlipka.pickly.controller.components; // Add this line

    opens dev.jlipka.pickly to javafx.fxml;
    exports dev.jlipka.pickly;
    exports dev.jlipka.pickly.server;
    opens dev.jlipka.pickly.server to javafx.fxml;
}