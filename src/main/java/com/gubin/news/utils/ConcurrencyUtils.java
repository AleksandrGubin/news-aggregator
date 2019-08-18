package com.gubin.news.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public enum ConcurrencyUtils {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(ConcurrencyUtils.class);
    private static final int DEFAULT_THREAD_COUNT = 2;
    private static final int DEFAULT_AWAIT_TERMINATION_TIMEOUT = 3;

    public void invokeAll(Collection<Runnable> tasks) {
        invokeAll(tasks, DEFAULT_THREAD_COUNT);
    }

    public void invokeAll(Collection<Runnable> tasks, int threadCount) {
        invokeAll(tasks, threadCount, DEFAULT_AWAIT_TERMINATION_TIMEOUT);
    }

    public void invokeAll(Collection<Runnable> tasks, int threadCount, int awaitTerminationTimeout) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        try {
            executor.invokeAll(toCallables(tasks));
        } catch (InterruptedException e) {
            logger.warn(String.format("Thread interrupted exception: %s", e.getMessage()));
        } finally {
            shutdownAndAwaitTermination(executor, awaitTerminationTimeout, TimeUnit.SECONDS);
        }
    }

    private Collection<Callable<Void>> toCallables(Collection<Runnable> runnables) {
        if (CollectionUtils.isEmpty(runnables)) {
            return Collections.emptyList();
        }
        return runnables.stream().map(r -> (Callable<Void>) () -> {
            r.run();
            return null;
        }).collect(Collectors.toList());
    }

    private void shutdownAndAwaitTermination(ExecutorService executor, int timeout, TimeUnit unit) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(timeout, unit)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            logger.warn(String.format("Interrupted exception when shutdown executor: %s", ie.getMessage()));
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
