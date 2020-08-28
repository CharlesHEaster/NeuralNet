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
	
	public ArrayList<Network> getTheBest(){
		return this.theBest;
	}
	
	public Network getTheBest(int i){
		return this.theBest.get(i);
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
		System.out.println();
		this.networks.addAll(this.theBest);
		Collections.sort(this.networks, Collections.reverseOrder()); // reverse order because high score first
	}

	public void cullAndCreate() {//top 10% -> top 10% morph * 10% / other 90% morph * 5% | next 10% morph * 2%.  makes 75% morphed, then 25% random
		this.theBest.clear();
		int top1 = (int)(this.numNetworks * 0.01);
		int top10 = (int)(this.numNetworks * 0.10);
		int top20 = (int)(this.numNetworks * 0.20);
		this.theBest.addAll(this.networks.subList(0,  top20)); // grab the top 20% and put them into 'theBest'
		System.out.print("theBest ");
		for (int i = 0; i < this.theBest.size(); i++) {
			System.out.print("[" + this.theBest.get(i).toStringHered() + "] ");
		}
		System.out.print("\n");
		if (this.fillTheMorgue$) {
			this.theMorgue.addAll(this.networks.subList(top20,  this.networks.size() - 1));
		}
		this.networks.clear();
		for (int i = 0; i < this.theBest.size(); i++) { // roll through those top 20%
			if (i < top1) {	
//				System.out.println("Morph " + i + " x10");
				for (int j = 0; j < 10; j++) {			// top 1% get 10 children
					this.networks.add(this.theBest.get(i).morph());
				}
			} else if (i < top10) {
//				System.out.println("Morph " + i + " x5");
				for (int j = 0; j < 5; j++) {			// top 1% - 10% get 5 children
					this.networks.add(this.theBest.get(i).morph());
				}
			} else {									// top 10% - 20% get 2 children
				this.networks.add(this.theBest.get(i).morph());
				this.networks.add(this.theBest.get(i).morph());
//				System.out.println("Morph " + i + " x2");
			}			
		}
		while (this.networks.size() < numNetworks) {	//fill in the rest of the networks with random 1stGen.  
			this.networks.add(new Network(this.structure));
//			System.out.println("New Net");
		}
		System.out.print("Networks ");
		for (int i = 0; i < this.networks.size(); i++) {
			System.out.print("[" + this.networks.get(i).toStringHered() + "] ");
		}
		System.out.print("\n");
	}
	
	//This method takes in a network and it's outputs.  then evaluates those outputs against the answer and updates the network
	public abstract void evaluateAndUpdate(Network net, Double[] SetOfInputs);
	
	
	//This method is the final evaluation.  If score updates after each set, then it can just plut in evaluateAndUpdate().
	//  else it can take the state variables and compute a score
	public abstract void finalEvaluateAndScore(Network net, Double[] SetOfInputs);
	
}
	



