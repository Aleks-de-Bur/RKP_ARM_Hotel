package com.hotel.arm_hotel.Controllers;

import com.hotel.arm_hotel.ARMHotel;
import com.hotel.arm_hotel.Classes.Photo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.sql.rowset.serial.SerialBlob;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PhotoController {
    @FXML
    private ImageView imageView;

    private List<Image> photoList = new ArrayList<>();
    private ObservableList<Photo> photos = FXCollections.observableArrayList();
    int photoId;
    byte[] photo;
    int apartmentId;
    int photoPos = 0;
    Blob blobPhoto;
    private Stage dialogStage;

    public void setAddStage(Stage addStage) {
        this.dialogStage = addStage;
    }

    public void setPhoto(int apartmentId) {
        this.apartmentId = apartmentId;
        update();
    }

    public void onAddPhoto(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        FileChooser.ExtensionFilter filter1 =
                new FileChooser.ExtensionFilter("All image files", "*.png", "*.jpg");
        FileChooser.ExtensionFilter filter2 =
                new FileChooser.ExtensionFilter("JPEG files", "*.jpg");
        FileChooser.ExtensionFilter filter3 =
                new FileChooser.ExtensionFilter("PNG image Files","*.png");
        fileChooser.getExtensionFilters().addAll(filter1, filter2, filter3);

        File file = fileChooser.showOpenDialog(ARMHotel.getPrimaryStage());
        if (file != null) {
            try {
                photo = Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PreparedStatement statement;
        try {
            statement = ARMHotel.getConnection().prepareStatement("INSERT Photos(Photo, Apart_id) values (?, ?)");
        } catch (SQLException e) {
            return;
        }
        try {
            Blob blob = new SerialBlob(photo);
            statement.setBlob(1, blob);
            statement.setInt(2, apartmentId);
            int info = statement.executeUpdate();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Фото успешно добавлено.", ButtonType.OK);
            alert.showAndWait();
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка, проверьте введённые данные.", ButtonType.OK);
            alert.showAndWait();
        }
        update();
    }

    public void onDeletePhoto(ActionEvent actionEvent) {
        Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
        alert1.setTitle("Удаление");
        alert1.setHeaderText("Вы уверены, что хотите удалить выбранную фотографию?");

        ButtonType buttonTypeOne = new ButtonType("Да");
        ButtonType buttonTypeTwo = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert1.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

        Optional<ButtonType> result = alert1.showAndWait();
        if (result.get() == buttonTypeOne) {
            photoId = photos.get(photoPos).getPhotoId();
            Statement statement = null;
            try {
                statement = ARMHotel.getConnection().createStatement();
            } catch (SQLException e) {
                return;
            }
            try {
                int info = statement.executeUpdate("Delete from Photos Where Photos_id like " + photoId);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Фотография успешно удалёна из базы данных", ButtonType.OK);
                alert.showAndWait();
                update();

            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка удаления.", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }

    public void onPrevPhoto(ActionEvent actionEvent) {
        if (photoPos != 0) {
            photoPos--;
            imageView.setImage(photoList.get(photoPos));
        } else {
            try {
                photoPos = photoList.size() - 1;
                imageView.setImage(photoList.get(photoPos));
            }
            catch (Exception e){
                return;
            }
        }
    }

    public void onNextPhoto(ActionEvent actionEvent) {
        if (photoPos == 0)
            return;
        if (photoPos == photoList.size() - 1) {
            photoPos = 0;
            imageView.setImage(photoList.get(photoPos));
        } else {
            photoPos++;
            imageView.setImage(photoList.get(photoPos));
        }
    }

    public void update() {
        photoList.clear();
        photos.clear();
        Statement statement = null;
        Path tempFile = null;
        try {
            statement = ARMHotel.getConnection().createStatement();
        } catch (SQLException e) {
            return;
        }
        try {
            //Получаем список Фотографий
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Photos Where Apart_id like " + apartmentId);
            while (resultSet.next()) {
                photoId = resultSet.getInt("Photos_id");
                blobPhoto = resultSet.getBlob("Photo");

                photos.add(new Photo(photoId, blobPhoto, apartmentId));

                try {
                    tempFile = Files.createTempFile("temp-", ".tmp");
                    Files.write(tempFile, photos.get(photos.size() - 1).getPhoto());
                    InputStream inputStream = Files.newInputStream(tempFile);
                    photoList.add(new Image(inputStream));
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось вывести фотографию", ButtonType.OK);
                    alert.showAndWait();
                }
            }
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Не удалось вывести данные из базы данных", ButtonType.OK);
            alert.showAndWait();
        }

        if (photoList.size() == 0)
            return;
        photoPos = photoList.size() - 1;
        imageView.setImage(photoList.get(photoPos));
    }
}
