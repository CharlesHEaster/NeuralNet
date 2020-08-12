
public class OutputNode extends Node{
	String name;

	public OutputNode(double[] weights, double step) {
		super(weights, step);
		
	}
	
	public OutputNode(int numWeights) {
		super(numWeights);
		
	}
	public double calcOutputDouble() {
		//inputs.get(i) * weights[i]
		double totalW = 0;
		double totalInputs = 0;
		for (int i = 0; i < inputs.size(); i++) {
			totalInputs += inputs.get(i) * this.getWeights()[i];
			totalW += this.getWeights()[i];
		}
		return totalInputs / totalW;
		
		
		
	}
	@Override
	public Node morph() {
		double[] newWeights = new double[this.getWeights().length];
		
		for (int i = 0; i < this.getWeights().length; i++) {
			if (Math.random() > .7) {
				double rando = (Math.random() - 0.5) * 0.4;
				newWeights[i] = this.getWeights()[i] + rando;
				newWeights[i] = newWeights[i] > 1 ? 1 : newWeights[i];
				newWeights[i] = newWeights[i] < -1 ? -1 : newWeights[i];
			} else {
				newWeights[i] = this.getWeights()[i];
			}
			
		}

		return new OutputNode(newWeights, 1); //Step is arbitrary as it won't be used on an output node
	}
}
