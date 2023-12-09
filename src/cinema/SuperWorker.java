package cinema;

import java.util.logging.Logger;

public class SuperWorker extends Thread {

    float salary = Cinema.AVERAGE_SALARY;

    Room[] rooms;

    public SuperWorker(Room[] rooms) {
        this.rooms = rooms;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Logger.getGlobal().warning("Truck Sleep Interrupted!");
                /* Clean up whatever needs to be handled before interrupting */
                Thread.currentThread().interrupt();
            }
        }
    }

    public void modifySalaryByPercent(float percent) {
        this.salary = salary + salary * percent;
    }
}
