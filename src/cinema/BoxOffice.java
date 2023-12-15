package cinema;

public class BoxOffice {

    private int ticketsTotal;
    private Integer ticketNumber;

    public BoxOffice(int ticketsTotal) {
        this.ticketsTotal = ticketsTotal;
        this.ticketNumber = this.ticketsTotal;
    }

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
