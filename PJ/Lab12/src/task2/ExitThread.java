package task2;

public class ExitThread extends Thread {
    private Parking parking;

    public ExitThread(Parking parking) {
        this.parking = parking;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                parking.exit(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}