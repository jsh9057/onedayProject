package dev.be.oneday.config.querycount;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConnectionProxyHandler implements InvocationHandler {

    private static final String QUERY_PREPARE_STATEMENT= "prepareStatement";

    private final Object connection;
    private final QueryCounter queryCounter;

    public ConnectionProxyHandler(Object connection, QueryCounter queryCounter) {
        this.connection = connection;
        this.queryCounter = queryCounter;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        countQuery(method);
        return method.invoke(connection,args);
    }

    private void countQuery(Method method){
        if(isPrepareStatement(method)){
            queryCounter.increaseCount();
        }
    }
    private boolean isPrepareStatement(Method method) {
        return method.getName().equals(QUERY_PREPARE_STATEMENT);
    }
}
