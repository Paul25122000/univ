package task2;

public class Parking {

    private final int spots;
    private int spotsOccupied;
    private Boolean resourceAvailable = false;

    public Parking(int spots, int spotsOccupied) {
        this.spots = spots;
        this.spotsOccupied = spotsOccupied;
    }

    public synchronized void exit(int exit) throws InterruptedException {
        if (!resourceAvailable) {
            wait();
        }
        if(this.spotsOccupied > 0) {
            this.spotsOccupied--;
            System.out.printf("- A car got out, \t\t\t %4d spots are occupied%n",
                    exit, this.spotsOccupied);
        }
        resourceAvailable = false;
        notify();
    }

    public synchronized void enter(int entrance) throws InterruptedException {
        if (resourceAvailable) {
            wait();
        }
        if(this.spotsOccupied < spots) {
            this.spotsOccupied++;
            System.out.printf("+ A car got in via entrance %d, %2d spots are occupied%n",
                    entrance, this.spotsOccupied);
        }
        resourceAvailable = true;
        notify();
    }
}