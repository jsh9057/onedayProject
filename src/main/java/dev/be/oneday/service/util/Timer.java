package dev.be.oneday.service.util;

import org.springframework.stereotype.Component;

@Component
public class Timer {
    private static final ThreadLocal<Long> THREAD_LOCAL = new ThreadLocal<>();

    public void start(){
        THREAD_LOCAL.set(System.currentTimeMillis());
    }

    public long getLatencyMillis(){
        final long latencyMillis = System.currentTimeMillis() - THREAD_LOCAL.get();
        timerRemove();

        return latencyMillis;
    }

    public void timerRemove(){
        THREAD_LOCAL.remove();
    }
}
