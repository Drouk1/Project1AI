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
		this.f = 0;
        this.h = 0;
        this.g = 0;
        this.father = null;
        this.totalTime = 0;
        this.leftSide = new ArrayList<>();
        for (int i = 0; i < times.length; i++) {
            this.leftSide.add(i); // initially, everyone is on the left side
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
	
	public ArrayList<State> getChildren() {return null;}
	
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