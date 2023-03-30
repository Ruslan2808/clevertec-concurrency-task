package ru.clevertec.service;

import lombok.Getter;

import ru.clevertec.model.Request;
import ru.clevertec.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Server {

    private static final int LOWER_DELAY_RANGE = 100;
    private static final int UPPER_DELAY_RANGE = 1000;

    private final List<Integer> values;
    private final Lock lock;
    private final Random random;

    public Server() {
        this.lock = new ReentrantLock();
        this.random = new Random();
        this.values = new ArrayList<>();
    }

    public Response processRequest(Request request) {
        lock.lock();

        try {
            int delay = getRandomDelay();
            Thread.sleep(delay);

            values.add(request.getValue());
            return new Response(values.size());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public int getRandomDelay() {
        return random.nextInt(UPPER_DELAY_RANGE - LOWER_DELAY_RANGE + 1) + LOWER_DELAY_RANGE;
    }
}
