package com.quick.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.quick.common.constant.CommonConstant;
import com.quick.common.exception.code.ExceptionCode;
import com.quick.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public abstract class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> nullPointerException(Exception ex){
        log.error("NullPointerException:{}", ex);
        return Result.fail(ExceptionCode.NULL_POINT_EX.getCode(),ExceptionCode.NULL_POINT_EX.getMsg());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> sqlException(SQLException ex) {
        log.error("SQLException:{}", ex.getMessage());
        return Result.fail(ExceptionCode.SQL_EX.getCode(),ExceptionCode.SQL_EX.getMsg() );
    }

    @ExceptionHandler(OAuth2Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> exception(OAuth2Exception ex){
        log.error("OAuth2Exception:{}", ex.getMsg());
        return Result.fail(ex.getCode(),ex.getMsg());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> usernameNotFoundException(UsernameNotFoundException ex){
        log.error("UsernameNotFoundException:{}", ex.getMsg());
        return Result.fail(ex.getCode(),ex.getMsg());
    }

    @ExceptionHandler(NotPermissionException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> usernameNotFoundException(NotPermissionException ex){
        log.error("NotPermissionException:{}", ex.getMessage());
        return Result.fail(CommonConstant.FORBIDDEN,ex.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> notLoginException(NotLoginException ex){
        log.error("NotLoginException:{}", ex.getMessage());
        return Result.fail(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,ex.getMessage());
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> BizException(BizException ex){
        log.error("BizException:{}", ex.getMsg());
        return Result.fail(ex.getCode(),ex.getMsg());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> forbiddenException(ForbiddenException ex){
        log.error("ForbiddenException:{}", ex.getMsg());
        return Result.fail(ex.getCode(),ex.getMsg());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> exception(Exception ex){
        log.error("Exception:{}", ex);
        return Result.fail(ExceptionCode.SYSTEM_BUSY.getCode(),ExceptionCode.SYSTEM_BUSY.getMsg());
    }
}
