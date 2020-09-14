import java.util.ArrayList;
import java.util.Arrays;

public class Network implements Comparable<Network>{

	private ArrayList<ArrayList<Node>> nodes;
	private Double score;
	private ArrayList<Integer> heredity;
	private int children;
	private ArrayList<ArrayList<Double>> outputs;
	private ArrayList<Double> networkOutput;
	private int[] structure;
	// Network State variables for some future iteration
	//	private Double[] stateVar; 
	//	private String[] stateString;
	private static int FirstGen; // Static in Network assumes only one group of networks at a time, which is true

	//constructors
	public Network(int[] struct) {  //new network, random nodes, first gen
		this.structure = struct;
		createNodes(struct);
		this.score = 0.0;
		this.heredity = new ArrayList<Integer>();
		this.heredity.add(Network.FirstGen);
		Network.FirstGen++;
		this.children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		this.networkOutput = new ArrayList<Double>();
		this.checkNetStructure();
	}

	public Network(int[] struct, int numHistoryInputs, int[] inputHistoryStructure) {  //new network, random nodes, first gen
		this.structure = struct;	
		createNodes(struct, numHistoryInputs, inputHistoryStructure);
		this.score = 0.0;
		this.heredity = new ArrayList<Integer>();
		this.heredity.add(Network.FirstGen);
		Network.FirstGen++;
		this.children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		this.networkOutput = new ArrayList<Double>();
		this.checkNetStructure();
	}

	public Network(ArrayList<ArrayList<Node>> nodes, ArrayList<Integer> hered) {//new Network, product of a morph
		this.nodes = nodes;
		this.checkNetStructure();
		this.heredity = hered;
		this.score = 0.0;
		this.children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		this.networkOutput = new ArrayList<Double>();
		this.checkNetStructure();
	}


	//utilities
	private void createNodes(int[] netStructure) {
		this.nodes = new ArrayList<ArrayList<Node>>();
		for (int i = 0; i < netStructure.length; i++) {
			ArrayList<Node> currentLayer = new ArrayList<Node>();
			for (int j = 0; j < netStructure[i]; j++) {
				if (i == 0) {
					currentLayer.add(new InputNode());
				} else if (i == netStructure.length - 1){
					currentLayer.add(new OutputNode(netStructure[i - 1]));
				} else {
					currentLayer.add(new Node(netStructure[i - 1]));
				}
			}
			this.nodes.add(currentLayer);
		}
		this.checkNetStructure();
	}

	private void createNodes(int[] netStructure, int numHistoryInputs, int[] inputHistoryStructure) {
		this.nodes = new ArrayList<ArrayList<Node>>();
		for (int i = 0; i < netStructure.length; i++) {  
			ArrayList<Node> currentLayer = new ArrayList<Node>();
			if (i == 0) { 
				for (int j = 0; j < numHistoryInputs; j++) { 
					for (int k = 0; k < inputHistoryStructure.length; k++) { 
						currentLayer.add(new InputNode(inputHistoryStructure[k]));
					}
				}
				for (int j = numHistoryInputs; j < this.structure[0]; j++) {
					currentLayer.add(new InputNode());
				}
			} else if ( i == netStructure.length - 1) {
				for (int j = 0; j < netStructure[i]; j++){
					int lastLayerSize = this.nodes.get(this.nodes.size() - 1).size();
					currentLayer.add(new OutputNode(lastLayerSize));
				}
			} else {
				for (int j = 0; j < netStructure[i]; j++){
					int lastLayerSize = this.nodes.get(this.nodes.size() - 1).size();
					currentLayer.add(new Node(lastLayerSize));
				}
			}

			this.nodes.add(currentLayer);
		}
		this.checkNetStructure();
	}

	public int[] checkNetStructure() {
		int[] netStructure = new int[this.nodes.size()];
		for (int i = 0; i < this.nodes.size(); i++) {
			netStructure[i] = this.nodes.get(i).size();
		}
		this.structure = netStructure;
		return netStructure;
	}

	public ArrayList<Double> run(ArrayList<Double> inputs) {
		//1. Iterate through levels (columns) of nodes.
		//2. Iterate through individual nodes (step down each column)
		//3. if first column of nodes (hence input nodes), input only the corresponding input from "inputs"
		//  3b. else input all of the previous outputs as inputs
		//4. calculate output
		//5. add it to the ArrayList for the next group of outputs
		//6. add that ArrayList to Double ArrayList of all node outputs for the network
		//7. repeat until all nodes are used.  Last column of outputs is network output.
		this.outputs.clear();
		for (int i = 0; i < nodes.size(); i++) {	//for each layer i
			ArrayList<Double> nextOutput = new ArrayList<Double>();
			for (int j = 0; j < nodes.get(i).size(); j++) { // for each node in layer j
				Node n = this.nodes.get(i).get(j);
				if (n instanceof InputNode) {
					InputNode nInput = (InputNode)n;
					nInput.setInput(inputs.get(j));
					nextOutput.add(n.calcOutput());
				} else if (n instanceof OutputNode){
					n.setInputs(this.outputs.get(i - 1));
					nextOutput.add(n.calcOutput());
				} else {
					n.setInputs(this.outputs.get(i - 1));
					nextOutput.add(n.calcOutput());
				}
			}
			this.outputs.add(nextOutput);
		}
		this.networkOutput = this.outputs.get(this.outputs.size() - 1);
		return this.networkOutput;
	}

	public void resetOutputs() {
		this.outputs = new ArrayList<ArrayList<Double>>();
	}

	public void clearInputHistories() {
		for (int i = 0; i < nodes.get(0).size(); i++) {
			((InputNode) nodes.get(0).get(i)).clearHistory();
		}
	}

	public String toString() {
		this.checkNetStructure();
		String str = "Network {\r\n Heredity ";
		str += this.heredity.toString();
		str += "\r\n  Network Structure ";
		str += Arrays.toString(this.getStructure());
		str += "  Score: " + this.getScore() + "\r\n\r\n";
		str += " Nodes::";
		for (int i = 0; i < this.nodes.size(); i++) {
			for (int j = 0; j < this.nodes.get(i).size(); j++) {
				str += "\r\n   Node[" + i + "][" + j + "] ";
				Node n = this.nodes.get(i).get(j);
				if (i == 0 && n instanceof InputNode) {
					str += "--InputNode--";
				} else if (i == this.nodes.size() - 1 && n instanceof OutputNode) {
					str += "--OutputNode--";
				}
				str += "\r\n      ";
				for (int k = 0; k < n.getWeights().length; k++) {
					if (!(n instanceof OutputNode) && k == n.getWeights().length - 1) {
						str += "[BIAS:" + n.getWeight(k) + "]";
					} else {
						str += "[" + n.getWeight(k) + "] ";
					}
					if ( k != 0 && k % 5 == 0 && k != n.getWeights().length - 1) {
						str += "\r\n      "; 
					} 
				}

			}
		}
		str += "\r\n" + "}" + "\r\n\r\n";
		return str;	
	}

	public ArrayList<Double> getOutput() {
		return this.networkOutput;
	}

	public void incScore() {
		this.score++;
	}	
	public void incScore(double inc) {
		this.score += inc;
	}
	public void incScore(int inc) {
		this.score += inc;
	}
	public void incScore(Double inc) {
		this.score += inc;
	}
	public void incScore(Integer inc) {
		this.score += inc;
	}

	public Double getScore() {
		return this.score;
	}

	public Node getNode(int col, int colnum) {
		return this.nodes.get(col).get(colnum);
	}

	public int[] getStructure() {
		this.checkNetStructure();
		return this.structure;
	}

	public ArrayList<Double> getNetworkOutput() {
		return this.networkOutput;
	}

	//Network State variables for some future iteration
	//	public Double getStateVar(int i) {
	//		return this.stateVar[i];
	//	}
	//
	//	public void setStateVar(int i, Double var){
	//		this.stateVar[i] = var;
	//	}
	//
	//	public Double[ ] getStateVar() {
	//		return this.stateVar;
	//	}
	//
	//	public void setStateVar(Double[ ] var){
	//		this.stateVar = var;
	//	}
	//
	//	public void resetStateVar(){
	//		for(int i =0; i < this.stateVar.length; i++){
	//			this.stateVar[i] = 0;
	//		}
	//	}
	//
	//	public String getStateString(int i) {
	//		return this.stateString[i];
	//	}
	//
	//	public void setStateString(int i, String str){
	//		this.stateString[i] = str;
	//	}
	//
	//	public String[ ] getStateString() {
	//		return this.stateString;
	//	}
	//
	//	public void setStateString(String[ ] str){
	//		this.stateString = str;
	//	} 

	public Network morph(Double evolveRate, Double learnRate){
		ArrayList<ArrayList<Node>> newNodes = new ArrayList<ArrayList<Node>>();
		for (int i = 0; i < this.nodes.size(); i++){
			ArrayList<Node> temp = new ArrayList<Node>();
			for (int j = 0; j < this.nodes.get(i).size(); j++){
				temp.add(this.nodes.get(i).get(j).morph(evolveRate, learnRate));
			}
			newNodes.add(temp);
		}

		ArrayList<Integer> newHered = new ArrayList<Integer>();
		newHered.addAll(this.heredity); 
		newHered.add(this.children);
		this.children++;
		return new Network(newNodes, newHered);
	}

	@Override
	public int compareTo(Network o) { //to run a sort method on ArrayList<Network> networks need to be able to be compared.  
		return this.getScore().compareTo(o.getScore()); //This Override effectively tells the compare function which variable to use.
	} 

}
