package ru.lesson.wallet.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {
    @Value("${app.threadPool.corePoolSize}")
    private int corePoolSize;

    @Value("${app.threadPool.maxPoolSize}")
    private int maxPoolSize;

    @Value("${app.threadPool.queueCapacity}")
    private int queueCapacity;

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("WalletApp-");
        executor.initialize();
        return executor;
    }
}
