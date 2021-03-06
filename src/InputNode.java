import java.util.ArrayList;

public class InputNode extends Node{
	private Double min;
	private Double max;
	private Double input;
	private int histCapasity = -1;
	private ArrayList<Double> history;
	private String inputLabel;
	
	public InputNode() {
		super(0);
		this.min = 0.0;
		this.max = 0.0;
	}
	
	public InputNode(String label) {
		this();
		this.setInputLabel(label);
	}
	
	public InputNode(int histCapasity, String label) {
		this(label);
		this.histCapasity = histCapasity;
		this.history = new ArrayList<Double>();
	}
	
	public InputNode(int hist, Double bias, String label) {
		this(hist, label);
		this.setBias(bias);
	}
	
	public InputNode(Double bias, String label) {
		this(label);
		this.setBias(bias);
	}
	
	public String getInputLabel() {
		return this.inputLabel;
	}
	
	public void setInputLabel(String label) {
		this.inputLabel = label;
	}
	
	public void setMin(Double min) {
		this.min = min;
	}
	
	public void setMax(Double max) {
		this.max = max;
	}
	
	public Double getMin() {
		return this.min;
	}
	
	public Double getMax() {
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
		Double removed = 0.0;
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
	public Double calcOutput() { 
		if (this.min == this.max && this.min == this.input) {
			this.output = this.getBias();
		} else {
			this.output = (this.input - this.min) / (this.max - this.min);
			this.output = (this.output * 2) - 1;
			this.output += this.getBias();
		}
		
		return this.output;
	}
	
	@Override
	public Node morph(Double evolveRate, Double learnRate) {
		Double newBias = this.getBias();
		if (Math.random() < evolveRate) {
			learnRate = Math.random() < 0.5 ? learnRate * -1 : learnRate;
			newBias += learnRate;
			newBias = newBias > 1 ? 1 : newBias;
			newBias = newBias < -1 ? -1 : newBias;
		}
		String newLabel = this.getInputLabel();
		return new InputNode(this.histCapasity, newBias, newLabel);
	}
	
	@Override
	public String toString() {
		String str = "Label: " + this.getInputLabel();
		str += "\r\nWeights " + this.toStringRoundedWeights();
		str += "\r\nOutput = " + this.output;

		return str;
	}
}
