package com.hotel.arm_hotel.Classes;

import java.util.Date;
/** Класс доп.услуг. Связывает значения таблиц "Проживания" и "Услуги". */
public class AdditionalServices {
    /** Поле id услуги, выбранной клиентом */
    private int asId;
    /** Поле id услуги, предоставляемой гостинницей */
    private int ServiceId;

    /** Конструктор класса.
     * @param asId id услуги, выбранной клиентом
     * @param serviceId id услуги, предоставляемой гостинницей
     * @see AdditionalServices#AdditionalServices(int, int)
     * */
    public AdditionalServices(int asId, int serviceId) {
        this.asId = asId;
        ServiceId = serviceId;
    }

    /**
     * Функция получения значение поля {@link AdditionalServices#asId}
     * @return возвращает id услуги, выбранной клиентом
     */

    public int getAsId() {
        return asId;
    }

    /**
     * Функция изменения id услуги, выбранной клиентом {@link AdditionalServices#asId}
     * @param asId id услуги, выбранной клиентом
     */

    public void setAsId(int asId) {
        this.asId = asId;
    }
    /**
     * Функция получения значение поля {@link AdditionalServices#ServiceId}
     * @return возвращает id услуги, предоставляемой гостинницей
     */
    public int getServiceId() {
        return ServiceId;
    }
    /**
     * Функция изменения id услуги, предоставляемой гостинницей {@link AdditionalServices#ServiceId}
     * @param serviceId id услуги, предоставляемой гостинницей
     */
    public void setServiceId(int serviceId) {
        ServiceId = serviceId;
    }
}
