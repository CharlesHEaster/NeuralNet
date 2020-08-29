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
	
	@Override
	public void setInputs(ArrayList<Double> inpu) {
		if (inpu.size() != this.getWeights().length) {
			System.out.println("ERROR 212: Node given incorrect number of inputs");
			System.out.println(this.getClass());
		}
		this.inputs = inpu;
	}
	
	@Override
	public double calcOutput() {
		double rawOutput = 0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += this.inputs.get(i) * this.getWeight(i);
		}
		double output = Math.tanh(rawOutput);
		this.setOutput(output);

		return output;
	}
}
