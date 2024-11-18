package dev.be.oneday.config;

import dev.be.oneday.service.util.Timer;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class WebFilterConfig {
    private final Timer timer;
    private final LatencyLogFilter latencyLogFilter;

    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(latencyLogFilter);
        filterFilterRegistrationBean.setOrder(1);
        filterFilterRegistrationBean.addUrlPatterns("/*");
//        filterFilterRegistrationBean.setEnabled(false);
        return filterFilterRegistrationBean;
    }
}
