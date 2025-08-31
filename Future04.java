import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

public class Future04 {
    public static void main(String[] args){
       ExecutorService executorService = Executors.newFixedThreadPool(10); 
        Future<String> future = executorService.submit(() -> {
            System.out.println("main:excutorService:submit");
            Thread.sleep(10000L);
            return "Hello World";
        });
        if(future.isDone() && !future.isCancelled()){
            try {
                String str = future.get();
                System.out.println(str);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.println("main:end of method");
    }
}