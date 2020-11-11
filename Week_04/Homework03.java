package java0.conc0303;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;

import static java0.conc0303.HomeworkHelper.*;

/**
 * 本周作业：（必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * <p>
 * 一个简单的代码参考：
 */
public class Homework03 {
    static int Result;

    public static void main(String[] args) throws ExecutionException, InterruptedException, BrokenBarrierException {
        // 1. FutureTask
        LocalTime start = LocalTime.now();
        FutureTask<Integer> task1 = new FutureTask<>(new ByCallable());
        new Thread(task1).start();
        printTaskResult("FutureTask", task1.get(), start);
        // 2.Future
        start = LocalTime.now();
        Future<Integer> task2 =
                Executors.newCachedThreadPool()
                        .submit(() -> new ByCallable().call());
        while (!task2.isDone()) {
        //System.out.println("task2 wait");
        }
        printTaskResult("Future", task2.get(), start);
        // 3.CompletableFuture
        final LocalTime startTime = LocalTime.now();
        CompletableFuture
                .supplyAsync(() -> new ByCallable().call())
                .thenAccept(res -> printTaskResult("CompletableFuture", res, startTime));
        // 4. CountDownLatch
        start = LocalTime.now();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        new Thread(new ByThread(countDownLatch)).start();
        countDownLatch.await();
        printTaskResult("CountDownLatch", Result, start);
        // 5.CyclicBarrier
        start = LocalTime.now();
        Result = 0;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Thread task5 = new Thread(() -> {
            Result = Invoke.fibo(DEFAULT_FIBO_VALUE);
            try {
                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });
        task5.start();
        cyclicBarrier.await();
        printTaskResult("CyclicBarrier", Result, start);
    }
}

class ByCallable implements Callable<Integer> {
    @Override
    public Integer call() {
        return Invoke.fibo(DEFAULT_FIBO_VALUE);
    }
}

class ByThread implements Runnable {
    private final CountDownLatch countDownLatch;

    public ByThread(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        Homework03.Result = Invoke.fibo(DEFAULT_FIBO_VALUE);
        countDownLatch.countDown();
    }
}

class Invoke {
    public static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }
}

/**
 * 辅助类
 */
class HomeworkHelper {
    static final int DEFAULT_FIBO_VALUE = 30;

    static void printTaskResult(String desc, int taskResult, LocalTime startTime) {
        if (startTime == null) {
            return;
        }
        LocalTime endTime = LocalTime.now();
        long duration = ChronoUnit.MILLIS.between(startTime, endTime);
        System.out.println(
                String.format("[实现方式: %s], 返回值为: %d, 执行时间为 %d Millis",
                        desc, taskResult, duration)
        );
    }
}