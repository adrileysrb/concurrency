import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledFuture;

public class ScheduledExecutorService03 {
    public static void main(String args[]) {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        Future<String> future = scheduledExecutorService.schedule(() -> {
            System.out.println("Scheduled:scheduled");
            return "Hello World";
        }, 10, TimeUnit.SECONDS);

        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("Scheduled::scheduleWithFixedDelay");
        }, 1, 1, TimeUnit.SECONDS);

        scheduledExecutorService.shutdown();
    }
}
