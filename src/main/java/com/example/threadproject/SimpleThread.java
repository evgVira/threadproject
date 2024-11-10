package com.example.threadproject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class SimpleThread {
    private static AtomicInteger finalValue = new AtomicInteger(0);
    private static List<Integer> numbers = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        Runnable task = () -> IntStream.range(0, 50)
                .forEach(i -> {
                    int currentValue = finalValue.incrementAndGet();
                    numbers.add(currentValue);
                });
        Thread firstThread = new Thread(task);
        Thread secondThread = new Thread(task);

        firstThread.start();
        secondThread.start();
        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception.getMessage());
        }
        System.out.printf("finalValue: %d, numbers size: %d\n", finalValue.intValue(), numbers.size());
        Collections.sort(numbers);
        System.out.println(numbers);
    }
}
