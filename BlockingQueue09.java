import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueue09 {
    static class NumbersProducer implements Runnable {
        private BlockingQueue<Integer> numbersQueue;
        private final int poisonPill;
        private final int poisonPillPerProducer;

        @Override
        public void run() {
            try {
                generateNumbers();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        public NumbersProducer(
                BlockingQueue<Integer> numbersQueue,
                int poisonPill,
                int poisonPillPerProducer) {
            this.numbersQueue = numbersQueue;
            this.poisonPill = poisonPill;
            this.poisonPillPerProducer = poisonPillPerProducer;
        }

        private void generateNumbers() throws InterruptedException {
            for (int i = 0; i < 100; i++) {
                numbersQueue.put(ThreadLocalRandom.current().nextInt(100));
            }
            for (int i = 0; i < poisonPillPerProducer; i++) {
                numbersQueue.put(poisonPill);
            }
        }

    }

    static class NumbersConsumer implements Runnable {
        private BlockingQueue<Integer> queue;
        private final int poisonPill;

        public NumbersConsumer(BlockingQueue<Integer> queue, int poisonPill) {
            this.queue = queue;
            this.poisonPill = poisonPill;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Integer number = queue.take();
                    if (number.equals(poisonPill)) {
                        return;
                    }
                    System.out.println(Thread.currentThread().getName() + " result: " + number);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public static void main(String... args) {
        int BOUND = 10;
        int NUMBER_PRODUCERS = 4;
        int NUMBER_CONSUMERS = Runtime.getRuntime().availableProcessors();
        int poisonPill = Integer.MAX_VALUE;
        int poisonPillPerProducer = NUMBER_CONSUMERS / NUMBER_PRODUCERS;
        int mod = NUMBER_CONSUMERS % NUMBER_PRODUCERS;

        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(BOUND);
        for (int i = 1; i < NUMBER_PRODUCERS; i++) {
            new Thread(new NumbersProducer(blockingQueue, poisonPill, poisonPillPerProducer)).start();
        }

        for (int i = 0; i < NUMBER_CONSUMERS; i++) {
            new Thread(new NumbersConsumer(blockingQueue, poisonPill)).start();
        }

        new Thread(new NumbersProducer(blockingQueue, poisonPill, poisonPillPerProducer + mod)).start();
    }

}
