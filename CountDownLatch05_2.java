import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/*
 * Ao criar diversas thread, pode ocorrer a situação que algumas threads iniciam o trabalho
 * antes de todas as threads serem criadas. Para isso é necessário utilizar a lógica demonstrada nesse trecho
 * de código, a fim de resolver esse problema durante a criação de um volume elevado de threads.
 */
public class CountDownLatch05_2 {
    static class WaitingWorker implements Runnable {
        private List<String> outputScraper;
        private CountDownLatch readyThreadCounter;
        private CountDownLatch callingThreadBlocker;
        private CountDownLatch completedThreadCounter;

        public WaitingWorker(
                List<String> outputScraper,
                CountDownLatch readyThreadCounter,
                CountDownLatch callingThreadBlocker,
                CountDownLatch completedThreadCounter) {
            this.outputScraper = outputScraper;
            this.readyThreadCounter = readyThreadCounter;
            this.callingThreadBlocker = callingThreadBlocker;
            this.completedThreadCounter = completedThreadCounter;
        }

        @Override
        public void run() {
            this.outputScraper.add("Worker " + this.hashCode() + " is ready!");
            readyThreadCounter.countDown();
            try {
                callingThreadBlocker.await();
                this.outputScraper.add("Counted down");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                completedThreadCounter.countDown();
            }
        }

    }

    public static void main(String args[]) throws InterruptedException {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch readyThreadCounter = new CountDownLatch(5);
        CountDownLatch callingThreadBlocker = new CountDownLatch(1);
        CountDownLatch completedThreadCounter = new CountDownLatch(5);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new WaitingWorker(
                        outputScraper,
                        readyThreadCounter,
                        callingThreadBlocker,
                        completedThreadCounter)))
                .limit(5)
                .collect(toList());

        workers.forEach(Thread::start);
        readyThreadCounter.await();
        outputScraper.add("Workers ready");
        callingThreadBlocker.countDown();
        completedThreadCounter.await();
        outputScraper.add("Workers complete");

        outputScraper.forEach(System.out::println);
    }
}
