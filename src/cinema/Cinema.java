package cinema;

import java.util.logging.Logger;

public class Cinema {

    public static final float AVERAGE_SALARY = 2000;

    public static final int NB_ROOMS = 5;
    static final int NB_CUSTOMERS = 50;

    private Room[] rooms = new Room[NB_ROOMS];
    private Customer[] customers = new Customer[NB_CUSTOMERS];
    private SuperWorker superWorker;

    Cinema() {

        // --- Rooms ---
        for (int i = 0; i < NB_ROOMS; i++)
            rooms[i] = new Room(i);

        // --- Customers ---
        for (int i = 0; i < NB_CUSTOMERS; i++) {
            customers[i] = new Customer();
            customers[i].start();
        }

        // --- Our super Worker ---
        superWorker = new SuperWorker();
        superWorker.setDaemon(true);
        superWorker.start();

        for (int i = 0; i < NB_CUSTOMERS; i++) {
            // eww... this loop will freeze on the first iteration
            try {
                customers[i].join();
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Thread Customer " + i + " interrupted");
                /* Clean up whatever needs to be handled before interrupting */
                customers[i].interrupt();
            }
        }

        // There is no more customers at the cinema.
        // The employee being a Daemon, will stop itself being the only one left.
        System.out.println("The employee is calling it a day");

        // By respect, we augment the daemon's salary.
        superWorker.modifySalaryByPercent(7.5f);
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        new Cinema();

        long finish = System.currentTimeMillis();
        float timeElapsed = finish - start;
        System.out.println("Day completed in " + timeElapsed / 1000 + "s");
    }
}
