import java.util.Scanner;

public class ATMSimulation {
    private double balance;

    public ATMSimulation(double initialBalance) {
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.printf("Successfully deposited $%.2f%n", amount);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
        } else if (amount > balance) {
            System.out.println("Insufficient balance. Withdrawal failed.");
        } else {
            balance -= amount;
            System.out.printf("Successfully withdrew $%.2f%n", amount);
        }
    }

    public void checkBalance() {
        System.out.printf("Your current balance is: $%.2f%n", balance);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATMSimulation atm = new ATMSimulation(1000.00); // initial balance
        
        System.out.println("Welcome to the ATM Simulation System!");
        boolean exit = false;

        while (!exit) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1 - Withdraw");
            System.out.println("2 - Deposit");
            System.out.println("3 - Check Balance");
            System.out.println("4 - Exit");

            System.out.print("Enter choice: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    System.out.print("Enter amount to withdraw: ");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        atm.withdraw(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a numeric value.");
                    }
                    break;
                case "2":
                    System.out.print("Enter amount to deposit: ");
                    try {
                        double amount = Double.parseDouble(scanner.nextLine());
                        atm.deposit(amount);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a numeric value.");
                    }
                    break;
                case "3":
                    atm.checkBalance();
                    break;
                case "4":
                    exit = true;
                    System.out.println("Thank you for using the ATM Simulation System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
                    break;
            }
        }

        scanner.close();
    }
}

