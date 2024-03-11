package org.seleniumbrain.lab.utility.polling;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class MethodPoller<T> {

    Duration pollDurationSec;
    int pollIntervalMillis;

    private Supplier<T> pollMethod = null;
    private Predicate<T> pollResultPredicate = null;

    public MethodPoller() {
    }

    public MethodPoller<T> poll(Duration pollDurationSec, int pollIntervalMillis) {
        this.pollDurationSec = pollDurationSec;
        this.pollIntervalMillis = pollIntervalMillis;
        return this;
    }

    public MethodPoller<T> method(Supplier<T> supplier) {
        pollMethod = supplier;
        return this;
    }

    public MethodPoller<T> until(Predicate<T> predicate) {
        pollResultPredicate = predicate;
        return this;
    }

    public T execute()
    {
        // TODO: Validate that poll, method and until have been called.

        T result = null;
        boolean pollSucceeded = false;
        // TODO: Add check on poll duration
        // TODO: Use poll interval
        while (!pollSucceeded) {
            result = pollMethod.get();
            pollSucceeded = pollResultPredicate.test(result);
        }

        return result;
    }

    public static void main(String[] args) {
        MethodPoller<String> poller = new MethodPoller<>();
        String uuidThatStartsWithOneTwoThree = poller.method(() -> UUID.randomUUID().toString())
                .until(s -> s.startsWith("123"))
                .execute();
        System.out.println(uuidThatStartsWithOneTwoThree.startsWith("123"));
        System.out.println(uuidThatStartsWithOneTwoThree);
    }
}