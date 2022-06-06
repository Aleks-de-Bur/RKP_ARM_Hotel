package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Guest;
import com.hotel.arm_hotel.Classes.Services;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.sql.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/** Данный класс отвечает за работу с услугами. В нём происходит добавление новой услуги и редактирование уже существующей. */
public class AddEditServiceController {
    @FXML
    private TextField tfTitle;
    @FXML
    private TextField tfPrice;

    private Stage dialogStage;
    Services services;
    int servicesId;

    boolean newService = true;

    @FXML
    public void initialize(){
        TextFormatter titleFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\D{0,20}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter priceFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,5}").matcher(change.getControlNewText()).matches() ? change : null);

        tfTitle.setTextFormatter(titleFormatter);
        tfPrice.setTextFormatter(priceFormatter);
    }

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /** Метод setServices заполняет поля на форме полученными данными. */
    public void setServices(Services services) {
        this.services = services;

        tfTitle.setText(services.getTitle());
        tfPrice.setText(String.valueOf(services.getPrice()));

        servicesId = services.getServiceId();
        newService = false;
    }

    /** Метод onDone получает данные из полей на форме и добавляет в базу данных запись о новой услуге. */
    public void onDone(ActionEvent actionEvent) {
        if (tfPrice.getText() != "" && tfTitle.getText() != ""){

            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                if (newService){
                    int info = statement.executeUpdate("INSERT Services(Title, Price) values " +
                            "('" + tfTitle.getText() + "','" + Integer.parseInt(tfPrice.getText()) + "')");
                }
                else{
                    int info = statement.executeUpdate("UPDATE Services SET Title = '" + tfTitle.getText() +
                            "', Price = '" + Integer.parseInt(tfPrice.getText()) + "' Where Services_id like " + servicesId);
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, проверьте введённые данные.", ButtonType.OK);
                alert.showAndWait();
            }
            Stage stage = (Stage) tfPrice.getScene().getWindow();
            stage.close();
        }
    }

    /** Метод onCancel закрывает данное диалоговое окно. */
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) tfPrice.getScene().getWindow();
        stage.close();
    }
}