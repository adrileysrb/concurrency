import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorService02 {

    static class Task implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                System.out.println("Thread ended before complete our task!");
            }
            System.out.println("Task:run");
        }
    }

    public static void main(String... args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.submit(new Task());
        // executorService.shutdown(); Completa as tarefas antes de encerrar
        executorService.shutdownNow();
    }
}
