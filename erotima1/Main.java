import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

        List<State> openList = new ArrayList<>();
        Set<State> closedList = new HashSet<>();

        openList.add(startState);
        

        // Continue the search as long as there are states to explore in the open list
        while (!openList.isEmpty()) {

            // Start by initializing a placeholder for the current state to explore
            State currentState = null;
            
            // Iterate over each state in the open list
            for (State state : openList) {
                // For the first state or if the current state in the iteration has a lower f value 
                // (cost + heuristic) than the previously identified state, assign it as the current state
                if (currentState == null || state.compareTo(currentState) < 0) {
                    currentState = state;
                }
            }

            // Remove the identified state with the lowest f value from the open list since it's about to be explored
            openList.remove(currentState);
            
            // Check if the current state is the goal state
            if(currentState.isFinal()) {
                // If it's the goal state, store it as the end state and break out of the search loop
                endState = currentState;
                break;
            }

            // If it's not the goal state, get all possible next states (children) from the current state
            for(State child : currentState.getChildren()) {
                // For each child state, check if it has not been visited (i.e., not in the closed list) 
                // or is not already in the open list waiting to be explored
                if(!closedList.contains(child) && !openList.contains(child)) {
                    // Set the current state as the parent of the child state. This is useful for retracing 
                    // the path from the start state to the goal state at the end of the search.
                    child.setFather(currentState);
                    // Update the child's g value. This is the total cost of the path from the start state 
                    // to this child state through its parent.
                    child.setG(currentState.getG() + child.getTotalTime());
                    // Calculate the child's f value, which is the sum of g (path cost) and h (heuristic). 
                    // The heuristic value is predetermined in the State class or set to zero if not available.
                    child.evaluate();
                    // Add the child state to the open list, marking it for future exploration
                    openList.add(child);
                }
            }
        }        

    }
}
