module com.example.paymentapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.codec;


    opens com.example.paymentapp to javafx.fxml;
    exports com.example.paymentapp;
}