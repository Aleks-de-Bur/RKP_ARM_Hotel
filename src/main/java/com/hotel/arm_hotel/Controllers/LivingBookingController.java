package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class LivingBookingController {
    @FXML
    private TableView<Apartment> apartmentsTable;
    @FXML
    private TableColumn<Apartment, Integer> apartNumberColumn;
    @FXML
    private TableColumn<Apartment, String> apartTypeColumn;
    @FXML
    private TableColumn<Apartment, Integer> maxGuestsColumn;
    @FXML
    private TableColumn<Apartment, Integer> priceColumn;


    @FXML
    private TextField tfGuestsCount;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfPatronymic;
    @FXML
    private DatePicker dtpSettling;
    @FXML
    private DatePicker dtpEviction;
    @FXML
    private ComboBox cbApartType;
    @FXML
    private TextField tfPrice;
    @FXML
    private Button btnDone;

    private ObservableList<Guest> guests = FXCollections.observableArrayList();
    private ObservableList<Apartment> apartments = FXCollections.observableArrayList();
    ObservableList<String> types = FXCollections.observableArrayList("Любой", "Люкс", "Полулюкс", "Семейный", "Двухместный", "Одноместный");

    int clientId;
    int apartmentId;
    int bookingId;
    int livingId;
    int price;
    int apartNumber;
    int valueOfGuests;
    String apartType;
    boolean isLiving;
    Date settling;
    Date eviction;


    Client client;
    Apartment apartment;
    private Stage dialogStage;

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    /** Метод setInform получает данные клиента и отображает их на форме. */
    public void setInform(Client client, boolean isLiving) {
        this.client = client;

        tfLastName.setText(String.valueOf(client.getLastName()));
        tfFirstName.setText(String.valueOf(client.getName()));
        tfPatronymic.setText(String.valueOf(client.getPatronymic()));
        tfPrice.setText(String.valueOf(0));
        tfGuestsCount.setText(String.valueOf(0));

        clientId = client.getClientId();
        this.isLiving = isLiving;
    }

    @FXML
    void initialize() {
        //Параметры таблицы Клиенты
        apartNumberColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getApartNumber()));
        apartTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getApartType()));
        maxGuestsColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getMaxGuests()));
        priceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));
        apartmentsTable.getSortOrder().add(apartNumberColumn);

        dtpSettling.setValue(LocalDate.now());
        dtpEviction.setValue(LocalDate.now().plusDays(1));

        TextFormatter guestFormatter = new TextFormatter((UnaryOperator<TextFormatter.Change>) change ->
                Pattern.compile("\\d{0,2}").matcher(change.getControlNewText()).matches() ? change : null);

        tfGuestsCount.setTextFormatter(guestFormatter);

        cbApartType.setItems(types);
        cbApartType.setValue(cbApartType.getItems().get(0));

        btnDone.setDisable(true);

        update();
    }

    /** Метод onDone получает данные из полей на форме и добавляет в базу данных запись о новом бронировании или проживании. */
    public void onDone(ActionEvent actionEvent) {
        /** Проверки на корректные значения. */
        if (apartmentsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Выберите номер.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpSettling.getValue()).before(Date.valueOf(LocalDate.now()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Нельзя произвести заселение на прошедшие даты.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpEviction.getValue()).before(Date.valueOf(dtpSettling.getValue()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Дата выселения должна быть больше даты заселения.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpEviction.getValue()).equals(Date.valueOf(dtpSettling.getValue()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Дата выселения должна быть больше даты заселения.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        if (((Integer.parseInt(tfGuestsCount.getText()) + 1) > apartmentsTable.getSelectionModel().getSelectedItem().getMaxGuests())){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Превышено количество гостей для данного номера.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        /** Высчитывание стоимости проживания на основании введённых параметров */
        tfPrice.setText(String.valueOf(apartmentsTable.getSelectionModel().getSelectedItem().getPrice() *
                (Math.abs(ChronoUnit.DAYS.between(dtpSettling.getValue(), dtpEviction.getValue())))));

        price = Integer.parseInt(tfPrice.getText());

        settling = Date.valueOf(dtpSettling.getValue());
        eviction = Date.valueOf(dtpEviction.getValue());
        apartNumber = apartmentsTable.getSelectionModel().getSelectedItem().getApartNumber();
        apartType = apartmentsTable.getSelectionModel().getSelectedItem().getApartType();
        valueOfGuests = Integer.parseInt(tfGuestsCount.getText());

        /** Внесение записи в таблицы о новом проживании или бронировании. */
        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {

            if (isLiving){
                int info = statement.executeUpdate("INSERT Livings(Settling, Eviction, Apart_number, Value_of_guests, " +
                        "Client_id, Price, IsActive, Apart_Type) values ('" + settling + "', '" +
                        eviction + "', " + apartNumber + ", " + valueOfGuests + ", " + clientId +  ", " + price +  ", '" + 1 +
                        "', '" + apartType + "')");
            }
            else{
                int info = statement.executeUpdate("INSERT Bookings(Settling, Eviction, Apart_number, Value_of_guests, " +
                        "Client_id, Price, Apart_Type) values ('" + settling + "', '" +
                        eviction + "', " + apartNumber + ", " + valueOfGuests + ", " + clientId +  ", " +
                        price +  ", '" + apartType + "')");
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, проверьте введённые данные.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        /** Вызов формы регистрации нового гостя. Если у постояльца есть гости, то выполняется регистрация каждого из них. */
        if(Integer.parseInt(tfGuestsCount.getText()) > 0){
            if (isLiving) {
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Livings " +
                            "Where Client_id like " + clientId + " AND Apart_number like " + apartNumber);
                    while (resultSet.next()) {
                        livingId = resultSet.getInt("Living_id");
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Ошибка при поиске проживания.", ButtonType.OK);
                    alert.showAndWait();
                }
            }
            else {
                try {
                    ResultSet resultSet = statement.executeQuery("SELECT * FROM Bookings " +
                            "Where Client_id like " + clientId + " AND Apart_number like " + apartNumber);
                    while (resultSet.next()) {
                        bookingId = resultSet.getInt("Booking_id");
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Ошибка при поиске бронирования.", ButtonType.OK);
                    alert.showAndWait();;
                }
            }

            int count = Integer.parseInt(tfGuestsCount.getText());
            while (count > 0){
                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(ARMHotel.class.getResource("guest-view.fxml"));
                Parent page = null;
                try {
                    page = loader.load();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
                    alert.showAndWait();
                }
                Stage addStage = new Stage();
                addStage.setTitle("Добавление гостя №" + (Integer.parseInt(tfGuestsCount.getText()) - (count -1)) + " из " +  Integer.parseInt(tfGuestsCount.getText()));
                addStage.initModality(Modality.APPLICATION_MODAL);
                addStage.initOwner(ARMHotel.getPrimaryStage());
                assert page != null;
                Scene scene = new Scene(page);
                addStage.setScene(scene);
                GuestController controller = loader.getController();
                controller.setAddStage(addStage);
                if (isLiving)
                    controller.setGuest(livingId);
                else
                    controller.setGuest(bookingId, false);
                addStage.showAndWait();
                count--;
            }

        }
        Alert alert;
        if (isLiving){
            alert = new Alert(Alert.AlertType.INFORMATION, "Заселение прошло успешно.", ButtonType.OK);
        }
        else {
            alert = new Alert(Alert.AlertType.INFORMATION, "Номер забронирован.", ButtonType.OK);
        }
        alert.showAndWait();
        Stage stage = (Stage) tfPrice.getScene().getWindow();
        stage.close();
    }

    /** Метод onCancel закрывает данное диалоговое окно. */
    public void onCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) tfLastName.getScene().getWindow();
        stage.close();
    }

    /** Метод update получает данные о номерах, фильтрует их и отображает на форме. */
    void update(){
        apartments.clear();
        apartmentsTable.getItems().clear();

        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            if (dtpSettling.getValue() != null && dtpEviction.getValue() != null)
            {
                if (cbApartType.getValue() == "Любой")
                {
                    //Получаем список Номеров
                    ResultSet resultSet = statement.executeQuery("SELECT a.Id, a.Apart_number, a.Apart_Type, a.Price, " +
                            "a.Max_guests FROM Apartments a WHERE ((a.Apart_number IN (SELECT Apart_number FROM Livings " +
                            "WHERE Eviction<'" + dtpSettling.getValue() + "') AND NOT EXISTS(SELECT Apart_number FROM Bookings " +
                            "WHERE a.Apart_number IN (SELECT Apart_number FROM Bookings))) OR " +
                            "(a.Apart_number in (SELECT Apart_number FROM Bookings WHERE Settling>'" +
                            dtpEviction.getValue() + "') OR (a.Apart_number in (SELECT Apart_number FROM Bookings " +
                            "WHERE Eviction<'" + dtpSettling.getValue() + "'))) AND NOT EXISTS(SELECT Apart_number FROM Livings " +
                            "WHERE a.Apart_number IN (SELECT Apart_number FROM Livings)) OR " +
                            "((a.Apart_number in (SELECT Apart_number FROM Livings WHERE Eviction<'" +
                            dtpSettling.getValue() + "')) AND (a.Apart_number in (SELECT Apart_number FROM Bookings " +
                            "WHERE Settling>'" + dtpEviction.getValue() + "') OR (a.Apart_number in (SELECT Apart_number " +
                            "FROM Bookings WHERE Eviction<'" + dtpSettling.getValue() + "')))) OR " +
                            "(a.Apart_number NOT IN (SELECT Apart_number FROM Livings) AND a.Apart_number " +
                            "NOT IN (SELECT Apart_number FROM Bookings)))");
                    while (resultSet.next()) {
                        apartmentId = resultSet.getInt("Id");
                        apartNumber = resultSet.getInt("Apart_Number");
                        apartType = resultSet.getString("Apart_Type");
                        price = resultSet.getInt("Price");
                        valueOfGuests = resultSet.getInt("Max_guests");

                        apartments.add(new Apartment(apartmentId, apartNumber, apartType, price, valueOfGuests));
                    }
                    apartmentsTable.setItems(apartments);
                }
                else{
                    //Получаем список Номеров
                    ResultSet resultSet = statement.executeQuery("SELECT a.Id, a.Apart_number, a.Apart_Type, a.Price, " +
                            "a.Max_guests FROM Apartments a WHERE a.Apart_Type like '" + cbApartType.getValue() +
                            "' AND ((a.Apart_number IN (SELECT Apart_number FROM Livings WHERE Eviction<'" +
                            dtpSettling.getValue() + "') AND NOT EXISTS(SELECT Apart_number FROM Bookings " +
                            "WHERE a.Apart_number IN (SELECT Apart_number FROM Bookings))) OR " +
                            "(a.Apart_number in (SELECT Apart_number FROM Bookings WHERE Settling>'" +
                            dtpEviction.getValue() + "') OR (a.Apart_number in (SELECT Apart_number FROM Bookings " +
                            "WHERE Eviction<'" + dtpSettling.getValue() + "'))) AND NOT EXISTS(SELECT Apart_number FROM Livings " +
                            "WHERE a.Apart_number IN (SELECT Apart_number FROM Livings)) OR " +
                            "((a.Apart_number in (SELECT Apart_number FROM Livings WHERE Eviction<'" +
                            dtpSettling.getValue() + "')) AND (a.Apart_number in (SELECT Apart_number FROM Bookings " +
                            "WHERE Settling>'" + dtpEviction.getValue() + "') OR (a.Apart_number in (SELECT Apart_number " +
                            "FROM Bookings WHERE Eviction<'" + dtpSettling.getValue() + "')))) OR " +
                            "(a.Apart_number NOT IN (SELECT Apart_number FROM Livings) AND a.Apart_number " +
                            "NOT IN (SELECT Apart_number FROM Bookings)))");
                    while (resultSet.next()) {
                        apartmentId = resultSet.getInt("Id");
                        apartments.add(new Apartment(apartmentId, resultSet.getInt("Apart_number"),
                                resultSet.getString("Apart_Type"), resultSet.getInt("Price"),
                                resultSet.getInt("Max_guests")));
                    }
                    apartmentsTable.setItems(apartments);
                }
            }
            else {
                //Получаем список Номеров
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Apartments");
                while (resultSet.next()) {
                    apartmentId = resultSet.getInt("Id");
                    apartments.add(new Apartment(apartmentId, resultSet.getInt("Apart_number"),
                            resultSet.getString("Apart_Type"), resultSet.getInt("Price"),
                            resultSet.getInt("Max_guests")));
                }
                apartmentsTable.setItems(apartments);
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    /** Метод onFilter выводит в таблицу на форме свободные номера на заданный период. */
    public void onFilter(ActionEvent actionEvent) {
        if (Date.valueOf(dtpSettling.getValue()).before(Date.valueOf(LocalDate.now()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Нельзя произвести заселение на прошедшие даты.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpEviction.getValue()).before(Date.valueOf(dtpSettling.getValue()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Дата выселения должна быть больше даты заселения.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpEviction.getValue()).equals(Date.valueOf(dtpSettling.getValue()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Дата выселения должна быть больше даты заселения.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        update();

        btnDone.setDisable(false);
    }

    /** Высчитывание стоимости проживания на основании введённых параметров */
    public void onSelectPrice(MouseEvent mouseEvent) {
        if (apartmentsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Выберите номер.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpSettling.getValue()).before(Date.valueOf(LocalDate.now()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Нельзя произвести заселение на прошедшие даты.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpEviction.getValue()).before(Date.valueOf(dtpSettling.getValue()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Дата выселения должна быть больше даты заселения.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        if (Date.valueOf(dtpEviction.getValue()).equals(Date.valueOf(dtpSettling.getValue()))){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Дата выселения должна быть больше даты заселения.", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        tfPrice.setText(String.valueOf(apartmentsTable.getSelectionModel().getSelectedItem().getPrice() *
                (Math.abs(ChronoUnit.DAYS.between(dtpSettling.getValue(), dtpEviction.getValue())))));

        price = Integer.parseInt(tfPrice.getText());
    }
}
