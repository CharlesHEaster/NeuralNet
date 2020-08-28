
public class InputNode extends Node{
	private double min;
	private double max;
	private double input;
	

	public InputNode() {
		super(1);
		this.min = -1;
		this.max = 1;
	}
	public InputNode(double minn, double maxx) {
		super(1);
		this.min = minn;
		this.max = maxx;
	}
	
	public void setInput(double input) { //takes in input.  checks if it is outside of min or max, and changes min or max as necessary
		this.input = input;
		this.min = input < this.min ? input : this.min;
		this.max = input > this.max ? input : this.max;
	}


	@Override
	public double calcOutput() { 
		this.output = (this.input - this.min) / (this.max - this.min);
		this.output = (this.output * 2) - 1;
		this.output = this.output * this.getWeight(0);
		this.output += this.getBias();
		
		return this.output;
	}
	
	@Override
	public Node morph() {
		return new InputNode(this.min, this.max);
	}
}
