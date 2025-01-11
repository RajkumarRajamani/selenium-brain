package org.seleniumbrain.lab.utility;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryController<T> {

    private final int maxRetries;

    public RetryController(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    // Takes a function and executes it, if fails, passes the function to the retry command
    public T run(Supplier<T> function) {
        try {
            log.info("Initial Try...");
            return function.get();
        } catch (Exception e) {
            log.error("Initial attempt failed. It will be retried " + maxRetries + " times.");
            return retry(function);
        }
    }

    public T forceRetryIgnoringExceptions(Supplier<T> function) {
        int retryCounter = 1;
        while (retryCounter < maxRetries) {
            try {
                log.info("Retry count " + retryCounter);
                function.get();
            } finally {
                retryCounter++;
            }
        }
        try {
            return function.get();
        } catch (Exception e) {
            log.error("Failed after max retry limit " + maxRetries);
            throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
        }
    }

    private T retry(Supplier<T> function) throws RuntimeException {

        int retryCounter = 1;
        while (retryCounter < maxRetries) {
            try {
                log.info("Retry count " + retryCounter);
                return function.get();
            } catch (Exception ex) {
                retryCounter++;
                if (retryCounter >= maxRetries) {
                    log.error("Failed after max retry limit " + maxRetries);
                    break;
                }
            }
        }
        throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
    }

}