package task2;

public class MainParking {

    public static void main(String[] args) {
        Parking parking = new Parking(10, 0);
        EnterThread enterThread1 = new EnterThread(parking, 1);
        EnterThread enterThread2 = new EnterThread(parking, 2);
        EnterThread enterThread3 = new EnterThread(parking, 3);
        ExitThread exitThread = new ExitThread(parking);

        enterThread1.start();
        enterThread2.start();
        enterThread3.start();
        exitThread.start();
    }
}
