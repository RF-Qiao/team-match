package com.feng.exception;
import com.feng.common.ErrorCode;
import lombok.Data;

@Data
public class BusinessException extends RuntimeException {
 private final  int code;

 private final String description;

    public BusinessException(ErrorCode code, String description) {
        super(code.getMessage());
        this.code=code.getCode();
        this.description = description;
    }
    public BusinessException(ErrorCode code) {
     this(code,"");
    }
}
