package com.hyhello.priceless.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends Exception{

    private int code = 600;

    public AppException(String msg) {
        super(msg);
    }


    public AppException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
