import java.util.ArrayList;

public class InputNode extends Node{
	private double min;
	private double max;
	private Double input;
	private int histCapasity = -1;
	private ArrayList<Double> history;
	
	public InputNode() {
		super(0);
		this.min = 0;
		this.max = 0;
	}
	
	public InputNode(int histCapasity) {
		super(0);
		this.min = 0;
		this.max = 0;
		this.histCapasity = histCapasity;
		this.history = new ArrayList<Double>();
	}
	
	public InputNode(int hist, double bias) {
		super(0);
		this.min = 0;
		this.max = 0;
		this.histCapasity = hist;
		this.setBias(bias);
		this.history = new ArrayList<Double>();
	}
	
	public InputNode(double bias) {
		super(0);
		this.min = 0;
		this.max = 0;
		this.setBias(bias);
	}
	
	public double getMin() {
		return this.min;
	}
	
	public double getMax() {
		return this.max;
	}
	
	public ArrayList<Double> getHist(){
		return history;
	}
	
	public void setHistCapasity(int cap) {
		this.history = new ArrayList<Double>();
		this.histCapasity = cap;
	}
	
	public int getHistCapasity() {
		return this.histCapasity;
	}
	
	public void clearHistory() {
		this.history.clear();
	}
	
	public void setInput(Double input) { 
		if (this.min == 0 && this.max == 0) {
			this.min = input - .0000000001;
			this.max = input + .0000000001;
		}
		this.input = input;
		double removed = 0;
		if (this.histCapasity <= 0) {
			this.min = input < this.min ? input : this.min;
			this.max = input > this.max ? input : this.max;
		} else {
			while (history.size() >= histCapasity) {
				removed = this.history.remove(0);
			}
			this.history.add(input);
			if (removed <= this.min || removed >= this.max || this.input > this.max || this.input < this.min) {
				this.min = this.history.get(0);
				this.max = this.min;
				for (int i = 1; i < this.history.size(); i++) {
					this.min = this.history.get(i) < this.min ? this.history.get(i) : this.min;
					this.max = this.history.get(i) > this.max ? this.history.get(i) : this.max;
				}
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
	public Node morph(Double evolveRate, Double learnRate) {
		double newBias = this.getBias();
		if (Math.random() < evolveRate) {
			learnRate = Math.random() < 0.5 ? learnRate * -1 : learnRate;
			newBias += learnRate;
			newBias = newBias > 1 ? 1 : newBias;
			newBias = newBias < -1 ? -1 : newBias;
		}
		return new InputNode(this.histCapasity, newBias);
	}
}
