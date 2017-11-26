package com.github.caoyouxin.taoke.event;

import com.github.gnastnosaj.boilerplate.rxbus.RxBus;

import io.reactivex.Observable;


public class MessageEvent {
    public final static Observable<MessageEvent> observable = RxBus.getInstance().register(MessageEvent.class, MessageEvent.class);

    public int count;

    public MessageEvent(int count) {
        this.count = count;
    }
}
