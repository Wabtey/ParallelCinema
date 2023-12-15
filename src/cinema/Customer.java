package cinema;

import java.util.Optional;

import cinema.Room.RoomState;
import tools.Pair;

public class Customer extends Thread {

    Room room;
    Optional<Pair<Integer, Integer>> potentialSeat = Optional.empty();
    Boolean movieSeen = false;

    public Customer(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        this.room.reserveTicket();
        // If no ticket get back home
        /* ------------------------ Waiting the room to open ------------------------ */

        this.potentialSeat = Optional.of(this.room.stand(this));

        /* ------------------------ Waiting the flim to start ----------------------- */
        /* -------------------- Waiting the flim to finish Sadge -------------------- */

        this.room.freeSeat(this, potentialSeat.get());
        this.potentialSeat = Optional.empty();
        this.movieSeen = true;
    }
}