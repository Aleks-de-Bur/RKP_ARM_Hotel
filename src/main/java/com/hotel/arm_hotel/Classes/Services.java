package com.hotel.arm_hotel.Classes;

/** Класс, содержащий информацию о предоставляемых услугах. */
public class Services {
    /** Поле Id услуги*/
    private int serviceId;
    /** Поле Название услуги*/
    private String title;
    /** Поле Стоимость*/
    private int price;

    /** Конструктор класса.
     * @param serviceId Id услуги
     * @param title Название услуги
     * @param serviceId Стоимость
     * @see Services#Services(int, String, int)*/
    public Services(int serviceId, String title, int price) {
        this.serviceId = serviceId;
        this.title = title;
        this.price = price;
    }

    /**
     * Функция получения значение поля {@link Services#serviceId}
     * @return возвращает Id услуги
     */
    public int getServiceId() {
        return serviceId;
    }

    /**
     * Функция изменения Id услуги {@link Services#serviceId}
     * @param serviceId Id услуги
     */
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * Функция получения значение поля {@link Services#title}
     * @return возвращает Название услуги
     */
    public String getTitle() {
        return title;
    }

    /**
     * Функция изменения Названия услуги {@link Services#title}
     * @param title Название услуги
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Функция получения значение поля {@link Services#price}
     * @return возвращает Стоимость
     */
    public int getPrice() {
        return price;
    }

    /**
     * Функция изменения Стоимости {@link Services#price}
     * @param price Стоимость
     */
    public void setPrice(int price) {
        this.price = price;
    }
}
