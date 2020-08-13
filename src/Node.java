import java.util.ArrayList;

public class Node {


	ArrayList<Integer> inputs;
	private double[] weights;
	int output;
	double step;

	// getters and setters
	public void setInputs(ArrayList<Integer> inpu) {
		if (inpu.size() != this.weights.length) {
			System.out.println("ERROR 212: Node given incorrect number of inputs");
		}
		this.inputs = inpu;
	}
	
	public void setInputs(double inpu) {
		//this should never happen
		System.out.println("ERROR 459: Regular Node given double as input.");
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
	
	public double getStep() {
		return this.step;
	}

	// utilities
	public String toString() {
		String str = "Weights [";
		for (int i = 0; i < this.weights.length; i++) {
			str += this.weights[i];
			if (i == this.weights.length - 1) {
				str += "]";
			} else {
			str += ", ";
			}
		}
		if (this.inputs != null) {
			str += "\nInputs [";
			for (int i = 0; i < this.inputs.size(); i++) {
				str += this.inputs.get(i);
				if (i == this.inputs.size() - 1) {
					str += "]";
				} else {
				str += ", ";
				}
			}
		}
		str += "\nOutput = " + this.output;
			
		
		return str;
	}
	
	public int calcOutput() {
		double rawOutput = 0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += inputs.get(i) * weights[i];
		}
		if (rawOutput >= this.step) {
			this.output = 1;
		} else {
			this.output = 0;
		}
		return this.output;
	}
	
	public double calcOutputDouble() {
		//this should never happen
		System.out.println("ERROR 692: Regular Node asked to output double");
		return 0.0692;
	}

	public Node morph() {
		double[] newWeights = new double[this.weights.length];
		double newStep = 0;
		for (int i = 0; i < this.weights.length; i++) {
			if (Math.random() > .7) {
				double rando = (Math.random() - 0.5) * 0.4;
				newWeights[i] = this.weights[i] + rando;
				newWeights[i] = newWeights[i] > 1 ? 1 : newWeights[i];
				newWeights[i] = newWeights[i] < -1 ? -1 : newWeights[i];
			} else {
				newWeights[i] = this.weights[i];
			}
			if (newWeights[i] > 0) {
				newStep += newWeights[i];  //find highest possible weighted total
			}
		}
		if (newStep == 0) { // if all weights are negative, step is half the lowest possible
		    for (int i = 0; i < this.weights.length; i++) {
		        newStep += this.weights[i];
		    }
		}
        newStep = newStep / 2; // divide the highest (or lowest) possible weighted total by 2,  call it the step
        if (newStep == 0) { //if all weights are 0
            return this.morph();
        } else {
            return new Node(newWeights, newStep);
        }
	}
	
	public static double[] makeWeights(int numWeights){
	    double[] weights = new double[numWeights];
	    boolean zeros$ = true;
	    for (int i = 0; i < numWeights; i++) {
		    if ((Math.random() > 0.5){ // half of weights will have value
			weights[i] = (Math.random() * 2) - 1;
		    } else {
		        weights[i] = 0;
		    }
		    if (weights[i] == 0) { //if this weight is not 0
		        zeros$ = false;    // flip zeros$ to false
		    }
		}
		if (zeros$){ // if all the weights are 0, try again
		    return Node.makeWeights(numWeights)
		}
		return weights;
	}

	// Constructors

	public Node(double[] weigh, double st) {
		this.weights = weigh;
		this.step = st;
	}

	public Node(int numWeights) { // from scratch, random weights, random step
		this.weights = Node.makeWeights(numWeights);
		double newStep = 0;
		for (int i = 0; i < this.weights.length; i++) { //find highest possible positive value
		    if (this.weights[i] > 0){
		        newStep += this.weights[i];
		    }
		}
		if (newStep == 0){// if all weights negative or zero, find lowest possible value
		    for (int i = 0; i < this.weights.length; i++) {
		        newStep += this.weights[i];
		    }
		}
		this.step = newStep / 2; //devide max (or min) by two, call it the step(threshold value)
	}

	public Node(Node node) {
		this.setWeights(node.getWeights());
		this.step = node.step;
	}
}
