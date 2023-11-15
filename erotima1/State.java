import java.util.*;
import java.util.stream.Collectors;
public class State implements Comparable<State>
{
	private int f, h, g;
	private State father;
	private int totalTime;
    private List<Integer> leftSide; // stores indices of people on the left side
    private List<Integer> rightSide; // stores indices of people on the right side
    private boolean torchPosition; // true if on the left side, false otherwise
    private int[] times; // times required for each person to cross

	
	//constructor - fill with arguments if necessary
	public State(int[] times) 
	{
		this.f = 0; 
        this.h = 0;
        this.g = 0;
        this.father = null;
        this.totalTime = 0;
		this.leftSide = new ArrayList<>();
		this.rightSide = new ArrayList<>();
		for (int i = 0; i < times.length; i++) {
			this.rightSide.add(i); // initially, everyone is on the right side
		}
		
        this.torchPosition = false; // torch starts on the right side
        this.times = times;
	}
	
	// copy constructor
	public State(State s)
	{
		this.f = s.f;
        this.g = s.g;
        this.h = s.h;
        this.father = s.father; 
        this.totalTime = s.totalTime;
        this.leftSide = new ArrayList<>(s.leftSide);
        this.rightSide = new ArrayList<>(s.rightSide);
        this.torchPosition = s.torchPosition;
        this.times = s.times; 
    }
	
	public int getF() 
	{
		return this.f;
	}
	
	public int getG() 
	{
		return this.g;
	}
	
	public int getH() 
	{
		return this.h;
	}
	
	public State getFather()
	{
		return this.father;
	}
	
	public void setF(int f)
	{
		this.f = f;
	}
	
	public void setG(int g)
	{
		this.g = g;
	}
	
	public void setH(int h)
	{
		this.h = h;
	}
	
	public void setFather(State f)
	{
		this.father = f;
	}
	
	public int getTotalTime() 
	{
		return this.totalTime;
	}
	
	public void setTotalTime(int time)
	{
		this.totalTime = time;
	}
	
	// Definition of the heuristic function.
	private int heuristic() {
		// Initialize the maximum time to 0.
		int maxTime = 0;
		// Iterate through all family members who are currently on the right side of the bridge.
		for (int index : rightSide) {
			// Check if the current family member's time to cross the bridge is greater than the maxTime recorded so far.
			if (times[index] > maxTime) {
				maxTime = times[index];
			}
		}

		// Return the maximum time found.
		return maxTime;
	}


	public void evaluate() 
	{
		this.h = heuristic();
        this.f = this.g + this.h;
	}
	
	
	public void print() {
		// Initialize strings to hold the representations of the left and right sides.
		String left = "Left side: ";
		String right = "Right side: ";
	
		// Append the times for each side to their respective strings.
		for (int index : leftSide) {
			left += times[index] + ", ";
		}
		for (int index : rightSide) {
			right += times[index] + ", ";
		}
	
		// Remove the last comma and space for a cleaner look.
		if (!leftSide.isEmpty()) {
			left = left.substring(0, left.length() - 2);
		}
		if (!rightSide.isEmpty()) {
			right = right.substring(0, right.length() - 2);
		}
	
		// Determine the position of the torch.
		String torchPositionStr = "Torch on " + (torchPosition ? "left" : "right");
		
		// Build the string for total time.
		String totalTimeStr = "Total time: " + totalTime;
	
		// Print the final state representation.
		System.out.println(right + " | " + left + " | " + torchPositionStr + " | " + totalTimeStr);
	}

	private void move(int i, int j) {
		// Variable to store the time taken for the current move.
		int moveTime;
	
		// Check if the torch is currently on the left side.
		if(torchPosition) {
			// Remove the first person from the left side.
			leftSide.remove(Integer.valueOf(i));
	
			// If a second distinct person is also moving, remove them from the left side.
			if(i != j) {
				leftSide.remove(Integer.valueOf(j));
			}
	
			// Add the first person to the right side.
			rightSide.add(i);
	
			// If a second distinct person is also moving, add them to the right side.
			if(i != j) {
				rightSide.add(j);
			}	
	
			// Calculate the time taken for this move. 
			// If only one person is moving, it's their time. If two people are moving, it's the maximum of their times.
			moveTime = (i == j) ? times[i] : Math.max(times[i], times[j]);
		} else {  // If the torch is currently on the right side.
			// Remove the first person from the right side.
			rightSide.remove(Integer.valueOf(i));
	
			// If a second distinct person is also moving, remove them from the right side.
			if(i != j) {
				rightSide.remove(Integer.valueOf(j));
			}
	
			// Add the first person to the left side.
			leftSide.add(i);
	
			// If a second distinct person is also moving, add them to the left side.
			if(i != j) {
				leftSide.add(j);
			}
	
			// Calculate the time taken for this move. 
			// Similar logic as above.
			moveTime = (i == j) ? times[i] : Math.max(times[i], times[j]);
		}
	
		// Update the total time with the time of the current move.
		totalTime += moveTime;
	
		// Toggle the torch's position.
		torchPosition = !torchPosition;
	
		// Sort the lists of people on both sides. This ensures consistent order which is helpful
		// for operations like equals and hashCode checks.
		Collections.sort(leftSide);
		Collections.sort(rightSide);
	}

	public ArrayList<State> getChildren() {
		// Initialize an empty list to store child states
		ArrayList<State> children = new ArrayList<>();
		List<Integer> fromSide;
		List<Integer> toSide;
		if (torchPosition) {  // if torch is on the left side
			fromSide = leftSide;
			toSide = rightSide;
		} else {  // if torch is on the right side
			fromSide = rightSide;
			toSide = leftSide;
		}
		

		
		// Double loop to generate combinations of either one or two people
		// who will move across the bridge.
		for(int i = 0; i < fromSide.size(); i++) {
			for(int j = i; j < fromSide.size(); j++) {  // Notice that j starts from i.
				
				// Create a new state as a copy of the current state
				State child = new State(this);
				
				// Move one or two persons (represented by their indices) across the bridge.
				// If i is equal to j, it means only one person is moving.
				child.move(fromSide.get(i), fromSide.get(j));
				
				// Add this new state to the list of children.
				children.add(child);
			}
		}
		// Return the list of all possible child states.
		return children;
	}
	
	// Determines if the state represents the final configuration.
	// The goal is to have all individuals on the left side (rightSide is empty) and the torch also on the left.
	public boolean isFinal() {
		return rightSide.isEmpty() && torchPosition ;
	}

	// Overrides the default equals method to check equality of two State objects.
	// A state is considered equal to another if they have the same torch position and the same individuals on each side.
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		State state = (State) obj;
		return torchPosition == state.torchPosition &&
				Objects.equals(leftSide, state.leftSide) &&
				Objects.equals(rightSide, state.rightSide);
	}

	// Overrides the default hashCode method to provide a unique hash for each state based on its attributes.
	@Override
	public int hashCode() {
		return Objects.hash(leftSide, rightSide, torchPosition);
	}

	// Compares two states based on their heuristic scores (f).
	// Useful for sorting states or inserting into a priority structure.
	@Override
	public int compareTo(State s) {
		return Double.compare(this.f, s.getF()); // Compare based on the heuristic score (f).
	}
	
}
