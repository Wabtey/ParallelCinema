package cinema;

import java.util.logging.Logger;

public class SuperWorker extends Thread {

    private float salary = Cinema.AVERAGE_SALARY;

    private Room[] rooms;

    public SuperWorker(Room[] rooms) {
        this.rooms = rooms;
    }

    @Override
    public void run() {
        // REFACTOR: multitasking - super worker need to be able to work on severals
        // rooms
        while (true) {
            /* ------------------------------ Open the Room ----------------------------- */
            this.rooms[0].nextRoomState();

            // while (!this.rooms[0].isRoomFull()) {
            // }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Super Worker's waiting Interrupted (open)!");
                /* Clean up whatever needs to be handled before interrupting */
                Thread.currentThread().interrupt();
            }

            /* ----------------------------- Start the flim ----------------------------- */
            this.rooms[0].nextRoomState();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Super Worker's screening Interrupted (projecting)!");
                /* Clean up whatever needs to be handled before interrupting */
                Thread.currentThread().interrupt();
            }

            /* ------------------------------ Exiting Phase ----------------------------- */
            this.rooms[0].nextRoomState();

            this.rooms[0].clean(this);

            /* ----------------------------- Cleaning Phase ----------------------------- */
            this.rooms[0].nextRoomState();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Super Worker's cleaning Interrupted (cleaning)!");
                /* Clean up whatever needs to be handled before interrupting */
                Thread.currentThread().interrupt();
            }
        }
    }

    public void modifySalaryByPercent(float percent) {
        this.salary = salary + salary * percent;
    }
}
