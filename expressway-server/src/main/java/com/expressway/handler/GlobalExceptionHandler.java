package com.expressway.handler;

import com.expressway.NotLoginException;
import com.expressway.exception.BusinessException;
import com.expressway.result.Result;
import com.expressway.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

/**
 * 全局异常处理器，统一返回异常结果
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 认证异常处理
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthException.class)
    public Result<Void> handleAuthException(AuthException e) {
        log.error("认证异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("业务异常：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    // 参数校验异常处理（@Validated注解触发）
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidException(MethodArgumentNotValidException e) {
        List<ObjectError> errors = e.getBindingResult().getAllErrors();
        String msg = errors.get(0).getDefaultMessage(); // 取第一个错误信息
        log.error("参数校验异常：{}", msg);
        return Result.error(msg);
    }

    // 未登录异常
    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Void> handleNotLoginException(NotLoginException e){
        log.error("用户登录异常：", e);
        return Result.error(e.getMessage());
    }

    // 通用异常处理
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常：", e);
        return Result.error("系统异常，请联系管理员");
    }
}