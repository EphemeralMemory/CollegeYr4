import java.util.*;

public class VacuumWorld {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Enter initial state:");

        // Validate room inputs
        boolean roomA = getBooleanInput("Is Room A dirty? (T/F/t/f/true/false/yes/no): ");
        boolean roomB = getBooleanInput("Is Room B dirty? (T/F/t/f/true/false/yes/no): ");

        // Validate vacuum position input
        int vacuumPosition = getVacuumPositionInput();

        scanner.close();

        boolean[] rooms = {roomA, roomB}; // Initial room states
        State initialState = new State(vacuumPosition, rooms, null, "Start");

        System.out.println("\nBFS Solution:");
        search(initialState, true);
        System.out.println("\nDFS Solution:");
        search(initialState, false);
    }

    // Method to get valid boolean input
    public static boolean getBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.next().trim().toLowerCase();
            if (input.equals("true") || input.equals("T") ||input.equals("t") || input.equals("yes")) {
                return true;
            } else if (input.equals("false") || input.equals("F") || input.equals("f") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Invalid input! Please enter true, false, yes, or no.");
            }
        }
    }

    // Method to get a valid integer (0 or 1) for vacuum position
    public static int getVacuumPositionInput() {
        while (true) {
            System.out.print("Vacuum position (0 for A, 1 for B): ");
            try {
                int position = scanner.nextInt();
                if (position == 0 || position == 1) {
                    return position;
                } else {
                    System.out.println("Invalid input! Please enter 0 for Room A or 1 for Room B.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number (0 or 1).");
                scanner.next(); // Clear invalid input
            }
        }
    }

    public static void search(State initialState, boolean useBFS) {
        Queue<State> frontier = useBFS ? new LinkedList<>() : new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        frontier.add(initialState);

        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            if (current.isGoal()) {
                printSolution(current);
                return;
            }

            visited.add(current.toString());
            for (State successor : current.getSuccessors()) {
                if (!visited.contains(successor.toString())) {
                    frontier.add(successor);
                }
            }
        }
        System.out.println("No solution found.");
    }

    public static void printSolution(State state) {
        List<String> path = new ArrayList<>();
        while (state != null) {
            path.add(state.action + " -> " + state.toString());
            state = state.parent;
        }
        Collections.reverse(path);
        for (String step : path) {
            System.out.println(step);
        }
    }
}

class State {
    int vacuumPosition; // 0 for Room A, 1 for Room B
    boolean[] rooms; // true if dirty, false if clean
    State parent;
    String action;

    public State(int vacuumPosition, boolean[] rooms, State parent, String action) {
        this.vacuumPosition = vacuumPosition;
        this.rooms = Arrays.copyOf(rooms, rooms.length); // Properly copy room states
        this.parent = parent;
        this.action = action;
    }

    public boolean isGoal() {
        return !rooms[0] && !rooms[1]; // Goal: Both rooms must be clean
    }

    public List<State> getSuccessors() {
        List<State> successors = new ArrayList<>();

        // Move left or right
        successors.add(new State(1 - vacuumPosition, rooms, this, vacuumPosition == 0 ? "Move Right" : "Move Left"));

        // Clean current room if dirty
        if (rooms[vacuumPosition]) {
            boolean[] newRooms = Arrays.copyOf(rooms, rooms.length); // Copy state properly
            newRooms[vacuumPosition] = false;
            successors.add(new State(vacuumPosition, newRooms, this, "Suck Dirt"));
        }

        return successors;
    }

    public String toString() {
        return "Vacuum Position: " + (vacuumPosition == 0 ? "A" : "B") +
               ", Room A: " + (rooms[0] ? "Dirty" : "Clean") +
               ", Room B: " + (rooms[1] ? "Dirty" : "Clean");
    }
}