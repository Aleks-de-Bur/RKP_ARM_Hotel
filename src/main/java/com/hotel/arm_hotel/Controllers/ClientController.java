package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Client;
import com.hotel.arm_hotel.Classes.Guest;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class ClientController {
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
    @FXML
    private TextField tfTelNumber;
    @FXML
    private DatePicker dtpBorn;

    private Stage dialogStage;
    Client client;
    int clientId;
    boolean newClient = true;

    @FXML
    public void initialize(){
        TextFormatter phoneFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,11}").matcher(change.getControlNewText()).matches() ? change : null);
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
        tfTelNumber.setTextFormatter(phoneFormatter);
        tfSeria.setTextFormatter(passSeriaFormatter);
        tfNumber.setTextFormatter(passNumberFormatter);
    }

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /** Метод setClient заполняет поля на форме полученными данными. */
    public void setClient(Client client) {
        this.client = client;

        tfLastName.setText(client.getLastName());
        tfFirstName.setText(client.getName());
        tfPatronymic.setText(client.getPatronymic());
        tfSeria.setText(String.valueOf(client.getSeriaPass()));
        tfNumber.setText(String.valueOf(client.getNumberPass()));
        tfTelNumber.setText(String.valueOf(client.getTelNumber()));
        dtpBorn.setValue(client.getBirthday());
        clientId = client.getClientId();
        newClient = false;
    }

    /** Метод onDone получает данные из полей на форме и добавляет в базу данных запись о новом клиенте. */
    public void onDone(ActionEvent actionEvent) {
        if (tfLastName.getText() != "" && tfFirstName.getText() != "" &&
                tfPatronymic.getText() != "" && tfSeria.getText() !="" && tfNumber.getText() != ""){

            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try{
                if (newClient){
                    int info = statement.executeUpdate("INSERT Clients(LastName, FirstName, Patronymic, Passport_series, Passport_number, Birthday, Tel_number) values " +
                            "('" + tfLastName.getText() + "','" + tfFirstName.getText() + "', '" + tfPatronymic.getText() +
                            "', " + Integer.parseInt(tfSeria.getText()) + ", " + Integer.parseInt(tfNumber.getText())
                            + ", '" + Date.valueOf(dtpBorn.getValue()) + "', '" + tfTelNumber.getText() + "')");
                }
                else{
                    int info = statement.executeUpdate("UPDATE Clients SET LastName = '" + tfLastName.getText() +
                            "', FirstName = '" + tfFirstName.getText() + "', Patronymic = '" + tfPatronymic.getText() +
                            "', Passport_series = " + Integer.parseInt(tfSeria.getText()) +
                            ", Passport_number = " + Integer.parseInt(tfNumber.getText()) + ", Birthday = '" + Date.valueOf(dtpBorn.getValue()) +
                            "', Tel_number = '" + tfTelNumber.getText() + "' Where Id like " + clientId);
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
