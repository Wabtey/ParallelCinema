package cinema;

import tools.Pair;

public class Customer extends Thread {

    private BoxOffice boxOffice;
    private Room room;
    // private boolean movieSeen = false;

    public Customer(BoxOffice boxOffice, Room room) {
        this.boxOffice = boxOffice;
        this.room = room;
    }

    @Override
    public void run() {
        Pair<Integer, Integer> seat;
        boolean hasTicket = this.boxOffice.bookTicket();

        // if (boolean.FALSE.equals(this.hasTicket)) {
        // // If no ticket get back home
        // this.interrupt();
        // // return;
        // }

        if (hasTicket) {
            /* ------------------------ Waiting the room to open ------------------------ */

            seat = this.room.stand(this);

            /* ------------------------ Waiting the flim to start ----------------------- */
            /* -------------------- Waiting the flim to finish Sadge -------------------- */

            this.room.freeSeat(this, seat);
            // this.movieSeen = true;

        }

        // go back home.
    }
}