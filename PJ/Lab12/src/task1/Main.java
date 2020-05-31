package task1;

public class Main {
    public static void main(String[] args) {
        BankAccount bankAccount = new BankAccount(0);
        DepositThread depositThread = new DepositThread(bankAccount);
        WithdrawThread withdrawThread = new WithdrawThread(bankAccount);

        depositThread.start();
        withdrawThread.start();
    }
}