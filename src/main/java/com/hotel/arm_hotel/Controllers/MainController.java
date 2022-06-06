package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Apartment;
import com.hotel.arm_hotel.Classes.Booking;
import com.hotel.arm_hotel.Classes.Client;
import com.hotel.arm_hotel.Classes.Living;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MainController {
    public Tab tabLivings;
    public Tab tabBookings;
    public Tab tabClients;
    public Tab tabApartments;
    public TabPane tabPane;

    @FXML
    private TableView<Living> livingsTable;
    @FXML
    private TableColumn<Living, String> livingsLastNameColumn;
    @FXML
    private TableColumn<Living, String> livingsFirstNameColumn;
    @FXML
    private TableColumn<Living, String> livingsPatronymicColumn;
    @FXML
    private TableColumn<Living, String> livingsSettlingColumn;
    @FXML
    private TableColumn<Living, String> livingsEvictionColumn;
    @FXML
    private TableColumn<Living, Integer> livingsApartmentColumn;
    @FXML
    private TableColumn<Living, Integer> livingsGuestsCountColumn;
    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking, String> bookingsLastNameColumn;
    @FXML
    private TableColumn<Booking, String> bookingsFirstNameColumn;
    @FXML
    private TableColumn<Booking, String> bookingsPatronymicColumn;
    @FXML
    private TableColumn<Booking, String> bookingsSettlingColumn;
    @FXML
    private TableColumn<Booking, String> bookingsEvictionColumn;
    @FXML
    private TableColumn<Booking, Integer> bookingsApartmentColumn;
    @FXML
    private TableColumn<Booking, Integer> bookingsGuestsCountColumn;
    @FXML
    private TableView<Client> clientsTable;
    @FXML
    private TableColumn<Client, String> clientsLastNameColumn;
    @FXML
    private TableColumn<Client, String> clientsFirstNameColumn;
    @FXML
    private TableColumn<Client, String> clientsPatronymicColumn;
    @FXML
    private TableColumn<Client, Integer> clientsPassportSeria;
    @FXML
    private TableColumn<Client, Integer> clientsPassportNumber;
    @FXML
    private TableColumn<Client, String> clientsDateOfBorn;
    @FXML
    private TableColumn<Client, String> clientsPhoneNumber;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Apartment> apartmentsTable;
    @FXML
    private TableColumn<Apartment, Integer> apartmentsNumber;
    @FXML
    private TableColumn<Apartment, String> apartmentsTypeOfNumber;
    @FXML
    private TableColumn<Apartment, Integer> apartmentsPrice;
    @FXML
    private TableColumn<Apartment, Integer> apartmentsValueOfGuests;

    private ObservableList<Apartment> apartments = FXCollections.observableArrayList();
    private ObservableList<Booking> bookings = FXCollections.observableArrayList();
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Living> livings = FXCollections.observableArrayList();

    int clientId = 0;
    int apartmentId = 0;
    int bookingId = 0;
    int livingId = 0;
    int passSeria = 0;
    int passNumber = 0;
    int price = 0;
    int apartNumber = 0;
    int valueOfGuests = 0;
    String firstName = "";
    String lastName = "";
    String patronymic = "";
    String apartType = "";
    String phoneNumber;
    Date dateOfBorn;
    Date settling;
    Date eviction;
    int isActive;

    @FXML
    void initialize() {
        //Параметры таблицы Клиенты
        clientsLastNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastName()));
        clientsFirstNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        clientsPatronymicColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatronymic()));
        clientsPassportSeria.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSeriaPass()));
        clientsPassportNumber.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getNumberPass()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        clientsDateOfBorn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getBirthday().format(dtf)));
        clientsPhoneNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTelNumber()));
        clientsTable.getSortOrder().add(clientsLastNameColumn);

        //Параметры таблицы Проживания
        livingsLastNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastName()));
        livingsFirstNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFirstName()));
        livingsPatronymicColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatronymic()));
        livingsSettlingColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSettling().format(dtf)));
        livingsEvictionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEviction().format(dtf)));
        livingsApartmentColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getApartNumber()));
        livingsGuestsCountColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValueOfGuests()));
        livingsTable.getSortOrder().add(livingsLastNameColumn);

        //Параметры таблицы Бронирования
        bookingsLastNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastName()));
        bookingsFirstNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFirstName()));
        bookingsPatronymicColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatronymic()));
        bookingsSettlingColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getSettling().format(dtf)));
        bookingsEvictionColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEviction().format(dtf)));
        bookingsApartmentColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getApartNumber()));
        bookingsGuestsCountColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getValueOfGuests()));
        bookingsTable.getSortOrder().add(bookingsLastNameColumn);

        //Параметры таблицы Апартаменты
        apartmentsNumber.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getApartNumber()));
        apartmentsTypeOfNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getApartType()));
        apartmentsPrice.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));
        apartmentsValueOfGuests.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getMaxGuests()));
        bookingsTable.getSortOrder().add(bookingsLastNameColumn);

        update();
    }

    //Получение детальной информации по выбранному проживанию
    public void onGetDetailsOfLiving(ActionEvent actionEvent) {
        if (livingsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Проживание не выбрано", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("detailsOfLiving-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Stage addStage = new Stage();
        addStage.setTitle("Подробная информация о проживании");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        DetailsOfLivingController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setLiving(livingsTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        update();
    }

    //Получение активных проживаний
    public void onGetLivingsActive(ActionEvent actionEvent) {
        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            livings.clear();
            livingsTable.getItems().clear();

            //Получаем список Проживаний
            ResultSet result;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Livings Where IsActive like 1");
            while (resultSet.next()) {
                livingId = resultSet.getInt("Living_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                isActive = resultSet.getInt("IsActive");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id = '" + clientId + "'");
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
                livings.add(new Living(livingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, isActive, apartType));
            }
            livingsTable.setItems(livings);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: проживания не найдены.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Получение проживаний с выселением сегодня
    public void onGetLivingsEvictionToday(ActionEvent actionEvent) {
        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            livings.clear();
            livingsTable.getItems().clear();

            //Получаем список Проживаний
            ResultSet result;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Livings Where Eviction like '" + Date.valueOf(LocalDate.now()) + "'");
            while (resultSet.next()) {
                livingId = resultSet.getInt("Living_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                isActive = resultSet.getInt("IsActive");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id = '" + clientId + "'");
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
                livings.add(new Living(livingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, isActive, apartType));
            }
            livingsTable.setItems(livings);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: проживания не найдены.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Переход к клиенту выбранного проживания
    public void onGoToClientFromLivings(ActionEvent actionEvent) {
        if (livingsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Проживание не выбрано", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        clientId = livingsTable.getSelectionModel().getSelectedItem().getClientId();
        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            tabPane.getSelectionModel().select(2);
            clients.clear();
            clientsTable.getItems().clear();

            //Получаем список Клиентов
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Clients Where Id like " + clientId);
            while (resultSet.next()) {
                clientId = resultSet.getInt("Id");
                lastName = resultSet.getString("LastName");
                firstName = resultSet.getString("FirstName");
                patronymic = resultSet.getString("Patronymic");
                dateOfBorn = resultSet.getDate("Birthday");
                passSeria = resultSet.getInt("Passport_series");
                passNumber = resultSet.getInt("Passport_number");
                phoneNumber = resultSet.getString("Tel_number");

                clients.add(new Client(clientId, lastName, firstName, patronymic, passSeria, passNumber, dateOfBorn, phoneNumber));
            }
            clientsTable.setItems(clients);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: Клиент не найден.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Удаление проживания
    public void onDeleteSelectedLiving(ActionEvent actionEvent) {
        if (livingsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Проживание не выбрано", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Удаление");
        alert1.setHeaderText("Вы уверены, что хотите удалить выбранное проживание?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne) {
            livingId = livingsTable.getSelectionModel().getSelectedItem().getLivingId();
            Statement statement = null;
            Statement statement1 = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
                statement1 = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                int info = statement.executeUpdate("Delete from Livings Where Living_id like " + livingId);
                info = statement1.executeUpdate("Delete from Guests Where Living_id like " + livingId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Проживание успешно удалёно из базы данных", ButtonType.OK);
                alert.showAndWait();
                update();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    //Получение броней с заселением на сегодня
    public void onGetBookingsSettlingToday(ActionEvent actionEvent) {
        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            bookings.clear();
            bookingsTable.getItems().clear();

            //Получаем список Броней
            ResultSet result;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Bookings Where Settling like '" + Date.valueOf(LocalDate.now()) + "'");
            while (resultSet.next()) {
                livingId = resultSet.getInt("Booking_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id = '" + clientId + "'");
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
                bookings.add(new Booking(bookingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, apartType));
            }
            bookingsTable.setItems(bookings);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: брони не найдены.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Оформление проживания из брони
    public void onUpgradeToLiving(ActionEvent actionEvent) {
        if (bookingsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Бронирование не выбрано", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        bookingId = bookingsTable.getSelectionModel().getSelectedItem().getBookingId();

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
            clientId = bookingsTable.getSelectionModel().getSelectedItem().getClientId();
            //Получаем список Броней
            ResultSet result;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Bookings Where Booking_id Like " + bookingId);
            while (resultSet.next()) {
                //livingId = resultSet.getInt("Booking_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id = '" + clientId + "'");
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
            }

            int info = statement.executeUpdate("INSERT Livings(Settling, Eviction, Apart_number, Value_of_guests, Client_id, Price, IsActive ,Apart_Type) values " +
                    "('" + settling + "', '" + eviction + "', " + apartNumber + ", " + valueOfGuests + ", " + clientId
                    + ", " + price + ", '" + 1 + "', '" + apartType + "')");

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: брони не найдены.", ButtonType.OK);
            alert.showAndWait();
        }

        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
            statement2 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }

        try {
            ResultSet resultSet = statement1.executeQuery("SELECT * FROM Livings " +
                    "Where Client_id like " + clientId + " AND Apart_number like " + apartNumber);
            while (resultSet.next()) {
                livingId = resultSet.getInt("Living_id");
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Ошибка при поиске проживания.", ButtonType.OK);
            alert.showAndWait();
        }

        try {
            int info = statement.executeUpdate("UPDATE Guests SET Living_id = " + livingId +
                    ", Booking_id = 0 Where Booking_id like " + bookingId);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка Переноса гостей в таблицу проживаний.", ButtonType.OK);
            alert.showAndWait();
        }

        try {
            int info = statement.executeUpdate("Delete from Bookings Where Booking_id like " + bookingId);
            info = statement1.executeUpdate("Delete from Guests Where Booking_id like " + bookingId);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Заселение прошло успешно.", ButtonType.OK);
            alert.showAndWait();

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
            alert.showAndWait();
        }
        update();
    }

    //Переход к клиенту выбранной брони
    public void onGoToClientFromBooking(ActionEvent actionEvent) {
        if (bookingsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Бронирование не выбрано", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        clientId = bookingsTable.getSelectionModel().getSelectedItem().getClientId();
        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            tabPane.getSelectionModel().select(2);

            clients.clear();
            clientsTable.getItems().clear();

            //Получаем список Клиентов
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Clients Where Id like " + clientId);
            while (resultSet.next()) {
                clientId = resultSet.getInt("Id");
                lastName = resultSet.getString("LastName");
                firstName = resultSet.getString("FirstName");
                patronymic = resultSet.getString("Patronymic");
                dateOfBorn = resultSet.getDate("Birthday");
                passSeria = resultSet.getInt("Passport_series");
                passNumber = resultSet.getInt("Passport_number");
                phoneNumber = resultSet.getString("Tel_number");

                clients.add(new Client(clientId, lastName, firstName, patronymic, passSeria, passNumber, dateOfBorn, phoneNumber));
            }
            clientsTable.setItems(clients);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: Клиент не найден.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Удаление брони
    public void onDeleteSelectedBooking(ActionEvent actionEvent) {
        if (bookingsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Бронь не выбрана", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Удаление");
        alert1.setHeaderText("Вы уверены, что хотите удалить выбранную бронь?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne) {

            bookingId = bookingsTable.getSelectionModel().getSelectedItem().getBookingId();
            Statement statement = null;
            Statement statement1 = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
                statement1 = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                int info = statement.executeUpdate("Delete from Bookings Where Booking_id like " + bookingId);
                info = statement1.executeUpdate("Delete from Guests Where Booking_id like " + bookingId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Бронь успешно удалёна из базы данных", ButtonType.OK);
                alert.showAndWait();
                update();

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    //Переход к броням выбранного клиента
    public void onGoToBookingsFromClient(ActionEvent actionEvent) {
        if (clientsTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Клиент не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        clientId = clientsTable.getSelectionModel().getSelectedItem().getClientId();
        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
            try {
                tabPane.getSelectionModel().select(1);

                bookings.clear();
                bookingsTable.getItems().clear();

                //Получаем список Броней
                ResultSet result;
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Bookings Where Client_id like " + clientId);
                while (resultSet.next()) {
                    livingId = resultSet.getInt("Booking_Id");
                    settling = resultSet.getDate("Settling");
                    eviction = resultSet.getDate("Eviction");
                    apartNumber = resultSet.getInt("Apart_number");
                    valueOfGuests = resultSet.getInt("Value_of_guests");
                    clientId = resultSet.getInt("Client_id");
                    price = resultSet.getInt("Price");
                    apartType = resultSet.getString("Apart_Type");

                    result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id = '" + clientId + "'");
                    while (result.next()){
                        lastName = result.getString("LastName");
                        firstName = result.getString("FirstName");
                        patronymic = result.getString("Patronymic");
                    }
                    bookings.add(new Booking(bookingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, apartType));
                }
                bookingsTable.setItems(bookings);

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: брони не найдены.", ButtonType.OK);
                alert.showAndWait();
            }
    }

    //Поиск гостя по фамилии
    public void onSearch(ActionEvent actionEvent) {
        if(searchTextField.getText() != ""){
            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                clients.clear();
                clientsTable.getItems().clear();

                //Получаем список Клиентов
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Clients Where LastName like '" + searchTextField.getText() + "'");
                while (resultSet.next()) {
                    clientId = resultSet.getInt("Id");
                    lastName = resultSet.getString("LastName");
                    firstName = resultSet.getString("FirstName");
                    patronymic = resultSet.getString("Patronymic");
                    dateOfBorn = resultSet.getDate("Birthday");
                    passSeria = resultSet.getInt("Passport_series");
                    passNumber = resultSet.getInt("Passport_number");
                    phoneNumber = resultSet.getString("Tel_number");

                    clients.add(new Client(clientId, lastName, firstName, patronymic, passSeria, passNumber, dateOfBorn, phoneNumber));
                }
                clientsTable.setItems(clients);

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: Клиент не найден.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    //Переход к проживаниям выбранного клиента
    public void onGoToLivingsFromClient(ActionEvent actionEvent) {
        if (clientsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Клиент не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        clientId = clientsTable.getSelectionModel().getSelectedItem().getClientId();
        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            tabPane.getSelectionModel().select(0);
            livings.clear();
            livingsTable.getItems().clear();

            //Получаем список Проживаний
            ResultSet result;
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Livings Where Client_id like " + clientId);
            while (resultSet.next()) {
                livingId = resultSet.getInt("Living_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                isActive = resultSet.getInt("IsActive");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id = '" + clientId + "'");
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
                livings.add(new Living(livingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, isActive, apartType));
            }
            livingsTable.setItems(livings);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка: проживания не найдены.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Редактирование информации о Клиенте
    public void onEditInformationOfClient(ActionEvent actionEvent) {
        if (clientsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Клиент не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("client-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Редактирование информации о постояльце");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        ClientController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setClient(clientsTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        update();
    }

    //Удаление клиента
    public void onDeleteSelectedClient(ActionEvent actionEvent) {
        if (clientsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Клиент не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Удаление");
        alert1.setHeaderText("Вы уверены, что хотите удалить выбранного постояльца?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne)
        {
            clientId = clientsTable.getSelectionModel().getSelectedItem().getClientId();
            Statement statement = null;
            Statement statement1 = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
                statement1 = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                ResultSet resultSet = statement.executeQuery("Select * from Livings Where Client_id like " + clientId);
                ResultSet resultSet1 = statement1.executeQuery("Select * from Bookings Where Client_id like " + clientId);
                if (resultSet.next() == false && resultSet1.next() == false){
                    int info = statement.executeUpdate("Delete from Clients Where Id like " + clientId);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Постоялец успешно удалён из базы данных", ButtonType.OK);
                    alert.showAndWait();
                    update();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "У клиента есть заселения или бронирования, удаление невозможно.", ButtonType.OK);
                    alert.showAndWait();
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                    alert.showAndWait();
            }
        }
    }

    //Добавление нового Клиента
    public void onCreateNewClient(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("client-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Добавление нового постояльца");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        ClientController controller = loader.getController();
        controller.setAddStage(addStage);
        addStage.showAndWait();

        update();
    }

    //Бронирование
    public void onCreateNewBooking(ActionEvent actionEvent) {
        if (clientsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Клиент не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        clientId = clientsTable.getSelectionModel().getSelectedItem().getClientId();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ARMHotel.class.getResource("livingBooking-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Бронирование");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        LivingBookingController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setInform(clientsTable.getSelectionModel().getSelectedItem(), false);
        addStage.showAndWait();

        update();
    }

    //Заселение
    public void onCreateNewLiving(ActionEvent actionEvent) {
        if (clientsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Клиент не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        clientId = clientsTable.getSelectionModel().getSelectedItem().getClientId();

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("livingBooking-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Заселение");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        LivingBookingController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setInform(clientsTable.getSelectionModel().getSelectedItem(), true);
        addStage.showAndWait();

        update();
    }

    //Переход к фотографиям
    public void onGoToPhoto(ActionEvent actionEvent) throws IOException {
        if (apartmentsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Номер не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        apartmentId = apartmentsTable.getSelectionModel().getSelectedItem().getApartId();

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("photo-view.fxml"));
        Parent page = loader.load();
        Stage addStage = new Stage();
        addStage.setTitle("Фотографии номера");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        PhotoController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setPhoto(apartmentId);
        addStage.showAndWait();
    }

    //Редактирование информации о выбранном номере
    public void onEditInformationOfApartment(ActionEvent actionEvent) {
        if (apartmentsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Номер не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        apartNumber = apartmentsTable.getSelectionModel().getSelectedItem().getApartNumber();

        Statement statement3 = null;
        Statement statement4 = null;
        try {
            statement3 = ARMHotel.getConnection().createStatement();
            statement4 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            ResultSet resultSet3 = statement3.executeQuery("Select * from Livings Where Apart_number like " + apartNumber + " AND IsActive like " + 1);
            ResultSet resultSet4 = statement4.executeQuery("Select * from Bookings Where Apart_number like " + apartNumber);
            if (resultSet3.next() == false && resultSet4.next() == false){
                FXMLLoader loader = new FXMLLoader();

                loader.setLocation(ARMHotel.class.getResource("apartment-view.fxml"));
                Parent page = null;
                try {
                    page = loader.load();
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
                    alert.showAndWait();
                }
                Stage addStage = new Stage();
                addStage.setTitle("Редактирование номера");
                addStage.initModality(Modality.APPLICATION_MODAL);
                addStage.resizableProperty().setValue(false);
                addStage.initOwner(ARMHotel.getPrimaryStage());
                Scene scene = new Scene(page);
                addStage.setScene(scene);
                ApartmentController controller = loader.getController();
                controller.setAddStage(addStage);
                controller.setApartment(apartmentsTable.getSelectionModel().getSelectedItem());
                addStage.showAndWait();

                update();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Номер заселён или забронирован. Редактирование невозможно.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка Редактирования.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Удаление номера
    public void onDeleteSelectedApartment(ActionEvent actionEvent) {
        if (apartmentsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Номер не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        apartNumber = apartmentsTable.getSelectionModel().getSelectedItem().getApartNumber();

        Statement statement3 = null;
        Statement statement4 = null;
        try {
            statement3 = ARMHotel.getConnection().createStatement();
            statement4 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            ResultSet resultSet3 = statement3.executeQuery("Select * from Livings Where Apart_number like " + apartNumber + " AND IsActive like " + 1);
            ResultSet resultSet4 = statement4.executeQuery("Select * from Bookings Where Apart_number like " + apartNumber);
            if (resultSet3.next() == false && resultSet4.next() == false){
                Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
                alert1.setTitle("Удаление");
                alert1.setHeaderText("Вы уверены, что хотите удалить выбранный номер?");

                ButtonType buttonTypeOne = new ButtonType("Да");
                ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

                Optional<ButtonType> result = alert1.showAndWait();
                if (result.get() == buttonTypeOne){
                    apartmentId = apartmentsTable.getSelectionModel().getSelectedItem().getApartId();
                    apartNumber = apartmentsTable.getSelectionModel().getSelectedItem().getApartNumber();
                    Statement statement = null;
                    try {
                        statement = ARMHotel.getConnection().createStatement();
                    } catch (SQLException e) {
                        return;
                    }
                    try {
                        int info = statement.executeUpdate("Delete from Apartments Where Id like " + apartmentId);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Номер успешно удалён из базы данных", ButtonType.OK);
                        alert.showAndWait();
                        update();

                    } catch (SQLException ex) {
                        Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Номер заселён или забронирован. Удаление невозможно.", ButtonType.OK);
                alert.showAndWait();
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Услуги
    public void onGoToServices(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("services-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Услуги");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        ServicesController controller = loader.getController();
        controller.setAddStage(addStage);
        addStage.show();

        update();
    }

    //Добавление нового Номера
    public void onCreateApartment(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ARMHotel.class.getResource("apartment-view.fxml"));
        Parent page = null;
        try {
            page = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка открытия формы", ButtonType.OK);
            alert.showAndWait();
        }
        Stage addStage = new Stage();
        addStage.setTitle("Добавление нового номера");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.resizableProperty().setValue(false);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        ApartmentController controller = loader.getController();
        controller.setAddStage(addStage);
        addStage.showAndWait();

        update();
    }

    public void update(){
        apartments.clear();
        bookings.clear();
        clients.clear();
        livings.clear();
        apartmentsTable.getItems().clear();
        bookingsTable.getItems().clear();
        clientsTable.getItems().clear();
        livingsTable.getItems().clear();

        Statement statement = null;
        Statement statement1 = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            //Получаем список Клиентов
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Clients");
            while (resultSet.next()) {
                clientId = resultSet.getInt("Id");
                lastName = resultSet.getString("LastName");
                firstName = resultSet.getString("FirstName");
                patronymic = resultSet.getString("Patronymic");
                dateOfBorn = resultSet.getDate("Birthday");
                passSeria = resultSet.getInt("Passport_series");
                passNumber = resultSet.getInt("Passport_number");
                phoneNumber = resultSet.getString("Tel_number");

                clients.add(new Client(clientId, lastName, firstName, patronymic, passSeria, passNumber, dateOfBorn, phoneNumber));
            }
            clientsTable.setItems(clients);

            //Получаем список Проживаний
            ResultSet result;
            resultSet = statement.executeQuery("SELECT * FROM Livings");
            while (resultSet.next()) {
                livingId = resultSet.getInt("Living_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                isActive = resultSet.getInt("IsActive");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id like " + clientId);
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
                livings.add(new Living(livingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, isActive, apartType));
            }
            livingsTable.setItems(livings);

            //Получаем список Броней
            resultSet = statement.executeQuery("SELECT * FROM Bookings");
            while (resultSet.next()) {
                bookingId = resultSet.getInt("Booking_Id");
                settling = resultSet.getDate("Settling");
                eviction = resultSet.getDate("Eviction");
                apartNumber = resultSet.getInt("Apart_number");
                valueOfGuests = resultSet.getInt("Value_of_guests");
                clientId = resultSet.getInt("Client_id");
                price = resultSet.getInt("Price");
                apartType = resultSet.getString("Apart_Type");

                result = statement1.executeQuery("SELECT LastName, FirstName, Patronymic FROM Clients Where Id like " + clientId);
                while (result.next()){
                    lastName = result.getString("LastName");
                    firstName = result.getString("FirstName");
                    patronymic = result.getString("Patronymic");
                }
                bookings.add(new Booking(bookingId, lastName, firstName, patronymic, settling, eviction, apartNumber, valueOfGuests, clientId, price, apartType));
            }
            bookingsTable.setItems(bookings);

            //Получаем список Апартаментов
            resultSet = statement.executeQuery("SELECT * FROM Apartments");
            while (resultSet.next()) {
                apartmentId = resultSet.getInt("Id");
                apartNumber = resultSet.getInt("Apart_number");
                apartType = resultSet.getString("Apart_Type");
                price = resultSet.getInt("Price");
                valueOfGuests = resultSet.getInt("Max_guests");
                apartments.add(new Apartment(apartmentId, apartNumber, apartType, price, valueOfGuests));
            }
            apartmentsTable.setItems(apartments);

        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
                alert.showAndWait();
        }
    }

    public void onUpdate(ActionEvent actionEvent) {
        update();
    }
}