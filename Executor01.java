import java.util.concurrent.Executor;

public class Executor01 {

    static class Invoker implements Executor {
        @Override
        public void execute(Runnable command) {
            System.out.println("Invoker:execute");
            command.run();
        }
    }

    public static void main(String... args) {
        Executor executor = new Invoker();
        executor.execute(() -> {
            System.out.println("main:Executor:execute:consumer");
        });
    }
}
