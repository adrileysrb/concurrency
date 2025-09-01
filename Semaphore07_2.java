import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import java.util.stream.IntStream;
/*
 * Implementação de sistema de login utilizando Semaphore. É definido o número de threads
 * que podem atuar em um ponto crítico do sistema, ao atingir esse número, não é possível adicionar mais
 * e se faz necessário que uma vaga seja liberada através do metodo release.
 */
public class Semaphore07_2 {
    static class LoginQueueUsingSemaphore {
        private Semaphore semaphore;

        public LoginQueueUsingSemaphore(int slotLimit) {
            semaphore = new Semaphore(slotLimit);
        }

        boolean tryLogin() {
            return semaphore.tryAcquire();
        }

        void logout() {
            semaphore.release();
        }

        int availableSlots() {
            return semaphore.availablePermits();
        }
    }

    public static void main(String... args) {
        int slots = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(slots);
        LoginQueueUsingSemaphore loginQueueUsingSemaphore = new LoginQueueUsingSemaphore(slots);

        IntStream.range(0, slots)
                .forEach((user) -> {
                    executorService.execute(loginQueueUsingSemaphore::tryLogin);
                    System.out.println("IntStream:number:" + user);
                });
        executorService.shutdown();

        System.out.println("LoginQueueUsingSemaphore:availablePermits:" + loginQueueUsingSemaphore.availableSlots());
        System.out.println("LoginQueueUsingSemaphore:tryAcquire:" + loginQueueUsingSemaphore.tryLogin());
        
        // System.out.println("LoginQueueUsingSemaphore:release");
        // loginQueueUsingSemaphore.logout();
        // System.out.println("LoginQueueUsingSemaphore:availablePermits:" + loginQueueUsingSemaphore.availableSlots());

    }

}
