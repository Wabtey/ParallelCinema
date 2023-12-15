package cinema;

public class BoxOffice {

    static final int MAX_TICKETS = 350;
    private Integer ticketNumber = MAX_TICKETS;

    public synchronized boolean bookTicket() {
        if (this.ticketNumber != 0) {
            this.ticketNumber--;
            // System.out.println("ticketNumber: " + this.ticketNumber);
            return true;
        } else {
            // System.out.println("rupture");
            return false;
        }
    }

    public int getTicketNumber() {
        return this.ticketNumber;
    }
}
