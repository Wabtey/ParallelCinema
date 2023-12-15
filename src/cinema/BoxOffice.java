package cinema;

public class BoxOffice {

    private Integer ticketNumber = 350;

    public synchronized boolean bookTicket() {
        if (this.ticketNumber != 0) {
            this.ticketNumber--;
            // System.out.println("ticketNumber: " + this.ticketNumber);
            return true;
        } else {
            System.out.println("rupture");
            return false;
        }
    }
}
