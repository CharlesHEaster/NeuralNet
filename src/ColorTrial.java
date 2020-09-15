import java.util.ArrayList;

public class ColorTrial extends Trial {
	

	public ColorTrial(int numNetworks, int numCycles, int[] netStructure, int numColors) {
		super(numNetworks, numCycles, netStructure, new ArrayList<ArrayList<Double>>(), new String[] {});
		this.setTrialInputs(ColorTrial.makeSomeColors(numColors));
		this.setInputLegend(new String[] {"Red", "Green", "Blue"});
		this.setDir("ColorTrialResults");
	}
	
	public ColorTrial(int numNetworks, int numCycles, int[] netStructure, int numColors, int numHistoryInpu, int[] inputHistoryStrut) {
		super(numNetworks, numCycles, netStructure, new ArrayList<ArrayList<Double>>(), new String[] {}, numHistoryInpu, inputHistoryStrut);
		this.setTrialInputs(ColorTrial.makeSomeColors(numColors));
		this.expandInputs();
		this.setInputLegend(new String[] {"Red", "Green", "Blue"});
		this.setDir("ColorTrialResults");
		
	}

	
	public static ArrayList<ArrayList<Double>> makeSomeColors(int num){
		ArrayList<ArrayList<Double>> arrayListOfColors = new ArrayList<ArrayList<Double>>();
		for (int i = 0; i < num; i++) {
			arrayListOfColors.add(ColorTrial.randColor());
		}
		return arrayListOfColors;
	}
	@Override
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network (score or state variables)
	public void evaluateAndUpdate(Network net, ArrayList<Double> inputColor) {
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
		ArrayList<Double> color = new ArrayList<Double>(inputColor);
		if (this.getNumHistoryInputs() != 0) {
			color = Trial.getUniqueInputs(color);
		} 
		if (color.get(0) > color.get(1) && color.get(0) > color.get(2)) {
			answer = "Red";
		}
		if (color.get(1) > color.get(0) && color.get(1) > color.get(2)) {
			answer = "Green";
		}
		if (color.get(2) > color.get(1) && color.get(2) > color.get(0)) {
			answer = "Blue";
		}

		if (NetworkOut.equals(answer)) {
			net.incScore();
		}
	} 
	
	@Override
	public void finalEvaluateAndScore(Network net, ArrayList<Double> SetInputs) {
		this.evaluateAndUpdate(net, SetInputs);
	}

	public static ArrayList<Double> randColor(){
		ArrayList<Double> color = new ArrayList<Double>();
		for (int i = 0; i < 3; i++) {
			color.add(Math.floor(Math.random() * 256));
		}
		while (color.get(0) == color.get(1) || color.get(1) == color.get(2)) {
			color.set(1,  Math.floor(Math.random() * 256));
		}
		while (color.get(1) == color.get(2) || color.get(2) == color.get(0)) {
			color.set(2,  Math.floor(Math.random() * 256));
		}
		return color;
	}
	

}
