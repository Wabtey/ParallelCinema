package cinema;

public class Customer extends Thread {

    Room room;

    public Customer(Room room) {
        this.room = room;
    }

    @Override
    public void run() {
        this.room.stand();
    }
}