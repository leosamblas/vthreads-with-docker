package com.example.vthreads;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ServiceTeste {

    @Async
    @SneakyThrows
    public void teste() {
        Thread.sleep(10000);
        log.info("VThread: {}", Thread.currentThread().isVirtual());
    }

    @SneakyThrows
    @Cacheable(cacheNames = "teste", key = "#name")
    public String getTeste(String name) {
        Thread.sleep(2000);
        log.info("CHAMADA REALIZADA");
        return name;
    }

}
