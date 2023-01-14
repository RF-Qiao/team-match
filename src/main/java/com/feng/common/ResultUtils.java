package com.feng.common;
public class ResultUtils {
    public static<T> BaseResponse<T> success( T data)
    {
        return  new BaseResponse<>(200,data,"ok","");
    }
    public static<T> BaseResponse<T> success( T data,String token)
    {
        return  new BaseResponse<>(200,data,"ok",token);
    }
    public static<T> BaseResponse<T> error(ErrorCode errorCode ,String message){
        return  new BaseResponse<>(errorCode,message);
    }
    public static<T> BaseResponse<T> error(ErrorCode errorCode ,String message,String description){
        return  new BaseResponse<>(errorCode,message,description);
    }
    public static<T> BaseResponse<T> error(ErrorCode errorCode){
        return  new BaseResponse<>(errorCode);
    }
}