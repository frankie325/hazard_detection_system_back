package com.expressway.exception;

/**
 * 部门相关业务异常
 */
public class DeptException extends RuntimeException {
  public DeptException(String message) {
    super(message);
  }
}