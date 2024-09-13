module com.yueshuya.knighttour {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.yueshuya.knighttour to javafx.fxml;
    exports com.yueshuya.knighttour;
}