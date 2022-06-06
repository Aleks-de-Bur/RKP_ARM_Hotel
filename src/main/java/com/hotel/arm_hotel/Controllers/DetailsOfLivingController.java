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
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;


public class DetailsOfLivingController {
    @FXML
    private HBox hboxEditGuests;
    @FXML
    private HBox hboxAddService;
    @FXML
    private Button btnIsActive;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfFirstName;
    @FXML
    private TextField tfPatronymic;
    @FXML
    private TextField tfPassportSeria;
    @FXML
    private TextField tfPassportNumber;
    @FXML
    private TextField tfPhoneNumber;
    @FXML
    private TextField tfTypeOfApartment;
    @FXML
    private TextField tfNumberOfApartment;
    @FXML
    private TextField tfSettling;
    @FXML
    private TextField tfEviction;
    @FXML
    private TextField tfPrice;
    @FXML
    private TableView<Guest> guestsTable;
    @FXML
    private TableColumn<Guest, String>  lastNameColumn;
    @FXML
    private TableColumn<Guest, String> firstNameColumn;
    @FXML
    private TableColumn<Guest, String> patronymicColumn;
    @FXML
    private TableColumn<Guest, Integer> seriaPasColumn;
    @FXML
    private TableColumn<Guest, Integer> numberPasColumn;
    @FXML
    private ComboBox<String> cbAdditionalService;
    @FXML
    private TableView<Services> additionalServicesTable;
    @FXML
    private TableColumn<Services, String> serviceNameColumn;
    @FXML
    private TableColumn<Services, Integer> servicePriceColumn;

    private ObservableList<Guest> guests = FXCollections.observableArrayList();
    private ObservableList<AdditionalServices> additionalServices = FXCollections.observableArrayList();
    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Services> services = FXCollections.observableArrayList();
    private ObservableList<Services> servicesInLiving = FXCollections.observableArrayList();
    private ObservableList<String> servicesList = FXCollections.observableArrayList();

    private Stage dialogStage;
    Living living;
    int asId;
    int clientId;
    int serviceId;
    int guestId;
    int livingId;
    int passSeria;
    int passNumber;
    int price;
    int apartMaxGuests;
    int guestsCount;
    String firstName;
    String lastName;
    String patronymic;
    String phoneNumber;
    String title;
    Date dateOfBorn;
    boolean isActive;
    int apartmentId;

    @FXML
    void initialize() {
        //Параметры таблицы Гости
        lastNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastName()));
        firstNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getName()));
        patronymicColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPatronymic()));
        seriaPasColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getSeriaPass()));
        numberPasColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getNumberPass()));
        guestsTable.getSortOrder().add(lastNameColumn);

        //Параметры таблицы Проживания
        serviceNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        servicePriceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrice()));
        additionalServicesTable.getSortOrder().add(serviceNameColumn);

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

        tfPhoneNumber.setTextFormatter(phoneFormatter);
        tfPassportSeria.setTextFormatter(passSeriaFormatter);
        tfPassportNumber.setTextFormatter(passNumberFormatter);
        tfLastName.setTextFormatter(lastNameFormatter);
        tfFirstName.setTextFormatter(firstNameFormatter);
        tfPatronymic.setTextFormatter(patronymicFormatter);


    }

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public void setLiving(Living living) {
        this.living = living;

        apartmentId = living.getApartNumber();
        guestsCount = living.getValueOfGuests();
        livingId = living.getLivingId();
        clientId = living.getClientId();
        try {
            update();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Добавление нового Гостя
    public void onAddGuest(ActionEvent actionEvent) {
        if (guestsCount >= apartMaxGuests){
            Alert alert = new Alert(Alert.AlertType.WARNING, "В номер уже заселено максимальное количество человек.", ButtonType.OK);
            alert.showAndWait();
            return;
        }

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
        addStage.setTitle("Добавление нового гостя");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        GuestController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setGuest(livingId);
        addStage.showAndWait();

        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try{
            int info = statement.executeUpdate("UPDATE Livings SET Value_of_guests = " + (living.getValueOfGuests() + 1) +
            " Where Living_id like " + livingId);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, количество гостей не изменено.", ButtonType.OK);
            alert.showAndWait();
        }

        try {
            update();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Редактирование информации о Госте
    public void onDoneUpdates(ActionEvent actionEvent) {
        if (guestsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Гость не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

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
        addStage.setTitle("Редактирование информации о госте");
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initOwner(ARMHotel.getPrimaryStage());
        assert page != null;
        Scene scene = new Scene(page);
        addStage.setScene(scene);
        GuestController controller = loader.getController();
        controller.setAddStage(addStage);
        controller.setGuest(guestsTable.getSelectionModel().getSelectedItem());
        addStage.showAndWait();

        try {
            update();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Удаление Гостя
    public void onDeleteGuest(ActionEvent actionEvent) {
        if (guestsTable.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Гость не выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Удаление");
        alert1.setHeaderText("Вы уверены, что хотите удалить выбранного гостя?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne)
        {
            guestId = guestsTable.getSelectionModel().getSelectedItem().getGuestId();
            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                int info = statement.executeUpdate("Delete from Guests Where Id like " + guestId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Гость успешно удалён из базы данных", ButtonType.OK);
                alert.showAndWait();
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                alert.showAndWait();
            }
        }

        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try{
            int info = statement.executeUpdate("UPDATE Livings SET Value_of_guests = " + (living.getValueOfGuests() - 1) +
                    " Where Living_id like " + livingId);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, количество гостей не изменено.", ButtonType.OK);
            alert.showAndWait();
        }

        try {
            update();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Оформление новой услуги
    public void onAddSelectedService(ActionEvent actionEvent) {
        if(cbAdditionalService.getValue() == null)
            return;

        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Добавление услуги");
        alert1.setHeaderText("Вы уверены, что хотите добавить услугу?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne) {
            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                int info = statement.executeUpdate("INSERT INTO Additional_services(Living_id, Service_id) values " +
                        "('" + livingId + "','" + services.get(cbAdditionalService.getSelectionModel().getSelectedIndex()).getServiceId() + "')");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Услуга успешно добавлена.", ButtonType.OK);
                alert.showAndWait();

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, услуга не удалена.", ButtonType.OK);
                alert.showAndWait();
            }
        }
        try {
            update();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Формируем список услуг
    public void addComboBox(){
        //Получаем список Услуг
        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }

        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Services");
            while (resultSet.next()) {
                serviceId = resultSet.getInt("Services_id");
                services.add(new Services(serviceId, resultSet.getString("Title"), resultSet.getInt("Price")));
                servicesList.add(resultSet.getString("Title"));
            }
            cbAdditionalService.setItems(servicesList);
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    //Формируем список услуг Клиента и просчитываем цену
    public void addService(int id){
        services.clear();
        //Получаем список Услуг
        Statement statement = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Services Where Services_id like " + id);
            while (resultSet.next()) {
                title = resultSet.getString("Title");
                servicesInLiving.add(new Services(id, title, resultSet.getInt("Price")));
                price += resultSet.getInt("Price");
            }
        }
        catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void update() throws SQLException {
        servicesInLiving.clear();
        guests.clear();
        additionalServices.clear();
        clients.clear();
        services.clear();
        servicesList.clear();
        guestsTable.getItems().clear();
        isActive = living.isActive();
        additionalServicesTable.getItems().clear();

        Statement statement = null;
        Statement statement1 = null;
        Statement statement2 = null;
        Statement statement3 = null;
            statement = ARMHotel.getConnection().createStatement();
            statement1 = ARMHotel.getConnection().createStatement();
            statement2 = ARMHotel.getConnection().createStatement();
            statement3 = ARMHotel.getConnection().createStatement();

        ResultSet resultSet3 = statement3.executeQuery(("Select * from Apartments Where Apart_number like " + apartmentId));
        while (resultSet3.next()) {
            apartMaxGuests = resultSet3.getInt("Max_guests");
        }
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

            //Получаем список Гостей
            ResultSet resultSet1 = statement1.executeQuery("SELECT * FROM Guests Where Living_id like " + livingId);
            while (resultSet1.next()) {
                guestId = resultSet1.getInt("Id");
                lastName = resultSet1.getString("LastName");
                firstName = resultSet1.getString("FirstName");
                patronymic = resultSet1.getString("Patronymic");
                passSeria = resultSet1.getInt("Passport_series");
                passNumber = resultSet1.getInt("Passport_number");
                guests.add(new Guest(guestId, lastName, firstName, patronymic, passSeria, passNumber, livingId, 0));
            }
            guestsTable.setItems(guests);

            price = 0;

            //Получаем список Id сервисов
            ResultSet resultSet2 = statement2.executeQuery("SELECT * FROM Additional_Services Where Living_id like " + livingId);
            while (resultSet2.next()) {
                asId = resultSet2.getInt("Living_id");
                serviceId = resultSet2.getInt("Service_id");
                additionalServices.add(new AdditionalServices(asId, serviceId));
                addService(serviceId);
            }

        additionalServicesTable.setItems(servicesInLiving);



        tfLastName.setText(living.getLastName());
        tfFirstName.setText(living.getFirstName());
        tfPatronymic.setText(living.getPatronymic());
        tfPassportSeria.setText(String.valueOf(clients.get(0).getSeriaPass()));
        tfPassportNumber.setText(String.valueOf(clients.get(0).getNumberPass()));
        tfPhoneNumber.setText(String.valueOf(clients.get(0).getTelNumber()));
        tfTypeOfApartment.setText(living.getApartType());
        tfNumberOfApartment.setText(String.valueOf(living.getApartNumber()));
        tfSettling.setText(String.valueOf(living.getSettling()));
        tfEviction.setText(String.valueOf(living.getEviction()));
        tfPrice.setText(String.valueOf(living.getPrice() + price));

        if (!isActive){
            btnIsActive.visibleProperty().setValue(false);
            hboxAddService.visibleProperty().setValue(false);
            hboxEditGuests.visibleProperty().setValue(false);
        }
        addComboBox();
    }

    //Проверяем действительно ли проживание
    public void onActiveIsFalse(ActionEvent actionEvent) {
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Выселение");
        alert1.setHeaderText("Вы уверены, что хотите выселить постояльца?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne) {
            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                int info = statement.executeUpdate("UPDATE Livings SET IsActive = 0 Where Living_id like " + livingId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Клиент успешно выселен!", ButtonType.OK);
                alert.showAndWait();

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка выселения.", ButtonType.OK);
                alert.showAndWait();
            }
        }
        try {
            update();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }
    }

}
