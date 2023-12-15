package cinema;

public class BoxOffice {

    private Integer ticketNumber = 350;

    public synchronized Boolean bookTicket() {
        if (this.ticketNumber != 0) {
            this.ticketNumber--;
            return true;
        } else {
            return false;
        }
    }
}
