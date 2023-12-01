package cinema;

import java.util.HashMap;
import java.util.Map;

public class Room {
    enum RoomState {
        CLOSED,
        OPEN,
        // INCLUDING THE CREDITS
        PROJECTING,
        EXITING,
        CLEANING
    }

    static final int NB_SEATS = 150;

    int index;
    Map<Integer, Boolean> seats = new HashMap<>();

    RoomState state = RoomState.CLOSED;

    public Room(int index) {
        this.index = index;
        for (int i = 0; i < NB_SEATS; i++)
            seats.put(i, true);
    }

    public RoomState getRoomState() {
        return this.state;
    }

    public void nextRoomState() {
        switch (state) {
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
        // this.state = this.state + 1 >= RoomState.values().length ?
        // RoomState.values()[0] : this.state.ordinal + 1;
    }

    public String toString() {
        return "";
    }
}
