package com.hotel.arm_hotel.Classes;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/** Класс, содержащий информацию о постояльцах. */
public class Client {
    /** Поле Id клиента*/
    private int clientId;
    /** Поле Фамилия*/
    private String lastName;
    /** Поле Имя*/
    private String name;
    /** Поле Отчество*/
    private String patronymic;
    /** Поле Серия паспорта*/
    private int seriaPass;
    /** Поле Номер паспорта*/
    private int numberPass;
    /** Поле Дата рождения*/
    private LocalDate birthday;
    /** Поле Номер телефона*/
    private String telNumber;

    /** Конструктор класса.
     * @param clientId Id клиента
     * @param lastName Фамилия
     * @param name Имя
     * @param patronymic Отчество
     * @param seriaPass Серия паспорта
     * @param numberPass Номер паспорта
     * @param birthday Дата рождения
     * @param telNumber Номер телефона
     * @see Client#Client(int, String, String, String, int, int, Date, String)
     * */
    public Client(int clientId, String lastName, String name, String patronymic, int seriaPass, int numberPass, Date birthday, String telNumber) {
        this.clientId = clientId;
        this.lastName = lastName;
        this.name = name;
        this.patronymic = patronymic;
        this.seriaPass = seriaPass;
        this.numberPass = numberPass;
        this.birthday = Instant.ofEpochMilli(birthday.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        this.telNumber = telNumber;
    }

    /**
     * Функция получения значение поля {@link Client#clientId}
     * @return возвращает id клиента
     */
    public int getClientId() {
        return clientId;
    }

    /**
     * Функция изменения id клиента {@link Client#clientId}
     * @param clientId id клиента
     */
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    /**
     * Функция получения значение поля {@link Client#lastName}
     * @return возвращает Фамилию
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Функция изменения Фамилии {@link Client#lastName}
     * @param lastName Фамилия
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Функция получения значение поля {@link Client#name}
     * @return возвращает Имя
     */
    public String getName() {
        return name;
    }

    /**
     * Функция изменения Имени {@link Client#name}
     * @param name Имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Функция получения значение поля {@link Client#patronymic}
     * @return возвращает Отчество
     */
    public String getPatronymic() {
        return patronymic;
    }

    /**
     * Функция изменения Отчество {@link Client#patronymic}
     * @param patronymic Отчество
     */
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    /**
     * Функция получения значение поля {@link Client#seriaPass}
     * @return возвращает Серию паспорта
     */
    public int getSeriaPass() {
        return seriaPass;
    }

    /**
     * Функция изменения Серии паспорта {@link Client#seriaPass}
     * @param seriaPass Серия паспорта
     */
    public void setSeriaPass(int seriaPass) {
        this.seriaPass = seriaPass;
    }

    /**
     * Функция получения значение поля {@link Client#numberPass}
     * @return возвращает Номер паспорта
     */
    public int getNumberPass() {
        return numberPass;
    }

    /**
     * Функция изменения Номера паспорта {@link Client#numberPass}
     * @param numberPass Номер паспорта
     */
    public void setNumberPass(int numberPass) {
        this.numberPass = numberPass;
    }

    /**
     * Функция получения значение поля {@link Client#birthday}
     * @return возвращает Дату рождения
     */
    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * Функция изменения Даты рождения {@link Client#birthday}
     * @param birthday Дата рождения
     */
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    /**
     * Функция получения значение поля {@link Client#telNumber}
     * @return возвращает Номер телефона
     */
    public String getTelNumber() {
        return telNumber;
    }

    /**
     * Функция изменения Номера телефона {@link Client#telNumber}
     * @param telNumber Номер телефона
     */
    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
}
