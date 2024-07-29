package com.example.vthreads;

import java.time.Duration;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@EnableAsync
@RestController
@SpringBootApplication
public class VthreadsApplication {

	private ServiceTeste serviceTeste;

	public VthreadsApplication(ServiceTeste serviceTeste) {
		this.serviceTeste = serviceTeste;
	}

	public static void main(String[] args) {
		SpringApplication.run(VthreadsApplication.class, args);
	}

	@GetMapping("/hello")
	public ResponseEntity<String> hello() {
		serviceTeste.teste();
		return ResponseEntity.ok("Hello, World!");
	}

	@GetMapping("/teste")
	public ResponseEntity<String> teste(@RequestParam("param") String name) {
		log.info("TESTE CACHE");
		return ResponseEntity.ok(serviceTeste.getTeste(name));
	}

	@Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
	public AsyncTaskExecutor asyncTaskExecutor() {
		return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
	}

	@Bean
	public RedisCacheConfiguration defaultCacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofSeconds(10))
				.disableCachingNullValues()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
	}

	@Bean
	public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
		return builder -> builder.withCacheConfiguration("teste",
				RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(20)));
	}
}
