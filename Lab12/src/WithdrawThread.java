public class WithdrawThread extends Thread {
    private BankAccount bankAccount;

    public WithdrawThread(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        int amount = 0;
        for (int i = 0; i < 5; i++) {
            amount = (int) (Math.random() * 1000);
            bankAccount.withdraw(amount, i);
        }
    }
}