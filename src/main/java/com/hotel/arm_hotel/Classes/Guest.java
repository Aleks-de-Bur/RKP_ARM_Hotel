package com.hotel.arm_hotel.Classes;

import java.util.Date;

/** Класс, содержащий информацию о гостях. */
public class Guest {
    /** Поле Id гостя*/
    private int guestId;
    private String lastName;
    /** Поле Имя*/
    private String name;
    /** Поле Отчество*/
    private String patronymic;
    /** Поле Серия паспорта*/
    private int seriaPass;
    /** Поле Номер паспорта*/
    private int numberPass;
    /** Поле Id проживания*/
    private int livingId;
    /** Поле Id бронирования*/
    private int bookingId;

    /** Конструктор класса.
     * @param guestId Id гостя
     * @param lastName Фамилия
     * @param name Имя
     * @param patronymic Отчество
     * @param seriaPass Серия паспорта
     * @param numberPass Номер паспорта
     * @param livingId Id проживания
     * @param bookingId Id бронирования
     * @see Guest#Guest(int, String, String, String, int, int, int, int)*/
    public Guest(int guestId, String lastName, String name, String patronymic, int seriaPass, int numberPass, int livingId, int bookingId) {
        this.guestId = guestId;
        this.lastName = lastName;
        this.name = name;
        this.patronymic = patronymic;
        this.seriaPass = seriaPass;
        this.numberPass = numberPass;
        this.livingId = livingId;
        this.bookingId = bookingId;
    }

    /**
     * Функция получения значение поля {@link Guest#guestId}
     * @return возвращает id гостя
     */
    public int getGuestId() {
        return guestId;
    }

    /**
     * Функция изменения id гостя {@link Guest#guestId}
     * @param guestId id гостя
     */
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    /**
     * Функция получения значение поля {@link Guest#lastName}
     * @return возвращает Фамилию
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Функция изменения Фамилии {@link Guest#lastName}
     * @param lastName Фамилия
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Функция получения значение поля {@link Guest#name}
     * @return возвращает Имя
     */
    public String getName() {
        return name;
    }

    /**
     * Функция изменения Имени {@link Guest#name}
     * @param name Имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Функция получения значение поля {@link Guest#patronymic}
     * @return возвращает Отчество
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Функция изменения Отчество {@link Guest#patronymic}
     * @param patronymic Отчество
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Функция получения значение поля {@link Guest#seriaPass}
     * @return возвращает Серию паспорта
     */
    public int getSeriaPass() {
        return seriaPass;
    }

    /**
     * Функция изменения Серии паспорта {@link Guest#seriaPass}
     * @param seriaPass Серия паспорта
     */
    public void setSeriaPass(int seriaPass) {
        this.seriaPass = seriaPass;
    }

    /**
     * Функция получения значение поля {@link Guest#numberPass}
     * @return возвращает Номер паспорта
     */
    public int getNumberPass() {
        return numberPass;
    }

    /**
     * Функция изменения Номера паспорта {@link Guest#numberPass}
     * @param numberPass Номер паспорта
     */
    public void setNumberPass(int numberPass) {
        this.numberPass = numberPass;
    }

    /**
     * Функция получения значение поля {@link Guest#livingId}
     * @return возвращает Id проживания
     */
    public int getLivingId() {
        return livingId;
    }

    /**
     * Функция изменения Id проживания {@link Guest#livingId}
     * @param livingId Id проживания
     */
    public void setLivingId(int livingId) {
        this.livingId = livingId;
    }

    /**
     * Функция получения значение поля {@link Guest#bookingId}
     * @return возвращает Id брони
     */
    public int getBookingId() {
        return bookingId;
    }

    /**
     * Функция изменения Id брони {@link Guest#bookingId}
     * @param bookingId Id брони
     */
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
}
