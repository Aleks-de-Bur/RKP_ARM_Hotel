package com.hotel.arm_hotel.Classes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/** Класс, содержащий информацию о проживаниях. */
public class Living {
    /** Поле Id проживания*/
    private int livingId;
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
    /** Поле Активность проживания*/
    private boolean isActive;
    /** Поле Тип номера*/
    private String apartType;

    /** Конструктор класса.
     * @param livingId id проживания
     * @param lastName Фамилия
     * @param firstName Имя
     * @param patronymic Отчество
     * @param settling Дата заселения
     * @param eviction Дата выселения
     * @param apartNumber Номер
     * @param valueOfGuests Кол-во гостей
     * @param clientId id клиента
     * @param price Цена за ночь
     * @param isActive Активность проживания
     * @param apartType Тип номера
     * @see Living#Living(int, String, String, String, Date, Date, int, int, int, int, boolean, String)
     * */

    /** Конструктор класса. */
    public Living(int livingId, String lastName, String firstName, String patronymic, Date settling, Date eviction, int apartNumber, int valueOfGuests, int clientId, int price, int isActive, String apartType) {
        this.livingId = livingId;
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
        if (isActive == 1)
            this.isActive = true;
        else
            this.isActive = false;
        this.apartType = apartType;
    }

    /**
     * Функция получения значение поля {@link Living#livingId}
     * @return возвращает id проживания
     */
    public int getLivingId() {
        return livingId;
    }

    /**
     * Функция изменения id брони {@link Living#livingId}
     * @param livingId id проживания
     */
    public void setLivingId(int livingId) {
        this.livingId = livingId;
    }


    /**
     * Функция получения значение поля {@link Living#lastName}
     * @return возвращает Фамилию
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Функция изменения Фамилии {@link Living#lastName}
     * @param lastName Фамилия
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Функция получения значение поля {@link Living#firstName}
     * @return возвращает Имя
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Функция изменения Имени {@link Living#firstName}
     * @param firstName Имя
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Функция получения значение поля {@link Living#patronymic}
     * @return возвращает Отчество
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Функция изменения Отчество {@link Living#patronymic}
     * @param patronymic Отчество
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Функция получения значение поля {@link Living#settling}
     * @return возвращает Дату заселения
     */
    public LocalDate getSettling() {
        return settling;
    }

    /**
     * Функция изменения Даты заселения {@link Living#settling}
     * @param settling Дата заселения
     */
    public void setSettling(LocalDate settling) {
        this.settling = settling;
    }

    /**
     * Функция получения значение поля {@link Living#eviction}
     * @return возвращает Дату выселения
     */
    public LocalDate getEviction() {
        return eviction;
    }

    /**
     * Функция изменения Даты выселения {@link Living#eviction}
     * @param eviction Дата выселения
     */
    public void setEviction(LocalDate eviction) {
        this.eviction = eviction;
    }

    /**
     * Функция получения значение поля {@link Living#apartNumber}
     * @return возвращает Номер
     */
    public int getApartNumber() {
        return apartNumber;
    }

    /**
     * Функция изменения Номера {@link Living#apartNumber}
     * @param apartNumber Номер
     */
    public void setApartNumber(int apartNumber) {
        this.apartNumber = apartNumber;
    }

    /**
     * Функция получения значение поля {@link Living#valueOfGuests}
     * @return возвращает Кол-во гостей
     */
    public int getValueOfGuests() {
        return valueOfGuests;
    }

    /**
     * Функция изменения Кол-ва гостей {@link Living#valueOfGuests}
     * @param valueOfGuests Кол-во гостей
     */
    public void setValueOfGuests(int valueOfGuests) {
        this.valueOfGuests = valueOfGuests;
    }

    /**
     * Функция получения значение поля {@link Living#clientId}
     * @return возвращает id клиента
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Функция изменения id клиента {@link Living#clientId}
     * @param clientId id клиента
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Функция получения значение поля {@link Living#price}
     * @return возвращает Цену за ночь
     */
    public int getPrice() {
        return price;
    }

    /**
     * Функция изменения Цены за ночь {@link Living#price}
     * @param price Цена за ночь
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Функция получения значение поля {@link Living#isActive}
     * @return возвращает Активно ли проживание
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Функция изменения Типа номера {@link Living#isActive}
     * @param active Активность проживания
     */
    public void setActive(boolean active) {
        isActive = active;
    }

    /**
     * Функция получения значение поля {@link Living#apartType}
     * @return возвращает Тип номера
     */
    public String getApartType() {
        return apartType;
    }

    /**
     * Функция изменения Типа номера {@link Living#apartType}
     * @param apartType Тип номера
     */
    public void setApartType(String apartType) {
        this.apartType = apartType;
    }

}
