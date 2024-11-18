package dev.be.oneday.config;

import dev.be.oneday.service.util.Timer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class LatencyLogFilter implements Filter {
    private final Timer timer;
    private static final String LATENCY_LOG_FORM = "METHOD: {}, URL: {}, STATUS_CODE: {}, Latency: {}ms";
    private static final String REQUEST_FORM = "REQUEST: {}";
    private static final String RESPONE_FORM = "RESPONSE: {}";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // ServletRequest/Response 의 inputStream 은 1회용이기때문에, 캐싱해서 데이터를 읽어야 합니다.
        // ContentCachingRequestWrapper 들은 값이 한번이상 읽혀야 캐싱되기때문에 doFilter 호출전에는 아무값도 담기지않습니다.

//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        String uri = httpServletRequest.getRequestURI();
//        String method = httpServletRequest.getMethod();

        ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
        ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        try{
            timer.start();
            chain.doFilter(contentCachingRequestWrapper,contentCachingResponseWrapper);
            log.debug(REQUEST_FORM,new String(contentCachingRequestWrapper.getContentAsByteArray()));
            log.debug(RESPONE_FORM,new String(contentCachingResponseWrapper.getContentAsByteArray()));
            contentCachingResponseWrapper.copyBodyToResponse();
            log.info(LATENCY_LOG_FORM, contentCachingRequestWrapper.getMethod(),contentCachingRequestWrapper.getRequestURI(),contentCachingResponseWrapper.getStatus(),timer.getLatencyMillis());
        }catch (Exception e){
            log.warn(LATENCY_LOG_FORM,  contentCachingRequestWrapper.getMethod(),contentCachingRequestWrapper.getRequestURI(),contentCachingResponseWrapper.getStatus(),timer.getLatencyMillis());
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
