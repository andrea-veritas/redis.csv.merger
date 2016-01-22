package com.tpadsz.utils.merger.entities;

import com.tpadsz.utils.merger.contants.BeanOfferEventType;

import java.io.Serializable;

/**
 * Created by roger.wang on 2016/1/22.
 */
public class BeanOfferEvent<E> implements Serializable {
    private static final long serialVersionUID = -3956196044628346206L;
    private E e;
    private BeanOfferEventType eventType;

    public BeanOfferEvent(E e, BeanOfferEventType eventType) {
        this.e = e;
        this.eventType = eventType;
    }
    public E getElement() {
        return e;
    }

    public BeanOfferEventType getEventType() {
        return eventType;
    }
}
