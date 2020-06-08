package com.hyhello.priceless.support;

import com.hyhello.priceless.dto.exception.AppException;
import com.hyhello.priceless.dto.resp.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Component
@Aspect
@Slf4j
public class ExceptionAOPSupport {

    @Around("execution(public * com.hyhello.priceless.web.controller..*.*(..))")
    public Object serviceAOP(ProceedingJoinPoint pjp) throws Exception {

        try {
            return pjp.proceed();
        } catch (AppException e) {
            log.error("error with AppException " , e);
            return ErrorResponse.AppError(e);
        } catch (Throwable e) {
            log.error("error with Throwable " , e);
            return ErrorResponse.SystemError(e);
        }
    }
}
