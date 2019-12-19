package com.example.Spring.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Scope
@Aspect
@Slf4j
public class LimitAspectV1 {
    //每秒只发出指定个令牌(这里方便测试用5个)，此处是单进程服务的限流,内部采用令牌捅算法实现
    private static RateLimiter rateLimiter = RateLimiter.create(5.0);

    //Controller层切点  限流
    @Pointcut("@annotation(com.example.Spring.annotation.RequestLimit)")
    public void requestAspect() {

    }


    @Around("requestAspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        ObjectMapper objectMapper = new ObjectMapper();

        //url
        log.info("url={}", request.getRequestURL());

        //method
        log.info("method={}", request.getMethod());

        //ip
        log.info("ip={}", request.getRemoteAddr());

        //类方法
        log.info("class_method={}", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        Boolean flag = rateLimiter.tryAcquire();
        Object obj = null;
        if (flag) {
            obj = joinPoint.proceed();
        } else {
            String message = "哎哟喂，业务太火爆，稍等一会儿呗";
            log.info(message);
            response.setContentType("application/json;charset=UTF-8");
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(message.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }
}
