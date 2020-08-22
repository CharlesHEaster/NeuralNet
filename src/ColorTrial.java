import java.util.ArrayList;

public class ColorTrial extends Trial {

	public ColorTrial(int numNetworks, int numCycles, int[] netStructure, Double[][] trialInputs) {
		super(numNetworks, numCycles, netStructure, trialInputs);
	}

	@Override
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network (score or state variables)
	public void evaluateAndUpdate(Network net, Double[ ] inputColor) {
		//Decode NetworkOutput
		String NetworkOut = "";
		ArrayList<Double> outputs = net.getOutput();
		if (outputs.get(0) > outputs.get(1) && outputs.get(0) > outputs.get(2)) {
			NetworkOut = "Red";
		}
		if (outputs.get(1) > outputs.get(0) && outputs.get(1) > outputs.get(2)) {
			NetworkOut = "Green";
		}
		if (outputs.get(2) > outputs.get(0) && outputs.get(2) > outputs.get(1)) {
			NetworkOut = "Blue";
		}

		//Determine color
		String answer = "";
		Double[ ] color = inputColor;
		if (color[0] > color[1] && color[0] > color[2]) {
			answer = "Red";
		}
		if (color[1] > color[0] && color[1] > color[2]) {
			answer = "Green";
		}
		if (color[2] > color[1] && color[2] > color[0]) {
			answer = "Blue";
		}
		if (NetworkOut.equals(answer)) {
			net.incScore();
		}
	} 
	
	@Override
	public void finalEvaluateAndScore(Network net, Double[] SetInputs) {
		this.evaluateAndUpdate(net, SetInputs);
	}

	public static double[ ] randColor(){
		double[ ] color = new double[3];
		color[0] = (Math.floor(Math.random() * 256));
		color[1] = (Math.floor(Math.random() * 256));
		while (color[1] == color[0]) {
			color[1] = (Math.floor(Math.random() * 256));
		}
		color[2] = Math.floor(Math.random() * 256);
		while (color[2] == color[0] || color[2] == color[1]) {
			color[2] = Math.floor(Math.random() * 256);
		}
		return color;
	}

}