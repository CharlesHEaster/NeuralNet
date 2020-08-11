
public class OutputNode extends Node{
	String name;

	public OutputNode(int numWeights) {
		super(numWeights);
		// TODO Auto-generated constructor stub
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

}
