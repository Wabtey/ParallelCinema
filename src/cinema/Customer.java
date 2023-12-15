package cinema;

import java.util.Optional;

import tools.Pair;

public class Customer extends Thread {

    BoxOffice boxOffice;
    Room room;
    Boolean hasTicket;
    Optional<Pair<Integer, Integer>> potentialSeat = Optional.empty();
    Boolean movieSeen = false;

    public Customer(BoxOffice boxOffice, Room room) {
        this.boxOffice = boxOffice;
        this.room = room;
    }

    @Override
    public void run() {
        this.hasTicket = this.boxOffice.bookTicket();

        // if (Boolean.FALSE.equals(this.hasTicket)) {
        // // If no ticket get back home
        // this.interrupt();
        // // return;
        // }

        if (Boolean.TRUE.equals(this.hasTicket)) {
            /* ------------------------ Waiting the room to open ------------------------ */

            this.potentialSeat = Optional.of(this.room.stand(this));

            /* ------------------------ Waiting the flim to start ----------------------- */
            /* -------------------- Waiting the flim to finish Sadge -------------------- */

            this.room.freeSeat(this, potentialSeat.get());
            this.potentialSeat = Optional.empty();
            this.movieSeen = true;

        }

        // go back home.
    }
}