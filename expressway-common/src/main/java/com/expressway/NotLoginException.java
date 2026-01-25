package com.expressway;

public class NotLoginException extends RuntimeException {
    public NotLoginException(String message) {
        super(message);
    }

    public NotLoginException(){
      super("用户未登录，请先登录");
    }
}
