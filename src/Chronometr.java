import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

/**
 * Created by pavel on 09.06.17.
 */
public class Chronometr {

    private final static long startTime = System.currentTimeMillis();
    private volatile long timeInSec;
    private Object monitor = new Object();

    Thread timer = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (monitor) {
                    timeInSec++;
                    monitor.notifyAll();
                }
            }
        }
    });

    public static void showTimeWithBase(int base, long sec) {
        if (sec % base == 0) {
            System.out.printf("Every %d sec. Time in sec: %d\n", base, sec);
        }
    }

    Thread show1sec = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showTimeWithBase(1, timeInSec);
                }
            }
        }
    });

    Thread show5sec = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showTimeWithBase(5, timeInSec);
                }
            }
        }
    });


    Thread show7sec = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                synchronized (monitor) {
                    try {
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    showTimeWithBase(7, timeInSec);
                }
            }
        }
    });

    public static void main(String[] args) {
        Chronometr chronometr = new Chronometr();
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(chronometr.timer);
        executor.execute(chronometr.show1sec);
        executor.execute(chronometr.show5sec);
        executor.execute(chronometr.show7sec);
        executor.shutdown();
    }

}
