
public class InputNode extends Node{
	double min;
	double max;
	double doubleInput;
	

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
	
	public void setInput(double input) {
		doubleInput = input;
		this.min = input < this.min ? input : this.min;
		this.max = input > this.max ? input : this.max;
	}


	@Override
	public int calcOutput() {
		double adjustedInput = this.doubleInput - this.min;
		double range = this.max - this.min;
		adjustedInput = adjustedInput / range;
		if (adjustedInput >= 0.5) {
			this.output = 1;	
		} else {
			this.output = 0;
		}
		return this.output;
	}
	
	@Override
	public Node morph() {
		return new InputNode(this.min, this.max);
	}
}
