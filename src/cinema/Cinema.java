package cinema;

import java.util.Random;
import java.util.logging.Logger;

public class Cinema {

    public static final float AVERAGE_SALARY = 2000;

    private BoxOffice boxOffice;
    private Room[] rooms;
    private Customer[] customers;
    private SuperWorker superWorker;

    Cinema(int nbRooms, int nbTickets, int nbCustomers, boolean animation) {
        this.rooms = new Room[nbRooms];
        this.boxOffice = new BoxOffice(nbTickets);
        this.customers = new Customer[nbCustomers];

        // --- Rooms ---
        for (int i = 0; i < nbRooms; i++)
            rooms[i] = new Room(i, animation);

        // --- Customers ---
        Random r = new Random();
        for (int i = 0; i < nbCustomers; i++) {
            // NOTE: we could put them the film idea and and the superWorker tells them
            // where to go
            int roomChoosen = r.nextInt(nbRooms);
            customers[i] = new Customer(i, boxOffice, rooms[roomChoosen]);
            customers[i].start();
        }

        // --- Our super Worker ---
        superWorker = new SuperWorker(rooms);
        superWorker.setDaemon(true);
        superWorker.start();

        for (int i = 0; i < nbCustomers; i++) {
            // eww... this loop will freeze on the first iteration
            try {
                customers[i].join();
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Thread Customer " + i + " interrupted");
                /* Clean up whatever needs to be handled before interrupting */
                customers[i].interrupt();
            }
        }

        for (int i = 0; i < nbRooms; i++)
            System.out.println(this.rooms[i].toString());

        long unhappyCustomers = nbCustomers < nbTickets ? 0 : nbCustomers - nbTickets;
        System.out.println("Tickets left: " + boxOffice.getTicketNumber() + "/" + nbTickets
                + ". Unhappy customers: " + unhappyCustomers);

        // There is no more customers at the cinema.
        // The employee being a Daemon, will stop itself being the only one left.
        System.out.println("The employee is calling it a day");

        // By respect, we augment the daemon's salary.
        superWorker.modifySalaryByPercent(7.5f);
    }

    public static void main(String[] args) {

        boolean animation = args != null && args.length > 0 && args[0] != null && args[0].equals("animation");
        long start = System.currentTimeMillis();

        new Cinema(1, 350, 400, animation);

        long finish = System.currentTimeMillis();
        float timeElapsed = finish - start;
        System.out.println("Day completed in " + timeElapsed / 1000 + "s");
    }
}
