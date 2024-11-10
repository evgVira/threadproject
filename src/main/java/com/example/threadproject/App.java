package com.example.threadproject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class App {

    private static final int COUNT = 100000;
    private static final int THREAD_COUNT = 10;
    private static AtomicInteger mainCounter = new AtomicInteger(0);
    private static List<Integer> numbers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        Runnable task = () -> IntStream.range(0, COUNT / THREAD_COUNT)
                .forEach(i -> {
                    int currentValue = mainCounter.incrementAndGet();
                    numbers.add(currentValue);
                });

        ExecutorService threads = Executors.newFixedThreadPool(THREAD_COUNT);
        IntStream.range(0, THREAD_COUNT)
                .forEach(i -> threads.execute(task));
        System.out.println("threads start");

        try{
            System.out.println("waiting threads to finish");
            SECONDS.sleep(5);
            threads.shutdownNow();
        }catch (InterruptedException exception){
            Thread.currentThread().interrupt();
        }

        Collections.sort(numbers);
        int lasValue = numbers.get(numbers.size() - 1);
        System.out.printf("mainCounter: %d, numbers size: %d, last value into numbers: %d", mainCounter.intValue(), numbers.size(), lasValue);
    }

}

