package dev.be.oneday.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class MonitoringService {

    private final EmailNotifier emailNotifier;
    private final AtomicInteger errorCount = new AtomicInteger(0);

    private final int ERROR_THRESHOLD = 10;
    private final double MEMORY_THRESHOLD_PERCENT = 0.80; // 0~1 의 값


    private final String ADMIN_MAIL = "jsh9057@gmail.com";
    private final String ERROR_LOG_TITLE = "ERROR 로그 갯수가 임계치 %d 개를 넘었습니다";
    private final String ERROR_LOG_MESSAGE = "현재 ERROR 로그 갯수: %d \n"
                                          + "확인 부탁드립니다.";
    private final String MEMORY_WARNING_TITLE = "메모리 사용량이 임계치 %.1f%%를 넘었습니다";
    private final String MEMORY_WARNING_MESSAGE = "현재 힙 메모리 사용량: %dMB \n"
                                                    + "최대 힙 메모리: %dMB\n"
                                                    + "확인 부탁드립니다.";
    private final long MEMORY_MAX_SIZE;
    private final long alertCooldown = 15*60*1000; // 15분
    private long lastAlertTime = 0;

    public MonitoringService(EmailNotifier emailNotifier) {
        this.emailNotifier = emailNotifier;
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        MEMORY_MAX_SIZE=heapMemoryUsage.getMax();;
    }

    @Scheduled(fixedRate = 5000)
    @Async
    public void checkMetrics(){
        int currentCount = errorCount.get();

        if(currentCount >= ERROR_THRESHOLD){
            String title = String.format(ERROR_LOG_TITLE,ERROR_THRESHOLD);
            String body = String.format(ERROR_LOG_MESSAGE,currentCount);
            emailNotifier.sendEmail(ADMIN_MAIL,title,body);
            errorCount.set(0);
        }

        double memoryUsage = getMemoryUsage();
//        log.debug(String.format("메모리 사용량:%.1f%%  전체 메모리:%dMB",memoryUsage/MEMORY_MAX_SIZE*100, MEMORY_MAX_SIZE/1024/1024));
        if (memoryUsage/MEMORY_MAX_SIZE >= MEMORY_THRESHOLD_PERCENT) {
            long currentTime = System.currentTimeMillis();

            if (currentTime - lastAlertTime > alertCooldown) {
                String title = String.format(MEMORY_WARNING_TITLE, MEMORY_THRESHOLD_PERCENT*100);
                String body = String.format(MEMORY_WARNING_MESSAGE, (long)memoryUsage/1024/1024, MEMORY_MAX_SIZE/1024/1024);
                emailNotifier.sendEmail(ADMIN_MAIL, title, body);
                lastAlertTime = currentTime; // 알림 전송 시간 갱신
            }
        }
    }
    private double getMemoryUsage() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        return heapMemoryUsage.getUsed(); // 사용 중인 힙 메모리
    }

    public void incrementErrorCount() {
        errorCount.incrementAndGet(); // 스레드 안전하게 에러 카운트 증가
    }
}
