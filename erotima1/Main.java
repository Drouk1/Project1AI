import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        // Check if no arguments were provided
        if(args.length < 1) {
            // Print an error message indicating the problem
            System.err.println("Error: Please provide the number of family members and their times.");
            // Exit the program with a status code indicating an error
            System.exit(1);
        // Check if the number of provided times (args.length - 1) doesn't match the number of family members specified in args[0]
        } else if(args.length-1 != Integer.parseInt(args[0])) {
            // Print an error message indicating the mismatch
            System.err.println("Error: The amount of family members given is not equal to the times given.");
            // Exit the program with a status code indicating an error
            System.exit(1);
        }

        // Initialize data
        int[] times = new int[args.length - 1];
        for (int i = 0; i < times.length; i++) {
            times[i] = Integer.parseInt(args[i + 1]);
        }

         // Create the initial state (start state) using the given times
        State startState = new State(times);
        // A placeholder for the final state (goal state) once the solution is found
        State endState = null;

        // Use a priority queue for the open list to always pick the next state with the lowest f score
        PriorityQueue<State> openList = new PriorityQueue<>();
        // Use a set for the closed list to track visited states
        Set<State> closedList = new HashSet<>();

        // Add the initial state to the open list to start the search
        openList.add(startState);




        

    }
}
