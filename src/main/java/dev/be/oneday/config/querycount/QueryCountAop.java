package dev.be.oneday.config.querycount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class QueryCountAop {
    private final QueryCounter queryCounter;

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Object getConnection(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        Object connection = proceedingJoinPoint.proceed();
        log.debug("커넥션 객체:"+connection);
        Object proxyInstance = Proxy.newProxyInstance(
                connection.getClass().getClassLoader(),
                connection.getClass().getInterfaces(),
                new ConnectionProxyHandler(connection, queryCounter)
        );
        log.debug(proxyInstance.toString());
        return proxyInstance;
    }
}
