import java.util.concurrent.Semaphore;

/*
 * Exemplo que apenas mostra dois metodos da classe Semaphore
 */
public class Semaphore07 {

    static class Execute {
        public void execute() throws InterruptedException {
            System.out.println("Avaliable permit: " + semaphore.availablePermits());
            System.out.println("Number of threads waiting to acquire: " + semaphore.getQueueLength());

            if (semaphore.tryAcquire()) {
                try {

                } finally {
                    semaphore.release();
                }
            }
        }
    }

    static Semaphore semaphore = new Semaphore(10);

    public static void main(String... args) throws InterruptedException {
        new Execute().execute();
    }
}
