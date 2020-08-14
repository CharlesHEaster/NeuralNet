import java.util.ArrayList;

public class Node {


	ArrayList<Integer> inputs;
	private double[] weights;
	double output;

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

	public double getOutput() {
		return this.output;
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
	
	public double calcOutput() {
	    //Activation function changed from bespoke "Frankenstein Equation" to tanh.  See note at end of code
		double rawOutput = 0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += inputs.get(i) * weights[i];
		}
		this.output = Math.tanh(rawOutput);
		
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
	
	public static double[] makeWeights(int numWeights){ // change to better make number of 
	//0 weights proportionate to number of weights.  or just do it better
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

	public Node(double[] weigh) {
		this.weights = weigh;
	}

	public Node(int numWeights) { // from scratch, random weights, random step
		this.weights = Node.makeWeights(numWeights);
	}

	public Node(Node node) {
		this.setWeights(node.getWeights());
	}
}


/**Activation Function:  So I was using a weird pieced together activation function based on 
 * what I thought would be best, call it the Frankenstein Funciton.  I'm not a genius and
 * I am just learning, so I changed to one of the standard models, tanh().  If in a few
 * years I am a neural net genius and I decide to revisit this code, maybe some of the half
 * cooked uninformed ideas hold.  Till then, here is an explination of the 
 * Frankenstein Function and what I was thinking.**/
 
 /**Frankenstein Function:  Adapted step function.  If there are any positive weights,
  * step(threshold) = half of total positive weights.  if not, step = half of negative weights.
  * 
  * I was anticipating mostly positive inputs that had potential to be quite 
  * large.  So this would make the step value not be "so low" as to only be positive 
  * or an arbitrary value like 1, nor be exactly in the middle of the range.  It would
  * allow the step to be towards the higher end of the total possible range.  But 
  * I fear I am trying to muddle with too many variables at once (Neg vs Pos weights 
  * and step value).  So I think it is best to go with a standard activation model for now
  * and just learn how it propogates.  The End **/
