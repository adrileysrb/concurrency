import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/*
 * Espera a execução de um conjunto de threads antes de continuar a execução do método.
 */
public class CountDownLatch05 {
    static class Worker implements Runnable {
        private List<String> outputScraper;
        private CountDownLatch countDownLatch;

        public Worker(List<String> outputScraper, CountDownLatch countDownLatch) {
            this.outputScraper = outputScraper;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            this.outputScraper.add("Counted down");
            this.countDownLatch.countDown();
        }

    }

    public static void main(String args[]) throws InterruptedException {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(5);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new Worker(outputScraper, countDownLatch)))
                .limit(5)
                .collect(toList());

        workers.forEach(Thread::start);
        // countDownLatch.await();
        outputScraper.add("Latch released");

        outputScraper.forEach(System.out::println);
    }
}
