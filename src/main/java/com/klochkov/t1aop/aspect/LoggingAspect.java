package com.klochkov.t1aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Pointcut("within(com.klochkov.t1aop.service.impl.*ServiceImpl)")
    public void isServiceLayer() {
    }

    @Pointcut("isServiceLayer() && execution(public * getUserById(java.util.UUID))")
    public void isFindUserByIdInService() {
    }

    @Pointcut("isServiceLayer() && execution(public com.klochkov.t1aop.entity.Order getOrderById(java.util.UUID))")
    public void isFindOrderByIdInService() {
    }

    @Pointcut("within(com.klochkov.t1aop.service.impl.UserServiceImpl) && " +
            "execution(public com.klochkov.t1aop.entity.User addOrder(java.util.UUID, java.util.UUID))")
    public void isAddOrderInUserService() {
    }


    @Before("isFindUserByIdInService() && args(id)")
    public void findUserByIdBefore(JoinPoint joinPoint, Object id) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} started for searching entity with id = {}", methodName, id);
    }
    
    @AfterReturning(value = "isFindUserByIdInService()")
    public void findUserByIdAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("{} returned entity with id = {} ", methodName, joinPoint.getArgs()[0]);
    }

    @AfterThrowing(value = "isFindUserByIdInService() && args(id)", throwing = "ex")
    public void findUserByIdWithException(JoinPoint joinPoint, Object id, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} which searched entity with id = {} throw exception with message: {}", methodName, id,
                ex.getMessage());
    }
    
    
    

    @Before("isFindOrderByIdInService() && args(id)")
    public void findOrderByIdBefore(JoinPoint joinPoint, Object id) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} started for searching entity with id = {}", methodName, id);
    }

    @AfterReturning(value = "isFindOrderByIdInService() && args(id)")
    public void addLoggingToFindOrderByIdAfter(JoinPoint joinPoint, Object id) {
        String methodName = joinPoint.getSignature().getName();
        log.info("{} returned entity with id = {} ", methodName, id);
    }

    @AfterThrowing(value = "isFindOrderByIdInService() && args(id)", throwing = "ex")
    public void findOrderByIdWithException(JoinPoint joinPoint, Object id, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} which searched entity with id = {} throw exception with message: {}", methodName, id,
                ex.getMessage());
    }



    @Before("isAddOrderInUserService()")
    public void addOrderOfUserServiceAtStart(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method {} started for adding order with id = {} to user with id = {}", methodName, args[0], args[1]);
    }

    @AfterReturning(value = "isAddOrderInUserService()", returning = "result")
    public void addOrderOfUserServiceAtFinish(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method {} for adding order with id = {} to user with id = {} returned result: {}", methodName,
                args[0], args[1], result);
    }

    @AfterThrowing(value = "isAddOrderInUserService()", throwing = "ex")
    public void addOrderOfUserServiceWithException(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Method {} for adding order with id = {} to user with id = {} throw exception with message: {}", methodName,
                args[0], args[1], ex.getMessage());
    }
}
