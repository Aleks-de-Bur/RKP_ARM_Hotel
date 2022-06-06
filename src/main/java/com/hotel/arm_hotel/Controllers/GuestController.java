package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Guest;
import com.hotel.arm_hotel.Classes.Living;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class GuestController {
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfPatronymic;
    @FXML
    private TextField tfSeria;
    @FXML
    private TextField tfNumber;

    private Stage dialogStage;
    Guest guest;
    int guestId;
    int livingId;
    int bookingId;
    boolean newGuest = true;
    boolean isLiving = true;
    @FXML
    public void initialize(){
        TextFormatter passSeriaFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,4}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter passNumberFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,6}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter lastNameFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\D{0,15}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter firstNameFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\D{0,15}").matcher(change.getControlNewText()).matches() ? change : null);
        TextFormatter patronymicFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\D{0,15}").matcher(change.getControlNewText()).matches() ? change : null);

        tfLastName.setTextFormatter(lastNameFormatter);
        tfFirstName.setTextFormatter(firstNameFormatter);
        tfPatronymic.setTextFormatter(patronymicFormatter);
        tfSeria.setTextFormatter(passSeriaFormatter);
        tfNumber.setTextFormatter(passNumberFormatter);
    }

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /** Метод setClient заполняет поля на форме полученными данными. */
    public void setGuest(Guest guest) {
        this.guest = guest;

        tfLastName.setText(guest.getLastName());
        tfFirstName.setText(guest.getName());
        tfPatronymic.setText(guest.getPatronymic());
        tfSeria.setText(String.valueOf(guest.getSeriaPass()));
        tfNumber.setText(String.valueOf(guest.getNumberPass()));
        guestId = guest.getGuestId();
        livingId = guest.getLivingId();
        newGuest = false;
    }

    /** Метод setGuest получает данные о выбранном госте. */
    public void setGuest(int livingId) {
        this.livingId = livingId;
    }

    /** Метод setGuest получает данные о выбранном госте. */
    public void setGuest(int bookingId, boolean isLiving) {
        this.bookingId = bookingId;
        this.isLiving = isLiving;
    }

    /** Метод onDone получает данные из полей на форме и добавляет в базу данных запись о новом госте. */
    public void onDone(ActionEvent actionEvent) {
        if (tfLastName.getText() != "" && tfFirstName.getText() != "" &&
                tfPatronymic.getText() != "" && tfSeria.getText() !="" && tfNumber.getText() != ""){

            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                if (newGuest){
                    if (isLiving) {
                        int info = statement.executeUpdate("INSERT Guests(LastName, FirstName, Patronymic, Passport_series, Passport_number, Living_id, Booking_id) values " +
                                "('" + tfLastName.getText() + "','" + tfFirstName.getText() + "', '" + tfPatronymic.getText() +
                                "', '" + Integer.parseInt(tfSeria.getText()) + "', '" + Integer.parseInt(tfNumber.getText())
                                + "', '" + livingId + "', '" + 0 + "')");
                    }
                    else {
                        int info = statement.executeUpdate("INSERT Guests(LastName, FirstName, Patronymic, Passport_series, Passport_number, Living_id, Booking_id) values " +
                                "('" + tfLastName.getText() + "','" + tfFirstName.getText() + "', '" + tfPatronymic.getText() +
                                "', '" + Integer.parseInt(tfSeria.getText()) + "', '" + Integer.parseInt(tfNumber.getText())
                                + "', '" + 0 + "', '" + bookingId + "')");
                    }
                }
                else{
                    int info = statement.executeUpdate("UPDATE Guests SET LastName = '" + tfLastName.getText() +
                            "', FirstName = '" + tfFirstName.getText() + "', Patronymic = '" + tfPatronymic.getText() +
                                    "', Passport_series = '" + Integer.parseInt(tfSeria.getText()) +
                                    "', Passport_number = '" + Integer.parseInt(tfNumber.getText()) + "' Where Id like " + guestId);
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, проверьте введённые данные.", ButtonType.OK);
                alert.showAndWait();
            }
            Stage stage = (Stage) tfLastName.getScene().getWindow();
            stage.close();
        }
    }

    /** Метод onCancel закрывает данное диалоговое окно. */
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) tfLastName.getScene().getWindow();
        stage.close();
    }
}
