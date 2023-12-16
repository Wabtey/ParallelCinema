package cinema;

import java.util.Optional;
import java.util.logging.Logger;

import tools.Pair;

/**
 * To display/represent the seats, I mostly use the code of `Aircraft` in the
 * lab sessions ["AirSimulation"](https://github.com/Wabtey/AirSimulation).
 */
public class Room {
    enum RoomState {
        CLOSED,
        OPEN,
        // INCLUDING THE CREDITS
        PROJECTING,
        EXITING,
        CLEANING
    }

    int index;

    private int nbSeats;
    private int nRows;
    private int nSeatsPerRow;
    private int nAisles;
    private int[] aisleSeat;
    private int nFreeSeats;

    private boolean animation;

    private volatile Customer[][] seatMap;
    // Map<Integer, boolean> seats = new HashMap<>();

    RoomState state = RoomState.CLOSED;

    public Room(int index, int nRows, int nSeatsPerRow, int[] aisleSeat, boolean animation) {
        this.animation = animation;
        this.index = index;

        try {
            // TODO: Define specific Exceptions

            // number of rows
            if (nRows < 3)
                throw new Exception("Room: the room needs at least 3 rows!");
            this.nRows = nRows;

            // number of seats per row
            if (nSeatsPerRow < 2)
                throw new Exception("Room: the room needs to have at least 2 seats per row!");
            this.nSeatsPerRow = nSeatsPerRow;

            // number of aisles (from 1 aisle, there are 2 corresponding seats
            if (aisleSeat.length < 2)
                throw new Exception("Room: the room needs to have at least 1 aisle!");
            this.nAisles = aisleSeat.length;

            // location of every aisle
            this.aisleSeat = new int[this.nAisles];
            for (int i = 0; i < this.nAisles; i++) {
                if (aisleSeat[i] == 1 || aisleSeat[i] == nAisles - 1)
                    throw new Exception("Room: aisles cannot be located next to the windows!");
                this.aisleSeat[i] = aisleSeat[i];
            }

            // seat map (null Customer)
            this.seatMap = new Customer[nRows][nSeatsPerRow];
            for (int i = 0; i < nRows; i++) {
                for (int j = 0; j < nSeatsPerRow; j++) {
                    this.seatMap[i][j] = null;
                }
            }

            // initializating counter of free seats
            this.nbSeats = nRows * nSeatsPerRow;
            this.nFreeSeats = this.nbSeats;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Constructor 2 (loads a standard movie theather model)
     */
    public Room(int index, boolean animation) {
        this(index, 32, 6, new int[] { 3, 4 }, animation);
    }

    /* ---------------------------- Customers Methods --------------------------- */

    /**
     * TODO: render - Make the customers choose the centerest seats
     * 
     * @param stander
     * @return
     */
    public synchronized Pair<Integer, Integer> stand(Customer stander) {
        Optional<Pair<Integer, Integer>> potentialFreeSeat = Optional.empty();
        while (potentialFreeSeat.isEmpty()) {

            // we can put them asleep without trouble
            // as they will be waken up as soon the room's state changes
            while (this.getRoomState() != RoomState.OPEN || this.isRoomFull()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Logger.getGlobal()
                            .warning(stander.getName() + " got interruped in their sleep.\n" + e.toString());
                    stander.interrupt();
                }
            }

            // get the first free seat
            for (int row = 0; row < this.seatMap.length; row++) {
                for (int column = 0; column < this.seatMap[row].length; column++) {
                    if (this.seatMap[row][column] == null) {

                        // to simulate the seating, the customer takes their time
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            Logger.getGlobal()
                                    .warning("Customer " + stander.getName() + " sleep Interrupted (seating)!");
                            Thread.currentThread().interrupt();
                        }

                        this.seatMap[row][column] = stander;
                        potentialFreeSeat = Optional.of(new Pair<Integer, Integer>(row, column));
                        nFreeSeats--;
                        break;
                    }
                }
                if (potentialFreeSeat.isPresent())
                    break;
            }

            if (this.animation && potentialFreeSeat.isPresent())
                System.out.println(toString() + cleanString());
        }
        return potentialFreeSeat.get();
    }

    /**
     * TODO: render - represent rubbish dumped by filthy customers
     * 
     * @param customer
     * @param seat
     */
    public synchronized void freeSeat(Customer customer, Pair<Integer, Integer> seat) {
        while (this.getRoomState() != RoomState.EXITING) {
            try {
                wait();
            } catch (InterruptedException e) {
                Logger.getGlobal()
                        .warning(customer.getName() + " got interruped in their sleep.\n" + e.toString());
                customer.interrupt();
            }
        }

        // to simulate the seating, the customer takes their time
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Logger.getGlobal()
                    .warning("Customer " + customer.getName() + " sleep Interrupted (seating)!");
            Thread.currentThread().interrupt();
        }
        this.seatMap[seat.getLeft()][seat.getRight()] = null;
        nFreeSeats++;

        // if last make sure to wake the super worker :)
        if (this.isRoomEmpty())
            notify();

        if (this.animation)
            System.out.println(toString() + cleanString());
    }

    /* ------------------------- Super Employee Methods ------------------------- */

    /**
     * @param superWorker
     */
    public synchronized void clean(SuperWorker superWorker) {
        // as all the customers left, the only one waiting is the super-worker
        if (!this.isRoomEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                Logger.getGlobal()
                        .warning("Super Worker got interruped in their sleep (waiting for cleaing).\n" + e.toString());
                superWorker.interrupt();
            }
        }

        // TODO: render - instead of a simple 1s sleep, explores rows and take a bit of
        // time of each trash to be removed.
    }

    public synchronized void nextRoomState() {
        switch (state) {
            case RoomState.CLOSED:
                this.state = RoomState.OPEN;
                break;
            case RoomState.OPEN:
                this.state = RoomState.PROJECTING;
                break;
            case RoomState.PROJECTING:
                this.state = RoomState.EXITING;
                break;
            case RoomState.EXITING:
                this.state = RoomState.CLEANING;
                break;
            case RoomState.CLEANING:
                this.state = RoomState.OPEN;
                break;
            default:
                break;
        }

        if (this.animation)
            System.out.println(toString() + cleanString());

        notifyAll();
        // this.state = this.state + 1 >= RoomState.values().length ?
        // RoomState.values()[0] : this.state.ordinal + 1;
    }

    /* ---------------------------- Room Own Methods ---------------------------- */

    public RoomState getRoomState() {
        return this.state;
    }

    /**
     * Resetting an empty room
     */
    public void reset() {
        for (int i = 0; i < this.nRows; i++) {
            for (int j = 0; j < this.nSeatsPerRow; j++) {
                this.seatMap[i][j] = null;
            }
        }
        this.nFreeSeats = this.nRows * this.nSeatsPerRow;
    }

    /**
     * Checking whether the room is full
     */
    public boolean isRoomFull() {
        return this.nFreeSeats == 0;
    }

    /**
     * Get the Number of Seats per Row
     */
    public int getSeatsPerRow() {
        return this.nSeatsPerRow;
    }

    /**
     * @return the number of free seats in the room.
     */
    public int getNumberOfFreeSeats() {
        return this.nFreeSeats;
    }

    public boolean isRoomEmpty() {
        return this.nFreeSeats == this.nbSeats;
    }

    /* ------------------------------ Display Room ------------------------------ */

    /**
     * Clean screen
     * (creates a String that make the cursor point at the beginning of the last
     * output from toString)
     */
    public String cleanString() {
        StringBuilder print = new StringBuilder();
        for (int i = 0; i < 4 + this.getSeatsPerRow() + this.nAisles / 2; i++)
            print.append("\033[F");
        print.append("\r");
        return print.toString();
    }

    /** Printing */
    public String toString() {
        StringBuilder print = new StringBuilder();

        // Same length State visualizer (10 being PROJECTING's length)
        StringBuilder stateFormated = new StringBuilder().append(this.getRoomState().toString());
        for (int i = this.getRoomState().toString().length() - 10; i < 10; i++)
            stateFormated.append(" ");

        // Same length # of free seats visualizer
        StringBuilder nFreeSeatsFormated = new StringBuilder();
        if (this.getNumberOfFreeSeats() < 10) {
            nFreeSeatsFormated.append("  ");
        } else if (this.getNumberOfFreeSeats() < 100) {
            nFreeSeatsFormated.append(" ");
        }
        nFreeSeatsFormated.append(this.getNumberOfFreeSeats());

        print.append("Room " + this.index + ": " + stateFormated.toString() + ", "
                + nFreeSeatsFormated + "/" + this.nbSeats + "\n");
        // print.append("Number of free seats: " + this.nFreeSeats + "\n");

        for (int i = 0; i < this.nRows; i++) {
            print.append("--");
        }
        print.append("-\n");

        int aisle = 0;
        for (int j = 0; j < this.nSeatsPerRow; j++) {
            if (aisle < this.nAisles - 1 && (this.aisleSeat[aisle] == j && this.aisleSeat[aisle + 1] == j + 1)) {
                for (int k = 0; k < this.nRows; k++)
                    print.append("--");
                print.append("-\n");
                aisle = aisle + 2;

            }

            for (int i = 0; i < this.nRows; i++) {
                if (this.seatMap[i][j] == null) {
                    print.append(" \033[1mx\033[0m");
                } else {
                    // TODO: render - find what print to represent customers
                    // `this.seatMap[i][j].getFlyerLevel()`
                    print.append(" \033[32;1m" + 0 + "\033[0m");
                }
            }
            print.append("\n");
        }

        for (int i = 0; i < this.nRows; i++) {
            print.append("--");
        }
        print.append("-\n");

        return print.toString();
    }

}
