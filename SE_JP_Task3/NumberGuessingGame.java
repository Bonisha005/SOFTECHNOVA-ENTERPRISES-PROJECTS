import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Random rand = new Random();
        int targetNumber = rand.nextInt(100) + 1; // Random number between 1 and 100
        Scanner scanner = new Scanner(System.in);
        int guess = 0;
        int attempts = 0;

        System.out.println("Welcome to the Number Guessing Game!");
        System.out.println("I have chosen a number between 1 and 100. Try to guess it!");

        while (guess != targetNumber) {
            System.out.print("Enter your guess: ");
            if (scanner.hasNextInt()) {
                guess = scanner.nextInt();
                attempts++;

                if (guess < 1 || guess > 100) {
                    System.out.println("Your guess is out of bounds! Please guess between 1 and 100.");
                    continue;
                }

                if (guess < targetNumber) {
                    System.out.println("Too low. Try again.");
                } else if (guess > targetNumber) {
                    System.out.println("Too high. Try again.");
                } else {
                    System.out.println("Congratulations! You guessed the number in " + attempts + " attempts.");
                    break;
                }

                if (attempts == 5) {
                    System.out.println("Hint: The secret number is " + targetNumber);
                }

            } else {
                String invalidInput = scanner.next();
                System.out.println("Invalid input: '" + invalidInput + "'. Please enter a valid integer between 1 and 100.");
            }
        }

        scanner.close();
    }
}

