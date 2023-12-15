# Docs

First I wanted to represent the customers path like that

```java
@Override
public void run() {
    while (Boolean.TRUE.equals(movieSeen)) {
        /* ------------------------ Waiting the room to open ------------------------ */
        while (room.getRoomState() != RoomState.OPEN) {
        }

        if (this.potentialSeat.isEmpty() && room.getRoomState() == RoomState.OPEN)
            this.potentialSeat = this.room.stand(this);
        /* ------------------------ Waiting the flim to start ----------------------- */
        while (room.getRoomState() != RoomState.PROJECTING) {
        }

        if (room.getRoomState() == RoomState.PROJECTING) {
            // Appreciate the flim
        }

        /* -------------------- Waiting the flim to finish Sadge -------------------- */
        while (room.getRoomState() != RoomState.EXITING) {
        }

        if (this.potentialSeat.isPresent() && room.getRoomState() == RoomState.EXITING) {
            this.room.freeSeat(potentialSeat.get());
            this.potentialSeat = Optional.empty();
            this.movieSeen = true;
        }
    }
}
```
