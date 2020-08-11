import java.util.ArrayList;

public class Node {
//TODO
//research activation equations 

	ArrayList<Integer> inputs;
	private double[] weights;
	int output;

	// getters and setters
	public void setInputs(ArrayList<Integer> inpu) {
		this.inputs = inpu;
	}
	
	public void setInputs(double inpu) {
		//this should never happen
		println("ERROR 459: Regular Node given double as input.")
	}

	public ArrayList<Integer> getInputs() {
		return this.inputs;
	}

	public void setWeights(double[] weig) {
		this.weights = weig;
	}

	public double[] getWeights() {
		return this.weights;
	}

	public double getWeight(int i) {
		return this.weights[i];
	}

	public int getOutput() {
		return this.output;
	}

	// utilities
	public int calcOutput() {
		double rawOutput = 0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += inputs.get(i) * weights[i];
		}
		if (rawOutput > 0) {
			this.output = 1;
		} else if ( rawOutput < 0) {
			this.output = -1;
		} else {
			this.output = 0;
		}
		return this.output;

	}
	public double calcOutputDouble() {
		//this should never happen
		System.out.println("ERROR 692: Regular Node asked to output double"):
		return 0.0692;
	}

	public Node morph() {
		double[] newWeights = new double[this.weights.length];
		for (int i = 0; i < this.weights.length; i++) {
			if (Math.random() > .7) {
				double rando = (Math.random() - 0.5) * 0.4;
				newWeights[i] = this.weights[i] + rando;
				newWeights[i] = newWeights[i] > 1 ? 1 : newWeights[i];
				newWeights[i] = newWeights[i] < -1 ? -1 : newWeights[i];
			}
		}

		return new Node(newWeights);
	}

	// Constructors

	public Node(double[] weigh) {// is new node, probably after morph
		this.weights = weigh;
	}

	public Node(int numWeights) { // from scratch, random weights

		for (int i = 0; i < numWeights; i++) {
			this.weights[i] = (Math.random() * 2) - 1;
		}
	}

	public Node(Node node) {
		this.setWeights(node.getWeights());
	}
}
