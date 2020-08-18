import java.util.ArrayList;

public class Node {


	ArrayList<Double> inputs;
	private double[] weights;
	double output;

	// getters and setters
	public void setInputs(ArrayList<Double> inpu) {
		if (inpu.size() != this.weights.length) {
			System.out.println("ERROR 212: Node given incorrect number of inputs");
		}
		this.inputs = inpu;
	}

	public ArrayList<Double> getInputs() {
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

	public double getOutput() {
		return this.output;
	}

	// utilities
	@Override
	public String toString() { 
		String str = "Weights ";
		str += this.toStringBasic();
		str += "\nOutput = " + this.output;

		return str;
	}

	public String toStringDetail() { 
		String str = "Weights [";
		for ( double weight: this.weights){
			str += weight + ", ";
		}
		str = str.substring(0, str.length() - 2) + "]";

		if (this.inputs != null) {
			str += "\nInputs [";
			for( double input: this.inputs){
				str += input + ", ";
			}
			str = str.substring(0, str.length() - 2) + "]";
		}
		str += "\nOutput = " + this.output;

		return str;
	}
	
	public String toStringBasic() { 
		String str = "[";
		for (double weight: this.weights){
			str += String.format("%.3f, ", weight);
		}
		str = str.substring(0, str.length() - 2) + "]";

		if (this.inputs != null) {
			str += "\nInputs [";
			for( double input: this.inputs){
				str += input + ", ";
			}
			str = str.substring(0, str.length() - 2) + "]";
		}
		return str;
	}
	
	public double calcOutput() {
		//Activation function changed from bespoke "Frankenstein Equation" to tanh().  See note at end of code
		double rawOutput = 0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += inputs.get(i) * weights[i];
		}
		this.output = Math.tanh(rawOutput);

		return this.output;
	}

	public Node morph() {
		double[] newWeights = new double[this.weights.length];
		for (int i = 0; i < this.weights.length; i++) {
			if (Math.random() > .7) { //3 in 10 nodes will be randomly adjusted
				double rando = (Math.random() - 0.5) * 0.4; //they will be randomly adjusted by range(-0.2 -> +0.2)
				newWeights[i] = this.weights[i] + rando;
				newWeights[i] = newWeights[i] > 1 ? 1 : newWeights[i]; //check new weight is not more than 1
				newWeights[i] = newWeights[i] < -1 ? -1 : newWeights[i];  //check new weight is not less than -1
			} else {
				newWeights[i] = this.weights[i]; // 7 in 10 nodes will be left as is
			}
		}
		return new Node(newWeights);
	}

	public static double[] makeWeightsFromScratch(int numWeights){ 
		double[] weights = new double[numWeights];
		boolean allZeros$ = true;
		if (numWeights < 6){//if 1 third of weights is not greater than 2, thus if numWeights < 6
			for (int i = 0; i < numWeights; i++) {
				weights[i] = (Math.random() * 2) - 1; //each weight is a random value between -1 and 1
				allZeros$ = false;
			}
		} else { //else numWeights > 6, each node will average at least 2 weights
			for (int i = 0; i < numWeights; i++) {
				if (Math.random() > 0.3){ // 1 in three weights will have value
					weights[i] = (Math.random() * 2) - 1;
				} else {
					weights[i] = 0;
				}
				if (weights[i] == 0) { //if this weight is not 0
					allZeros$ = false;    // flip allZeros$ to false
				}
			}
		}
		if (allZeros$){ // if all the weights are 0, try again
			weights = Node.makeWeightsFromScratch(numWeights);
		}
		return weights;
	}

	// Constructors

	public Node(double[] weigh) {
		this.weights = weigh;
	}

	public Node(int numWeights) { // from scratch, random weights
		this.weights = Node.makeWeightsFromScratch(numWeights);
	}

	public Node(Node node) {
		this.setWeights(node.getWeights());
	}
}


/**Activation Function:  So I was using a strange pieced together activation function based on 
 * what I thought would be best, call it the Frankenstein Function.  I'm not a genius and
 * I am just learning, so I changed to one of the standard models, tanh().  If in a few
 * years I am a neural net prodigy and I decide to revisit this code, maybe some of the half
 * cooked uninformed ideas hold.  Till then, here is an explanation of the 
 * Frankenstein Function and what the hell I was thinking.**/

/**Frankenstein Function:  Adapted step function.  If there are any positive weights,
 * step(threshold) = half of total positive weights.  if not, step = half of negative weights.
 * 
 * I was anticipating mostly positive inputs that had potential to be quite 
 * large.  So this would make the step value not be "so low" as to only be positive 
 * or an arbitrary value like 1, nor be exactly in the middle of the range.  It would
 * allow the step to be towards the higher end of the total possible range.  But 
 * I fear I am trying to muddle with too many variables at once (Neg vs Pos weights 
 * and step value).  So I think it is best to go with a standard activation model for now
 * and just learn how it propagates.  The End **/
