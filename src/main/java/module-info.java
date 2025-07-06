module com.agungadiariawan.portscanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;
    requires java.net.http;
    requires com.google.gson;
    requires org.json;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.graphics;

    opens com.agungadiariawan.portscanner to javafx.fxml;
    exports com.agungadiariawan.portscanner;
}