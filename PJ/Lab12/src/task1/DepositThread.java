package task1;

public class DepositThread extends Thread {
    private BankAccount bankAccount;

    public DepositThread(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        int amount = 0;
        int balance = 0;
        for (int i = 0; i < 5; i++) {
            amount = (int) (Math.random() * 1000);
            bankAccount.deposit(amount, i);
            try {
                sleep((int) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}