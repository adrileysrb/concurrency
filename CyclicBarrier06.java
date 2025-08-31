import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/*
 * Quando é necessário uma determinada operação quando um certo número de threads
 * alcança uma determinada etapa, é utilizado CyclicBarrier
 */
public class CyclicBarrier06 {

    static class Task implements Runnable {

        private CyclicBarrier cyclicBarrier;

        public Task(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting");
                cyclicBarrier.await();
                System.out.println(Thread.currentThread().getName() + " is released");
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        Runnable cyclicBarrierTask = () -> {
            System.out.println("All previous tasks are completed");
        };

        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, cyclicBarrierTask);
        
        Thread t1 = new Thread(new Task(cyclicBarrier), "T1");
        Thread t2 = new Thread(new Task(cyclicBarrier), "T2");
        Thread t3 = new Thread(new Task(cyclicBarrier), "T3");

        if(!cyclicBarrier.isBroken()) {
            t1.start();
            t2.start();
            t3.start();
        }

    }
}
