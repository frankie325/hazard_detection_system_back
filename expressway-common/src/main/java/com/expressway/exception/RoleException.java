package com.expressway.exception;

/**
 * 部门相关业务异常
 */
public class RoleException extends RuntimeException {
  public RoleException(String message) {
    super(message);
  }
}