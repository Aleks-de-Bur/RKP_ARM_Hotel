package com.hotel.arm_hotel.Classes;

import java.sql.Blob;
import java.sql.SQLException;

/** Класс, содержащий фотографии номеров. */
public class Photo {
    private int photoId;
    private byte[] photo;
    private int apartId;

    /** Конструктор класса. */
    public Photo(int photoId, Blob photo, int apartId) {
        this.photoId = photoId;
        try {
            this.photo = photo.getBytes(1, (int)photo.length());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.apartId = apartId;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getApartId() {
        return apartId;
    }

    public void setApartId(int apartId) {
        this.apartId = apartId;
    }
}
