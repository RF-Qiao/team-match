package com.feng.common;
import lombok.Data;
import java.io.Serializable;
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public  BaseResponse(ErrorCode errorCode,String message){
        this(errorCode.getCode(),null,message,"");
    }

    public  BaseResponse(ErrorCode errorCode,String message,String description){
        this(errorCode.getCode(),null,message,description);
    }

    public  BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,null,"");
    }
}
