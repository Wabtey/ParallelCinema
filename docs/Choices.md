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

## Room's life

I thought like giving life to the room, which will switch state itself but for now we give fullpower to the `SuperWorker`.

I put `nextRoomState()` on `synchronized` to ensure that everyone has the good room's state.

Since [this commit](3e97d0b2bb07d47bca609b6d2b8a4029dbc7ed63) the numbering of Free seats was off.
