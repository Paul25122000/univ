public class BankAccount {

    private int amount;
    private Boolean available = false;
    private static final int limit = 1000;

    public BankAccount(int amount) {
        this.amount = amount;
    }

    public synchronized int withdraw(int amount, int transactionNumber) {
        if (!available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(this.amount >= amount) {
            this.amount -= amount;
            System.out.printf("- Transaction %d: %4dRON has been withdrawn, %4dRON left%n",
                    transactionNumber, amount, this.amount);
        }
        available = false;
        notify();
        return this.amount;
    }

    public synchronized void deposit(int amount, int transactionNumber) {
        if (available) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(this.amount + amount <= limit) {
            this.amount += amount;
        }
        available = true;
        System.out.printf("+ Transaction %d: %4dRON has been deposited, %4dRON left%n",
                transactionNumber, amount, this.amount);
        notify();
    }
}