import java.util.ArrayList;
import java.util.Arrays;

public class OutputNode extends Node{

	public OutputNode(int numWeights) {
		super(numWeights);
		this.setWeights(Arrays.copyOf(this.getWeights(), this.getWeights().length - 1));

	}
	
	public OutputNode(Node N) {
		super(N);
		if (!(N instanceof OutputNode)) {
			this.setWeights(Arrays.copyOf(this.getWeights(), this.getWeights().length - 1));
		}
	}
	
	public OutputNode(Double[] weights) {
		super(weights);
	}
	
	@Override
	public void setInputs(ArrayList<Double> inpu) {
		if (inpu.size() != this.getWeights().length) {
			System.out.println("ERROR 212: Node given incorrect number of inputs");
			System.out.println(this.getClass());
		}
		this.inputs = inpu;
	}
	
	@Override
	public Double calcOutput() {
		Double rawOutput = 0.0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += this.inputs.get(i) * this.getWeight(i);
		}
		Double output = Math.tanh(rawOutput);
		this.setOutput(output);

		return output;
	}
	
	@Override
	public Node morph(Double evolveRate, Double learnRate) {
		Double[] newWeights = new Double[this.getWeights().length];
		for (int i = 0; i < this.getWeights().length; i++) {
			if (Math.random() < evolveRate) { 
				learnRate = Math.random() < 0.5 ? learnRate * -1 : learnRate;
				newWeights[i] = this.getWeight(i) + learnRate;
				newWeights[i] = newWeights[i] > 1 ? 1 : newWeights[i]; 
				newWeights[i] = newWeights[i] < -1 ? -1 : newWeights[i];  
			} else {
				newWeights[i] = this.getWeights()[i]; 
			}
		}
		return new OutputNode(newWeights);
	}
	
	@Override
	public void setBias(Double nope) {
		System.out.println("Error 312: Tried to set bias of an OutputNode.  " + nope);
	}
}
