package cinema;

import java.util.Optional;

import cinema.Room.RoomState;
import tools.Pair;

public class Customer extends Thread {

    Room room;
    Optional<Pair<Integer, Integer>> potentialSeat;
    Boolean movieSeen = false;

    public Customer(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        while (Boolean.TRUE.equals(movieSeen)) {
            while (this.potentialSeat.isEmpty() && room.getRoomState() == RoomState.OPEN)
                this.potentialSeat = this.room.stand(this);
            while (room.getRoomState() == RoomState.PROJECTING) {
            }
            while (this.potentialSeat.isPresent() && room.getRoomState() == RoomState.EXITING) {
                this.room.freeSeat(this);
                this.potentialSeat = Optional.empty();
                this.movieSeen = true;
            }
            while (room.getRoomState() == RoomState.CLEANING) {
            }
        }
    }
}