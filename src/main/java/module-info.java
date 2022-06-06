module com.hotel.arm_hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;


    opens com.hotel.arm_hotel to javafx.fxml;
    exports com.hotel.arm_hotel;
    exports com.hotel.arm_hotel.Controllers;
    opens com.hotel.arm_hotel.Controllers to javafx.fxml;
}