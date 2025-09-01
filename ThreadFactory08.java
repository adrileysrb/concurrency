import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;
import java.util.concurrent.Executors;

public class ThreadFactory08 {
    static class ExampleThreadFactory implements ThreadFactory {
        private int threadId;
        private String name;

        public ExampleThreadFactory(String name) {
            this.threadId = 1;
            this.name = name;
        }

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable, name + "-Thread_" + threadId);
            System.out.println("Created new thread with id: " + threadId + " and name: " + thread.getName());
            this.threadId++;
            return thread;
        }
    }

    public static void main(String[] agrs) {
        ExampleThreadFactory exampleThreadFactory = new ExampleThreadFactory("Test");
        IntStream.range(0, 3)
                .forEach(i -> {
                    Thread newThread = exampleThreadFactory.newThread(() -> System.out.println("Thread has started"));
                    newThread.start();
                });

        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        IntStream.range(0, 2).forEach((i) -> {
            Thread thread = threadFactory.newThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Thread has been created using ThreadFactory");
                }
            });
            thread.start();
        });
    }
}
