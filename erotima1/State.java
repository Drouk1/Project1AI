import java.util.*;
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
		this.f = 0;//evaluate() 
        this.h = 0;//heuristic()
        this.g = 0;
        this.father = null;
        this.totalTime = 0;
        this.leftSide = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            this.leftSide.add(i); // At first, everyone is on the left side
        }
        this.rightSide = new ArrayList<>();
        this.torchPosition = true; // torch starts on the left side
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
		System.out.println("Left side: " + leftSide + " Right side: " + rightSide + " Torch on left: " + torchPosition + " Total time: " + totalTime);
	}

	private void move(int i, int j) {
		// If the torch is on the left side
		if(torchPosition) {
			// Remove the person represented by index 'i' from the left side
			leftSide.remove(Integer.valueOf(i));
			
			// Remove the person represented by index 'j' from the left side
			leftSide.remove(Integer.valueOf(j));
			
			// Add the person represented by index 'i' to the right side
			rightSide.add(i);
			
			// Add the person represented by index 'j' to the right side
			rightSide.add(j);
			
			// Increase the totalTime by the time taken by the slower person
			// because both walk together at the speed of the slower person
			totalTime += Math.max(times[i], times[j]);
		} 
		// If the torch is on the right side
		else {
			// Remove the person represented by index 'i' from the right side
			rightSide.remove(Integer.valueOf(i));
			
			// Remove the person represented by index 'j' from the right side
			rightSide.remove(Integer.valueOf(j));
			
			// Add the person represented by index 'i' to the left side
			leftSide.add(i);
			
			// Add the person represented by index 'j' to the left side
			leftSide.add(j);
			
			// Increase the totalTime by the time taken by the slower person
			totalTime += Math.max(times[i], times[j]);
		}
		
		// Toggle the torch's position
		torchPosition = !torchPosition;
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
	
	
	public boolean isFinal() {return true;}
	
	@Override
	public boolean equals(Object obj) {return true;}
	
	@Override
    public int hashCode() {return 0;}
	
	@Override
    public int compareTo(State s)
    {
        return Double.compare(this.f, s.getF()); // compare based on the heuristic score.
    }
}