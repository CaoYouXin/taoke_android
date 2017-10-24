package com.github.caoyouxin.taoke.api;

/**
 * Created by jasontsang on 10/24/17.
 */

public class ApiException extends Exception {
    public ApiException() {
        super();
    }

    public ApiException(String message) {
        super(message);
    }
}
