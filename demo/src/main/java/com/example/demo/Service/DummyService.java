package com.example.demo.Service;

import com.example.demo.exception.WrapperException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Service;

@Service
public class DummyService {

    @Autowired
    DummyComponent dummyComponent;

    @Autowired
    ApplicationContext context;

    @CircuitBreaker(name = "serviceCB", fallbackMethod = "fallback")
    public String doSomethingDangerous(String input) {
        try {
            context.getBean(DummyService.class).perform("input");
        } catch (WrapperException e) {
            System.out.println("Exception = " + e.getMessage());
        }
        return input;
    }

    @Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public String perform(String input) {
        int attempt = RetrySynchronizationManager.getContext() != null
                ? RetrySynchronizationManager.getContext().getRetryCount() + 1
                : 1;

        System.out.println("Trying perform() for input: " + input + " | Attempt: " + attempt);
        throw new RuntimeException("Simulated exception");
    }

    @Recover
    public String recover(RuntimeException e, String input) {
        System.out.println("Recovered after retries. Reason: " + e.getMessage());
        throw e;
    }

    public String fallback(String input, Throwable t) {
        System.out.println("Service fallback triggered due to: " + t.getMessage());
        return "Service fallback response for input: " + input;
    }
}
