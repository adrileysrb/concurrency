import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class Semaphore07_3 {
    static class CounterUsingMutex {
        private Semaphore mutex; // Mutual Exclusion
        private int count;

        public CounterUsingMutex() {
            this.mutex = new Semaphore(1);
            this.count = 0;
        }

        void increase() throws InterruptedException {
            mutex.acquire();
            this.count = this.count + 1;
            Thread.sleep(1000);
            mutex.release();
        }

        int getCount() {
            return this.count;
        }

        boolean hasQueuedThreads() {
            return mutex.hasQueuedThreads();
        }
    }

    static public void main(String... args) throws InterruptedException {
        int count = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CounterUsingMutex counterUsingMutex = new CounterUsingMutex();

        IntStream.range(0, count)
                .forEach(i -> {
                    executorService.execute(() -> {
                        try {
                            counterUsingMutex.increase();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                });
        executorService.shutdown();

        Thread.sleep(500); // Dá tempo para as threads competirem pelo semáforo
        System.out.println("CounterUsingMutex:hasQueuedThreads:chamada1:" + counterUsingMutex.hasQueuedThreads());
        System.out.println("CounterUsingMutex:getCount:chamada1:" + counterUsingMutex.getCount());
        Thread.sleep(5000); // Tempo suficiente para todas as threads passarem pelo semáfo, logo, não irá ter alguma impedida.
        System.out.println("CounterUsingMutex:hasQueuedThreads:chamada2:" + counterUsingMutex.hasQueuedThreads());
        System.out.println("CounterUsingMutex:getCount:chamada2:" + counterUsingMutex.getCount());
    }
}
