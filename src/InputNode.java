import java.util.ArrayList;

public class InputNode extends Node{
	private double min;
	private double max;
	private double input;
	private int histLength;
	private ArrayList<Double> history;
	
	public InputNode() {
		super(0);
		this.min = 0;
		this.max = 0;
	}
	
	public InputNode(int hist) {
		super(0);
		this.min = 0;
		this.max = 0;
		this.histLength = hist;
	}
	
	public InputNode(int hist, double bias) {
		super(0);
		this.min = 0;
		this.max = 0;
		this.histLength = hist;
		this.setBias(bias);
	}
	
	
	public InputNode(double minn, double maxx, int hist, double bias) {
		super(0);
		this.min = minn;
		this.max = maxx;
		this.histLength = hist;
		this.setBias(bias);
	}
	
	public void setInput(double input) { 
		if (this.min == 0 && this.max == 0) {
			this.min = input - .0000000001;
			this.max = input + .0000000001;
		}
		this.input = input;
		if (this.histLength == 0) {
			this.min = input < this.min ? input : this.min;
			this.max = input > this.max ? input : this.max;
		} else if (this.histLength > 0) {
			this.history.remove(0);
			this.history.add(input);
			for (int i = 0; i < this.history.size(); i++) {
				this.min = this.history.get(i) < this.min ? this.history.get(i) : this.min;
				this.max = this.history.get(i) > this.max ? this.history.get(i) : this.max;
			}
		}	
	}

	@Override
	public double calcOutput() { 
		this.output = (this.input - this.min) / (this.max - this.min);
		this.output = (this.output * 2) - 1;
		this.output += this.getBias();
		
		return this.output;
	}
	
	@Override
	public Node morph() {
		double newBias = this.getBias();
		double rando = (Math.random() - 0.5) * 0.4;
		newBias = newBias + rando;
		newBias = newBias > 1 ? 1 : newBias; //check new weight is not more than 1
		newBias = newBias < -1 ? -1 : newBias;  //check new weight is not less than -1
		return new InputNode(this.min, this.max, this.histLength, newBias);
	}
}
