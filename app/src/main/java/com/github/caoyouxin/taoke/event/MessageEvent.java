package com.github.caoyouxin.taoke.event;

import com.github.gnastnosaj.boilerplate.rxbus.RxBus;

import io.reactivex.Observable;

/**
 * Created by jasontsang on 11/24/17.
 */

public class MessageEvent {
    public final static Observable<MessageEvent> observable = RxBus.getInstance().register(MessageEvent.class, MessageEvent.class);

    public long count;
    public boolean hide;

    public MessageEvent(long count) {
        this.count = count;
        this.hide = count == 0;
    }
}
