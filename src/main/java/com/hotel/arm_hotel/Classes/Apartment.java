package com.hotel.arm_hotel.Classes;

/** Класс, содержащий информацию о номерах. */
public class Apartment {
    /** Поле Id номера*/
    private int apartId;
    /** Поле Номер*/
    private int apartNumber;
    /** Поле Тип номера*/
    private String apartType;
    /** Поле Стоимость за ночь*/
    private int price;
    /** Поле Количество гостей*/
    private int maxGuests;

    /** Конструктор класса.
     * @param apartId id номера
     * @param apartNumber Номер
     * @param apartType Тип номера
     * @param price Стоимость за ночь
     * @param maxGuests Количество гостей
     * @see Apartment#Apartment(int, int, String, int, int)
     * */
    public Apartment(int apartId, int apartNumber, String apartType, int price, int maxGuests) {
        this.apartId = apartId;
        this.apartNumber = apartNumber;
        this.apartType = apartType;
        this.price = price;
        this.maxGuests = maxGuests;
    }

    /**
     * Функция получения значение поля {@link Apartment#apartId}
     * @return возвращает Id номера
     */
    public int getApartId() {
        return apartId;
    }

    /**
     * Функция изменения Id номера {@link Apartment#apartId}
     * @param apartId Id номера
     */
    public void setApartId(int apartId) {
        this.apartId = apartId;
    }

    /**
     * Функция получения значение поля {@link Apartment#apartNumber}
     * @return возвращает Номер
     */
    public int getApartNumber() {
        return apartNumber;
    }

    /**
     * Функция изменения Номера {@link Apartment#apartNumber}
     * @param apartNumber Номер
     */
    public void setApartNumber(int apartNumber) {
        this.apartNumber = apartNumber;
    }

    /**
     * Функция получения значение поля {@link Apartment#apartType}
     * @return возвращает Тип номера
     */
    public String getApartType() {
        return apartType;
    }

    /**
     * Функция изменения Типа номера {@link Apartment#apartType}
     * @param apartType Тип номера
     */
    public void setApartType(String apartType) {
        this.apartType = apartType;
    }

    /**
     * Функция получения значение поля {@link Apartment#price}
     * @return возвращает Стоимость за ночь
     */
    public int getPrice() {
        return price;
    }

    /**
     * Функция изменения Стоимости за ночь {@link Apartment#price}
     * @param price Стоимость за ночь
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Функция получения значение поля {@link Apartment#maxGuests}
     * @return возвращает Количество гостей
     */
    public int getMaxGuests() {
        return maxGuests;
    }

    /**
     * Функция изменения Количества гостей {@link Apartment#maxGuests}
     * @param maxGuests Количество гостей
     */
    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }
}
