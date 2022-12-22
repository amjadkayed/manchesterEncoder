module com.example.fileencoder {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.fileencoder to javafx.fxml;
    exports com.example.fileencoder;
}