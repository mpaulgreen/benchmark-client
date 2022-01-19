package com.redhat.database.benchmark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.acme.mongodb.Fruit;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class BenchmarkRunner {
    private Logger logger = LoggerFactory.getLogger(BenchmarkRunner.class);
    @ConfigProperty(name = "process-api/mp-rest/url")
    private String serverUrl;

    @RestClient
    @Inject
    private BenchmarkService benchmarkService;

    public String run(int noOfTests, int noOfThreads) throws JsonProcessingException,
            InterruptedException {
        TestMetrics metrics = new Worker(noOfTests, noOfThreads).run();
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(metrics);
    }

    private class Worker {
        private int durationInSeconds;
        private int noOfThreads;
        private AtomicLong itemsCounter = new AtomicLong(0);
        AtomicBoolean timerElapsed = new AtomicBoolean(false);

        private Stats stats;

        private Worker(int durationInSeconds, int noOfThreads) {
            this.durationInSeconds = durationInSeconds;
            this.noOfThreads = noOfThreads;
            this.stats = new Stats();
        }

        private TestMetrics run() throws InterruptedException {
            logger.info("Ready to run from {}: {} seconds in {} threads", serverUrl, durationInSeconds,
                    noOfThreads);
            ExecutorService executor = Executors.newFixedThreadPool(noOfThreads);
            Timer timer = new Timer("timer");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    logger.info("Timer elapsed");
                    timerElapsed.set(true);
                    timer.cancel();
                }
            }, durationInSeconds * 1000);

            Collection<Callable<Void>> callables =
                    IntStream.rangeClosed(1, noOfThreads).mapToObj(n -> newCallable()).collect(Collectors.toList());
            executor.invokeAll(callables);

            TestMetrics metrics = stats.build();
            logger.info("Completed {} tests in {}ms", metrics.getNoOfExecutions(), metrics.getElapsedTimeMillis());
            return metrics;
        }

        private Callable<Void> newCallable() {
            return () -> {
                while (!timerElapsed.get()) {
                    long index = itemsCounter.incrementAndGet();
                    Execution execution = stats.startOne(index);
                    try {
                        executorOfType().execute();
                        execution.stop();
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Failed to run: {}", e.getMessage());
                        execution.failed();
                    }
                }
                return null;
            };
        }

        private RestExecutor executorOfType() {
            return fruits;
        }


        private Supplier<Fruit> newFruit = () -> {
            String uuid = UUID.randomUUID().toString();
            Fruit fruit = new Fruit();
            fruit.setId(uuid);
            fruit.setName(uuid);
            fruit.setDescription(uuid);
            return fruit;
        };

        private final RestExecutor fruits = () ->
                benchmarkService.add(newFruit.get());

    }

    @FunctionalInterface
    interface RestExecutor {
        void execute() throws Exception;
    }
}