package com.hyhello.priceless.dto.resp;

import com.hyhello.priceless.dto.Node;
import com.hyhello.priceless.dto.exception.AppException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 */
@Getter
@Setter
public class ErrorResponse extends Response{

    public ErrorResponse(int code, String msg){
        super(code,msg);
    }


    public static ErrorResponse AppError(AppException e){
        return new ErrorResponse(e.getCode(), e.getMessage());
    }
    public static ErrorResponse SystemError(Throwable e){
        return new ErrorResponse(500, e.getMessage());
    }
}
