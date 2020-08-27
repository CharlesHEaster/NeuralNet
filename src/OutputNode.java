public class InputNode extends Node{
	private double input;
	

	public OutputNode(int numWeights) {
		super(numWeights);

	}
  @Override
  public double calcOutput() {
		//Activation function changed from bespoke "Frankenstein Equation" to tanh().  See note at end of code
		double rawOutput = 0;
		for (int i = 0; i < this.inputs.size(); i++) {
			rawOutput += inputs.get(i) * weights[i];
		}
		this.output = Math.tanh(rawOutput);

		return this.output;
	}

  @Override
	public static double[] makeWeightsFromScratch(int numWeights){ 
		double[] weights = new double[numWeights];
		if (numWeights < 6){// less than 6 weights would make too high a ratio of 0 weights
			for (int i = 0; i < numWeights; i++) {
				weights[i] = (Math.random() * 2) - 1; //each weight is a random value between -1 and 1
			}
			weights[numWeights] = Math.random() - 0.5;// bias between -.5 and .5
		} else { //if numWeights > 6, each node will average at least 2 weights.  ideal
			for (int i = 0; i < numWeights; i++) {
				if (Math.random() > 0.3){ // 1 in three weights will have value
					weights[i] = (Math.random() * 2) - 1;
				} else {
					weights[i] = 0;
				}
			}
		}
		return weights;
	}
}
