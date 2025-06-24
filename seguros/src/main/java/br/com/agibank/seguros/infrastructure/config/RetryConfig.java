package br.com.agibank.seguros.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean(name = "retryListener")
    public RetryListener retryListener() {
        return new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext context, RetryCallback<T, E> callback) {
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                if (throwable != null) {
                    // Log error on close if needed
                }
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                int retryCount = context.getRetryCount();
                if (retryCount > 0) {
                    System.out.printf("Tentativa %d falhou. Próxima tentativa em breve...%n", retryCount);
                }
            }
        };
    }
}
