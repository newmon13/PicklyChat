module dev.jlipka.pickly {
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires static lombok;
    requires java.sql;

    requires org.slf4j;
    requires MaterialFX;
    requires javafx.media;
    requires de.jensd.fx.glyphs.fontawesome;
    requires de.jensd.fx.glyphs.commons;
    requires com.gluonhq.richtextarea;
    requires com.gluonhq.emoji;
    requires org.carrot2.morfologik.polish;
    requires org.carrot2.morfologik.stemming;
    requires jdk.incubator.vector;


    exports dev.jlipka.pickly.model;

    opens dev.jlipka.pickly.model;

    opens dev.jlipka.pickly to javafx.fxml;
    exports dev.jlipka.pickly;
//    exports dev.jlipka.pickly.server;
//    opens dev.jlipka.pickly.server to javafx.fxml;

    exports dev.jlipka.pickly.controller.sections;
    opens dev.jlipka.pickly.controller.sections;
    exports dev.jlipka.pickly.controller.scenes;
    opens dev.jlipka.pickly.controller.scenes;
    exports dev.jlipka.pickly.controller.components.chat;
    opens dev.jlipka.pickly.controller.components.chat;
    exports dev.jlipka.pickly.controller.components.media;
    opens dev.jlipka.pickly.controller.components.media;
}