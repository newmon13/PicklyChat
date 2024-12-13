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

    opens dev.jlipka.pickly to javafx.fxml;
    exports dev.jlipka.pickly;
    exports dev.jlipka.pickly.server;
    opens dev.jlipka.pickly.server to javafx.fxml;
}