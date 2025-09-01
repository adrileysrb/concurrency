import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.DelayQueue;

import java.util.Random;

/*
 * Queue producer-consumer em que a ordem de consumo é definida pela expiração dos registros na fila. Um registro só é consumido quando expirado.
 */
public class DelayQueue10 {

    static class DelayObject implements Delayed {
        private String data;
        private long startTime;

        public DelayObject(String data, long delayInMilliseconds) {
            this.data = data;
            this.startTime = System.currentTimeMillis() + delayInMilliseconds;
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.startTime, ((DelayObject) o).startTime);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = this.startTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "Name: " + data;
        }

    }

    static class DelayQueueProducer implements Runnable {
        private BlockingQueue<DelayObject> queue;
        private Integer numberOfElementsToProduce;

        public DelayQueueProducer(BlockingQueue<DelayQueue10.DelayObject> queue, Integer numberOfElementsToProduce) {
            this.queue = queue;
            this.numberOfElementsToProduce = numberOfElementsToProduce;
        }

        @Override
        public void run() {
            for (int i = 0; i < numberOfElementsToProduce; i++) {
                Random random = new Random();
                int threadSleep = random.nextInt(50000) + 100;
                String name = "name-" + i;
                System.out.println("threadSleep: " + threadSleep + " - name: " + name);
                DelayObject delayObject = new DelayObject(
                        name,
                        threadSleep);
                System.out.println("Put object: " + delayObject);
                try {
                    queue.put(delayObject);
                    // Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class DelayQueueConsumer implements Runnable {
        private BlockingQueue<DelayObject> blockingQueue;
        private Integer numberOfElementsToTake;
        private AtomicInteger numberOfConsumedElements = new AtomicInteger();

        public DelayQueueConsumer(BlockingQueue<DelayObject> blockingQueue,
                Integer numberOfElementsToTake) {
            this.blockingQueue = blockingQueue;
            this.numberOfElementsToTake = numberOfElementsToTake;
        }

        @Override
        public void run() {
            for (int i = 0; i < numberOfElementsToTake; i++) {
                try {
                    DelayObject delayObject = blockingQueue.take();
                    numberOfConsumedElements.incrementAndGet();
                    System.out.println("Consumer take: " + delayObject);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        BlockingQueue<DelayObject> blockingQueue = new DelayQueue<>();
        int numberOfElementsToProduce = 5;
        DelayQueueProducer delayQueueProducer = new DelayQueueProducer(blockingQueue,
                numberOfElementsToProduce);
        DelayQueueConsumer delayQueueConsumer = new DelayQueueConsumer(blockingQueue, numberOfElementsToProduce);

        executor.submit(delayQueueProducer);
        executor.submit(delayQueueConsumer);

        executor.awaitTermination(5, TimeUnit.SECONDS);
        executor.shutdown();

        System.out.println(delayQueueConsumer.numberOfConsumedElements.get() + " - " + numberOfElementsToProduce);
    }
}
