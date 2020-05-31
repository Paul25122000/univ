package task2;

public class EnterThread extends Thread {
    private Parking parking;
    private final int entrance;

    public EnterThread(Parking parking, int entrance) {
        this.entrance = entrance;
        this.parking = parking;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                parking.enter(entrance);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}