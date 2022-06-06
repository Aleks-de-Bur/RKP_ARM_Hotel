package com.hotel.arm_hotel.Classes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/** Класс, содержащий информацию о бронированиях. */
public class Booking {
    /** Поле Id брони*/
    private int bookingId;
    /** Поле Фамилия */
    private String lastName;
    /** Поле Имя*/
    private String firstName;
    /** Поле Отчество*/
    private String patronymic;
    /** Поле Дата заселения*/
    private LocalDate settling;
    /** Поле Дата выселения*/
    private LocalDate eviction;
    /** Поле Номер*/
    private int apartNumber;
    /** Поле Количество гостей*/
    private int valueOfGuests;
    /** Поле Id клиента*/
    private int clientId;
    /** Поле Стоимость за ночь*/
    private int price;
    /** Поле Тип номера*/
    private String apartType;

    /** Конструктор класса.
     * @param bookingId id брони
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество
     * @param settling Дата заселения
     * @param eviction Дата выселения
     * @param apartNumber Номер
     * @param valueOfGuests Кол-во гостей
     * @param clientId id клиента
     * @param price Цена за ночь
     * @param apartType Тип номера
     * @see Booking#Booking(int, String, String, String, Date, Date, int, int, int, int, String)
     * */
    public Booking(int bookingId, String lastName, String firstName, String patronymic, Date settling, Date eviction, int apartNumber, int valueOfGuests, int clientId, int price, String apartType) {
        this.bookingId = bookingId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.settling = Instant.ofEpochMilli(settling.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        this.eviction = Instant.ofEpochMilli(eviction.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        this.apartNumber = apartNumber;
        this.valueOfGuests = valueOfGuests;
        this.clientId = clientId;
        this.price = price;
        this.apartType = apartType;
    }

    /**
     * Функция получения значение поля {@link Booking#bookingId}
     * @return возвращает id брони
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Функция изменения id брони {@link Booking#bookingId}
     * @param bookingId id брони
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    /**
     * Функция получения значение поля {@link Booking#lastName}
     * @return возвращает Фамилию
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Функция изменения Фамилии {@link Booking#lastName}
     * @param lastName Фамилия
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Функция получения значение поля {@link Booking#firstName}
     * @return возвращает Имя
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Функция изменения Имени {@link Booking#firstName}
     * @param firstName Имя
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Функция получения значение поля {@link Booking#patronymic}
     * @return возвращает Отчество
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Функция изменения Отчество {@link Booking#patronymic}
     * @param patronymic Отчество
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Функция получения значение поля {@link Booking#settling}
     * @return возвращает Дату заселения
     */
    public LocalDate getSettling() {
        return settling;
    }

    /**
     * Функция изменения Даты заселения {@link Booking#settling}
     * @param settling Дата заселения
     */
    public void setSettling(LocalDate settling) {
        this.settling = settling;
    }

    /**
     * Функция получения значение поля {@link Booking#eviction}
     * @return возвращает Дату выселения
     */
    public LocalDate getEviction() {
        return eviction;
    }

    /**
     * Функция изменения Даты выселения {@link Booking#eviction}
     * @param eviction Дата выселения
     */
    public void setEviction(LocalDate eviction) {
        this.eviction = eviction;
    }

    /**
     * Функция получения значение поля {@link Booking#apartNumber}
     * @return возвращает Номер
     */
    public int getApartNumber() {
        return apartNumber;
    }

    /**
     * Функция изменения Номера {@link Booking#apartNumber}
     * @param apartNumber Номер
     */
    public void setApartNumber(int apartNumber) {
        this.apartNumber = apartNumber;
    }

    /**
     * Функция получения значение поля {@link Booking#valueOfGuests}
     * @return возвращает Кол-во гостей
     */
    public int getValueOfGuests() {
        return valueOfGuests;
    }

    /**
     * Функция изменения Кол-ва гостей {@link Booking#valueOfGuests}
     * @param valueOfGuests Кол-во гостей
     */
    public void setValueOfGuests(int valueOfGuests) {
        this.valueOfGuests = valueOfGuests;
    }

    /**
     * Функция получения значение поля {@link Booking#clientId}
     * @return возвращает id клиента
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Функция изменения id клиента {@link Booking#clientId}
     * @param clientId id клиента
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Функция получения значение поля {@link Booking#price}
     * @return возвращает Цену за ночь
     */
    public int getPrice() {
        return price;
    }

    /**
     * Функция изменения Цены за ночь {@link Booking#price}
     * @param price Цена за ночь
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Функция получения значение поля {@link Booking#apartType}
     * @return возвращает Тип номера
     */
    public String getApartType() {
        return apartType;
    }

    /**
     * Функция изменения Типа номера {@link Booking#apartType}
     * @param apartType Тип номера
     */
    public void setApartType(String apartType) {
        this.apartType = apartType;
    }

}
