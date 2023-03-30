package ru.clevertec.service;

import lombok.Getter;

import ru.clevertec.model.Request;
import ru.clevertec.model.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
public class Client {

    private final List<Integer> values;
    private List<Future<Response>> futureResponses;
    private final ExecutorService executorService;
    private final Lock lock;
    private final Random random;
    private int accumulator;

    public Client(int numberValues, int numberThreads) {
        this.values = IntStream.rangeClosed(1, numberValues)
                .boxed()
                .collect(Collectors.toList());
        this.futureResponses = new ArrayList<>();
        this.executorService = Executors.newFixedThreadPool(numberThreads);
        this.lock = new ReentrantLock();
        this.random = new Random();
    }

    public void sendRequests(Server server) {
        int numberRequests = values.size();

        futureResponses = IntStream.range(0, numberRequests)
                .mapToObj(numberRequest -> (Callable<Response>) () -> {
                    int removedValue = removeRandomValue();
                    return server.processRequest(new Request(removedValue));
                })
                .map(executorService::submit)
                .toList();
        accumulateResponses();

        executorService.shutdown();
    }

    private void accumulateResponses() {
        accumulator = futureResponses.stream()
                .mapToInt(this::getResponseValue)
                .sum();
    }

    private int removeRandomValue() {
        lock.lock();

        try {
            int randomIndex = random.nextInt(values.size());
            return values.remove(randomIndex);
        } finally {
            lock.unlock();
        }
    }

    private int getResponseValue(Future<Response> futureResponse) {
        try {
            return futureResponse.get().getValue();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
