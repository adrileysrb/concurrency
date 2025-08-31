import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Stream;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

/*
 * Caso ocorra um erro durante a execução do Worker (Thread) o CountDownLatch await nunca vai terminar.
 * Para resolver isso é necessário definir um tempo de espera para encerrar o processo caso ocorra um erro nos workers.
 */
public class CountDownLatch05_3 {
    static class BrokenWorker implements Runnable {
        private List<String> outputScraper;
        private CountDownLatch countDownLatch;

        public BrokenWorker(
                List<String> outputScraper,
                CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            if (true) {
                throw new RuntimeException("Broken Worker!");
            }
            countDownLatch.countDown();
            outputScraper.add("Counted down");
        }

    }

    public static void main(String args[]) throws InterruptedException {
        List<String> outputScraper = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch countDownLatch = new CountDownLatch(5);
        List<Thread> workers = Stream
                .generate(() -> new Thread(new BrokenWorker(outputScraper, countDownLatch)))
                .limit(5)
                .collect(toList());

        workers.forEach(Thread::start);
        boolean isCompletedWork = countDownLatch.await(3L, TimeUnit.SECONDS);
        System.out.println("Workers done the work? " + isCompletedWork);
        outputScraper.forEach(System.out::println);
    }
}
