package com.example.integrahubspot.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class RateLimiter {

    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RateLimiter(int maxRequests, int periodInSeconds) {
        semaphore = new Semaphore(maxRequests);
        scheduler.scheduleAtFixedRate(() -> semaphore.release(maxRequests - semaphore.availablePermits()), periodInSeconds, periodInSeconds, TimeUnit.SECONDS);
    }

    public boolean tryConsume() {
        return semaphore.tryAcquire();
    }

}
