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
	
	private int heuristic() {
        int maxTime = 0;
        for (int index : rightSide) {
            if (times[index] > maxTime) {
                maxTime = times[index];
            }
        }
        return maxTime;
    }

	public void evaluate() 
	{
		this.h = heuristic();
        this.f = this.g + this.h;
	}
	
	public void print() {
		 // The leftSide list represents the destination side.
        String left = "Left side: " + leftSide.stream().map(i -> String.valueOf(times[i])).collect(Collectors.joining(", "));

        // The family starts on the right, so the rightSide list represents the starting side.
        String right = "Right side: " + rightSide.stream().map(i -> String.valueOf(times[i])).collect(Collectors.joining(", "));
       
        // The torch position is represented by the torchPosition boolean; if true, the torch is on the left side.
        String torch = "Torch on " + (torchPosition ? "left" : "right");
        // The time string represents the total time elapsed.
        String time = "Total time: " + totalTime;

        // Print out the state with the family starting on the right and moving to the left.
        System.out.println( left + " | " + right + " | " + torch + " | " + time);
    }

	private void move(int i, int j) {
		int moveTime;
		
		if(torchPosition) {  // If torch is on the left
			leftSide.remove(Integer.valueOf(i));
			if(i != j) {  // If not the same person, move the second person
				leftSide.remove(Integer.valueOf(j));
			}
			rightSide.add(i);
			if(i != j) {
				rightSide.add(j);
			}
			moveTime = (i == j) ? times[i] : Math.max(times[i], times[j]);
		} else {  // If torch is on the right
			rightSide.remove(Integer.valueOf(i));
			if(i != j) {
				rightSide.remove(Integer.valueOf(j));
			}
			leftSide.add(i);
			if(i != j) {
				leftSide.add(j);
			}
			moveTime = (i == j) ? times[i] : Math.max(times[i], times[j]);
		}
	
		totalTime += moveTime;
		torchPosition = !torchPosition;
		
		// Sort the lists to maintain order (for equals and hashCode checks)
		Collections.sort(leftSide);
		Collections.sort(rightSide);
	}
	
	
	
	public ArrayList<State> getChildren() {
		// Initialize an empty list to store child states
		ArrayList<State> children = new ArrayList<>();
		
		// If torchPosition is true (left), fromSide is leftSide, otherwise it's rightSide.
		// This tells us which side of the riverbank we're moving from.
		List<Integer> fromSide = torchPosition ? leftSide : rightSide;
		
		// Similarly, toSide tells us which side we're moving to.
		// If torchPosition is true (left), toSide is rightSide, otherwise it's leftSide.
		List<Integer> toSide = torchPosition ? rightSide : leftSide;
		
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