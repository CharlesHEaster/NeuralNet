import java.util.ArrayList;
import java.util.Arrays;

public class ColorTrial extends Trial {
	
	@Override
	private static string dir = "ColorTrialResults";

	public ColorTrial(int numNetworks, int numCycles, int[] netStructure, int numColors) {
		super(numNetworks, numCycles, netStructure, new Double[][] {}, new String[] {});
		Double[][] trialInputs = new Double[numColors][];
		for (int i = 0; i < numColors; i++) {
			trialInputs[i] = ColorTrial.randColor();
		}
		this.setTrialInputs(trialInputs);
		this.setInputLegend(new String[] {"Red", "Green", "Blue"});
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

	public static Double[] randColor(){
		Double[ ] color = new Double[3];
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
	
	@Override
	public void printBestToFile(int numBest) 
	{
		numBest = numBest > this.getNumNetworks() * 0.20 ? (int) (this.getNumNetworks() * 0.20) : numBest;
  String contents = "Color Trial, completed: " + Trial.dateAndTime() + "    Networks Trained for: " + Trial.convertNanoTime(this.getElapsed()) + "\r\n";
		contents += "   # of Networks : " + this.getNumNetworks() + "\r\n";
		contents += "     # of Cycles : " + this.getNumCycles() + "\r\n";
		contents += "Network Structure: " + Arrays.toString(this.getStructure()) + "\r\n";
		if (this.getMorgue()) {
			contents += "Dead Networks: KEPT\r\n";
		} else {
			contents += "Dead Networks: DISCARDED\r\n";
		}
		contents += "Input Legend\r\n";
		contents += Arrays.toString(this.getInputLegend()) + "\r\n\r\n";
		contents += "Inputs\r\n";
		contents += ((Trial) this).stringTrialInputs() + "\r\n\r\n";
		
		contents += "THE BEST NETWORKS\r\n";
		for (int i = 0; i < numBest; i++) {
			contents += this.getTheBest(i).toString();
		}
		
		
		
		
		//Trial.writeFile(dir, contents);
		System.out.println(contents);
	}

}
