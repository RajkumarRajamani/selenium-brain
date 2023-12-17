package org.seleniumbrain.lab.utility;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryCommandBackup<T> {

    private final int maxRetries;

    public RetryCommandBackup(int maxRetries)
    {
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
        int retryCounter = 0;
        while(retryCounter < maxRetries) {
            try {
                log.info("Running " + retryCounter + " Retry...");
                function.get();
            } catch(Exception e) {}
            retryCounter++;
        }
        try {
            return function.get();
        } catch (Exception e) {
            log.error("FAILED - Command failed, will be retried " + maxRetries + " times.");
            return retry(function);
        }
    }

    private T retry(Supplier<T> function) throws RuntimeException {

        int retryCounter = 0;
        while (retryCounter < maxRetries) {
            try {
                log.info("Running " + retryCounter + " Retry...");
                return function.get();
            } catch (Exception ex) {
                retryCounter++;
                log.error("FAILED - Command failed on retry " + retryCounter + " of " + maxRetries, ex);
                if (retryCounter >= maxRetries) {
                    log.error("Max retries exceeded.");
                    break;
                }
            }
        }
        throw new RuntimeException("Command failed on all of " + maxRetries + " retries");
    }

}