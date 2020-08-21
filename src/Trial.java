import java.util.ArrayList;
import java.util.Collections;

public abstract class Trial {


	ArrayList<Network> networks, theBest, theMorgue;
	private int cycles, numNetworks;
	private int[] structure;
	private Double[][] TrialInputs;
	private boolean fillTheMorgue$;
	private String[] InputLegend;

	public void setMorgue(boolean fill$) {
		this.fillTheMorgue$ = fill$;
	}
	
	public ArrayList<Network> getNetworks(){
		return this.networks;
	}

	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, Double[][] trialInputs){
		//setup
		this.networks = new ArrayList<Network>();
		this.numNetworks = numNetworks;
		for (int i = 0; i < numNetworks; i++) {
			this.networks.add(new Network(netStructure));
		}
		this.theBest = new ArrayList<Network>();
		this.cycles = numCycles;
		this.structure = netStructure;
		this.TrialInputs = trialInputs;
		this.fillTheMorgue$ = false;
		this.InputLegend = new String[3];
		this.InputLegend[0] = "Red";
		this.InputLegend[1] = "Green";
		this.InputLegend[2] = "Blue";

	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles


	public void runSet(Double[] inputs){
		// run a set of inputs
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();			
			this.networks.get(i).run(inputs);
			this.evaluateAndUpdate(networks.get(i), inputs);
		}
	}										


	public void runCycle() {
		for (int setNum = 0; setNum < TrialInputs.length; setNum++) {
			this.runSet(TrialInputs[setNum]);			
		}

		this.compare();
		this.cullAndCreate();

	}
	
	public void run() {
		for(int i = 0; i < this.cycles; i++) {
			this.runCycle();
		}
	}

	public void compare(){
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder()); // reverse order because high score first
	}

	public void cullAndCreate() {//top 10% -> top 10% morph * 10% / other 90% morph * 5% | next 10% morph * 2%.  makes 75% morphed, then 25% random
		this.theBest.clear();
		this.theBest.addAll(this.networks.subList(0,  (int) (this.networks.size() * 0.2))); // grab the top 20% and put them into 'theBest'
		this.networks.clear();
		//I tried but I lost it.  It's too late.  try again next time.  format is in testStuff
		
		
		
		
		
		
		
		
	}
	
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network
	public abstract void evaluateAndUpdate(Network net, Double[] SetInputs);
	
	
	//This method is the final evaluation.  If score updates after each set, then it can just plut in evaluateAndUpdate().
	//  else it can take the state variables and compute a score
	public abstract void finalEvaluateAndScore(Network net, Double[] SetInputs);
	
}
	



