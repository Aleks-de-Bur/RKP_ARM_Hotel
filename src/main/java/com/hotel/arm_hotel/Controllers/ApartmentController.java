package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Apartment;
import com.hotel.arm_hotel.Classes.Guest;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ApartmentController {
    @FXML
    private TextField tfApartmentNumber;
    @FXML
    private ComboBox<String> cbApartmentType;
    @FXML
    private TextField tfMaxGuests;
    @FXML
    private TextField tfPrice;

    private Stage dialogStage;
    Apartment apartment;
    int apartmentId;
    boolean newApartment = true;

    ObservableList<String> types = FXCollections.observableArrayList("Люкс", "Полулюкс", "Семейный", "Двухместный", "Одноместный");

    @FXML
    public void initialize(){
        TextFormatter priceFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,5}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter apartNumberFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,3}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter maxGuestsFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,2}").matcher(change.getControlNewText()).matches() ? change : null);

        tfApartmentNumber.setTextFormatter(apartNumberFormatter);
        tfMaxGuests.setTextFormatter(maxGuestsFormatter);
        tfPrice.setTextFormatter(priceFormatter);
    }

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
        cbApartmentType.setItems(types);
    }

    /** Метод setApartment заполняет поля на форме полученными данными. */
    public void setApartment(Apartment apartment) {
        this.apartment = apartment;

        tfApartmentNumber.setText(String.valueOf(apartment.getApartNumber()));
        tfMaxGuests.setText(String.valueOf(apartment.getMaxGuests()));
        tfPrice.setText(String.valueOf(apartment.getPrice()));

        cbApartmentType.setValue(apartment.getApartType());

        apartmentId = apartment.getApartId();
        newApartment = false;
    }

    /** Метод onDone получает данные из полей на форме и добавляет в базу данных запись о новом номере. */
    public void onDone(ActionEvent actionEvent) {
        if (tfApartmentNumber.getText() != "" && tfMaxGuests.getText() != "" &&
                tfPrice.getText() != "" && cbApartmentType.getValue() != null){

            Statement statement = null;
            Statement statement1 = null;
            Statement statement2 = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
                statement1 = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                ResultSet resultSet = statement1.executeQuery("Select * from Apartments Where Apart_number like " + tfApartmentNumber.getText());
                if (resultSet.next() != false){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Номер с таким числовым обозначением уже существует.", ButtonType.OK);
                    alert.showAndWait();
                    return;
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка поиска номеров по заданному номеру.", ButtonType.OK);
                alert.showAndWait();
            }
            try {
                if (newApartment){
                    int info = statement.executeUpdate("INSERT Apartments(Apart_Number, Apart_Type, Price, Max_guests) values " +
                            "(" + Integer.parseInt(tfApartmentNumber.getText()) + ", '" + cbApartmentType.getValue() + "', " + Integer.parseInt(tfPrice.getText()) +
                            ", " + Integer.parseInt(tfMaxGuests.getText()) + ")");
                }
                else{
                    int info = statement.executeUpdate("UPDATE Apartments SET Apart_Number = " + Integer.parseInt(tfApartmentNumber.getText()) +
                            ", Apart_Type = '" + cbApartmentType.getValue() + "', Price = " + Integer.parseInt(tfPrice.getText()) +
                            ", Max_guests = " + Integer.parseInt(tfMaxGuests.getText()) + " Where Id like " + apartmentId);
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка добавления номера.", ButtonType.OK);
                alert.showAndWait();
            }
            Stage stage = (Stage) tfPrice.getScene().getWindow();
            stage.close();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING, "Введите необходимые данные.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /** Метод onCancel закрывает данное диалоговое окно. */
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) tfPrice.getScene().getWindow();
        stage.close();
    }
}