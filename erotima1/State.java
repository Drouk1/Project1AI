import java.util.*;

public class State implements Comparable<State>
{
	private int f, h, g;
	private State father;
	private int totalTime;
	
	//constructor - fill with arguments if necessary
	public State() 
	{
		this.f = 0;
		this.h = 0;
		this.g = 0;
		this.father = null;
		this.totalTime = 0;
	}
	
	// copy constructor
	public State(State s)
	{
		// create a state similar with s...
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
	
	public void evaluate() 
	{
		//calculate f...
	}
	
	public void print() {}
	
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