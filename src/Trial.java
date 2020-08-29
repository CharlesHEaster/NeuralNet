import java.util.ArrayList;
import java.util.Collections;

public abstract class Trial {


	private ArrayList<Network> networks, theBest, theMorgue;
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

	public void setTrialInputs(Double[][] trialInputs) {
		this.TrialInputs = trialInputs;
	}
	
	public void setInputLegend(String[] inputLegend) {
		this.InputLegend = inputLegend;
	}
	
	public ArrayList<Network> getTheBest(){
		return this.theBest;
	}
	
	public Network getTheBest(int i){
		return this.theBest.get(i);
	}
	
	//constructors
	public Trial(int numNetworks, int numCycles, int[] netStructure, Double[][] trialInputs, String[] inputLegend){
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
		this.InputLegend = inputLegend;
	}

	//Set = A set of inputs
	//Cycle = all sets run once, then compare and cull
	//Trial = all cycles


	public void run() {
		for(int i = 0; i < this.cycles; i++) {
			System.out.print("Cycle: " + i + "/" + this.cycles + " :: ");
			this.runCycle();
		}
	}
	
	public void runCycle() {
		for (int setNum = 0; setNum < TrialInputs.length; setNum++) {
			this.runSet(TrialInputs[setNum]);			
		}
		this.compare();
		this.cullAndCreate();
	}
	
	public void runSet(Double[] inputs){
		// run a set of inputs
		for (int i = 0; i < this.networks.size(); i++){
			this.networks.get(i).resetOutputs();			
			this.networks.get(i).run(inputs);
			this.evaluateAndUpdate(networks.get(i), inputs);
		}
	}										

	public void compare(){
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder());
	}

	public void cullAndCreate() {
		this.theBest.clear();
		int top1 = (int)(this.numNetworks * 0.01);
		int top10 = (int)(this.numNetworks * 0.10);
		int top20 = (int)(this.numNetworks * 0.20);
		this.theBest.addAll(this.networks.subList(0,  top20)); // grab the top 20% and put them into 'theBest'
		if (this.fillTheMorgue$) {
			this.theMorgue.addAll(this.networks.subList(top20,  this.networks.size() - 1));
		}
		this.networks.clear();
		for (int i = 0; i < this.theBest.size(); i++) { // roll through those top 20%
			if (i < top1) {	
				for (int j = 0; j < 10; j++) {			// top 0% - 1% get 10 children
					this.networks.add(this.theBest.get(i).morph());
				}
			} else if (i < top10) {
				for (int j = 0; j < 5; j++) {			// top 1% - 10% get 5 children
					this.networks.add(this.theBest.get(i).morph());
				}
			} else {									// top 10% - 20% get 2 children
				this.networks.add(this.theBest.get(i).morph());
				this.networks.add(this.theBest.get(i).morph());
			}			
		}
		while (this.networks.size() < numNetworks) {	//fill in the rest of the networks with random 1stGen.  
			this.networks.add(new Network(this.structure));
		}
		System.out.print(" Score: " + this.theBest.get(0).getScore() + "\n");
	}
	
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network
	public abstract void evaluateAndUpdate(Network net, Double[] SetOfInputs);
	
	
	//This method is the final evaluation.  If score updates after each set, then it can just put in evaluateAndUpdate().
	//  else it can take the state variables and compute a score
	public abstract void finalEvaluateAndScore(Network net, Double[] SetOfInputs);
	
}
	



