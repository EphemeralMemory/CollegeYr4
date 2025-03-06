import java.util.*;

public class test {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        // Values
        int money;
        int totalTime;
        int timePerPlay = 10; // Each play takes 10 seconds
        int[] prizeValues = {0, 1, 5, 10, 50}; // Reward values for the machines
        int[] chancesA = new int[5]; // Winning chances for Machine A
        int[] chancesB = new int[5]; // Winning chances for Machine B

        // User input for initial required values
        System.out.print("Enter starting money: ");
        money = scanner.nextInt();

        System.out.print("Enter total play time (seconds): ");
        totalTime = scanner.nextInt();

        System.out.println("Define winning chances for Machine A (percentages for 0, 1, 5, 10, 50):");
        inputWinningChances(scanner, chancesA, prizeValues);

        System.out.println("Define winning chances for Machine B (percentages for 0, 1, 5, 10, 50):");
        inputWinningChances(scanner, chancesB, prizeValues);

        int maxPlays = totalTime / timePerPlay;


        //below are for the simulation creates a list to store the history of each machine

        List<Integer> machineAHistory = new ArrayList<>();
        List<Integer> machineBHistory = new ArrayList<>();

        // Main simulation loop
        for (int play = 1; play <= maxPlays && money > 0; play++) {
            // how it picks which machine to choose
            int chosenMachine = chooseMachine(machineAHistory, machineBHistory);
            System.out.println("Play #" + play + ": Playing Machine " + (chosenMachine == 1 ? "A" : "B"));

            // Simulate pay-off based on chances
            int payOff = simulatePayOff(chosenMachine == 1 ? chancesA : chancesB, prizeValues, random);

            // Update machine history and money of the user
            if (chosenMachine == 1) {
                machineAHistory.add(payOff);
            } else {
                machineBHistory.add(payOff);
            }

            money = money - 1 + payOff; //plays aren't free so deduct
            System.out.println("Pay-off: Php " + payOff);
            System.out.println("Remaining money: Php " + money);

            // Stop plays if money is dead
            if (money <= 0) {
                System.out.println("No more money to play.");
                break;
            }
        }

        // after each end this is result of the simulation
        System.out.println("Simulation ended. Final money: Php " + money);
        System.out.println("Machine A History: " + machineAHistory);
        System.out.println("Machine B History: " + machineBHistory);
        scanner.close();
    }

    // Method to input winning chances for a machine
    public static void inputWinningChances(Scanner scanner, int[] chances, int[] prizeValues) {
        int totalChance;
        do {
            totalChance = 0;
            for (int i = 0; i < prizeValues.length; i++) {
                System.out.print("Percent chance for " + prizeValues[i] + " reward: ");
                chances[i] = scanner.nextInt();
                totalChance += chances[i];
            }
            if (totalChance != 100) {
                System.out.println("The total chances must add up to 100%. Your total was " + totalChance + ". Try again.");
            }
        } while (totalChance != 100);
    }

    // Choose a machine based on past performance
    public static int chooseMachine(List<Integer> machineAHistory, List<Integer> machineBHistory) {
        double avgA = machineAHistory.isEmpty() ? 0 : machineAHistory.stream().mapToInt(Integer::intValue).average().orElse(0);
        double avgB = machineBHistory.isEmpty() ? 0 : machineBHistory.stream().mapToInt(Integer::intValue).average().orElse(0);

        if (avgA > avgB) {
            return 1; // Choose Machine A
        } else if (avgB > avgA) {
            return 2; // Choose Machine B
        } else {
            return Math.random() < 0.5 ? 1 : 2; // Random choice if averages are equal gotta be human
        }
    }

    // Simulate pay-off on the machine chances
    public static int simulatePayOff(int[] chances, int[] prizeValues, Random random) {
        int roll = random.nextInt(100); // Random roll between 0 and 99
        int cumulativeChance = 0;

        for (int i = 0; i < chances.length; i++) {
            cumulativeChance += chances[i];
            if (roll < cumulativeChance) {
                return prizeValues[i]; // Return prize based on the chance range
            }
        }
        //default return value if no prize is won
        return 0; 
    }
}
