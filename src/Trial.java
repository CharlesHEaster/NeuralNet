import java.util.ArrayList;
import java.util.Collections;

public class Trial {


	ArrayList<Network> networks, theBest, theMorgue;
	private int cycles;
	private int[] structure;
	private RandColor[] inputs;
	private boolean fillTheMorgue$;
	private String[] InputLegend;

	public void setMorgue(boolean fill$) {
		this.fillTheMorgue$ = fill$;
	}
	
	public ArrayList<Network> getNetworks(){
		return this.networks;
	}

	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, RandColor[] trialInputs){
		//setup
		this.networks = new ArrayList<Network>();
		for (int i = 0; i < numNetworks; i++) {
			this.networks.add(new Network(netStructure));
		}
		this.theBest = new ArrayList<Network>();
		this.cycles = numCycles;
		this.structure = netStructure;
		this.inputs = trialInputs;
		this.fillTheMorgue$ = false;
		this.InputLegend = new String[3];
		this.InputLegend[0] = "Red";
		this.InputLegend[1] = "Green";
		this.InputLegend[2] = "Blue";

	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles


	public void runSet(RandColor color){  //this began to become tailored to the color trial.  it's all getting muddled.  get to color trial action, then fix
		// run a set of inputs
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();
			double[] in = new double[3];
			in[0] = color.getValue(0);
			in[1] = color.getValue(1);
			in[2] = color.getValue(2);			
			this.networks.get(i).run(in);
			this.evaluate(networks.get(i), networks.get(i).getNetworkOutput(), color);
		}
	}										


	public void runCycle() {
		for (int setNum = 0; setNum < inputs.length; setNum++) {
			this.runSet(inputs[setNum]);			
		}

		this.compare();
		//		this.cullAndCreate();

	}

	public void compare(){
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder()); // reverse order because high score first
	}

	//Make abstract later, and make classes that extend trial.  MyTrial, ColorTrial...
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network
	public void evaluate(Network net, ArrayList<Double> outputs, RandColor color) {
		//This one is specific to ColorTrial
		//Determine NetworkOutput
		String NetworkOut = "";
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
		if (color.values[0] > color.values[1] && color.values[0] > color.values[2]) {
			answer = "Red";
		}
		if (color.values[1] > color.values[0] && color.values[1] > color.values[2]) {
			answer = "Green";
		}
		if (color.values[2] > color.values[1] && color.values[2] > color.values[0]) {
			answer = "Blue";
		}

		if (NetworkOut.equals(answer)) {
			net.incScore();
		}
	}
}





