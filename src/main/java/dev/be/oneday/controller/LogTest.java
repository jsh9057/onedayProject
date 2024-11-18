package dev.be.oneday.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logtest")
@RequiredArgsConstructor
@Slf4j
public class LogTest {

    @GetMapping
    public void test(){
        log.info("INFO HELLO");
        log.error("INFO HELLO");
        log.trace("INFO HELLO");
        log.debug("INFO HELLO");
    }


}
