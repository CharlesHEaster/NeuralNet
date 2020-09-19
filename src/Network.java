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

	//constructors
	public Network(int[] struct, int firstGen) {
		this.structure = struct;
		createNodes(struct);
		this.score = 0.0;
		this.heredity = new ArrayList<Integer>();
		this.heredity.add(firstGen);
		this.children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		this.networkOutput = new ArrayList<Double>();
		this.checkNetStructure();
	}

	public Network(int[] struct, int firstGen, int numHistoryInputs, int[] inputHistoryStructure) {
		this.structure = struct;	
		createNodes(struct, numHistoryInputs, inputHistoryStructure);
		this.score = 0.0;
		this.heredity = new ArrayList<Integer>();
		this.heredity.add(firstGen);
		this.children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		this.networkOutput = new ArrayList<Double>();
		this.checkNetStructure();
	}

	public Network(ArrayList<ArrayList<Node>> nodes, ArrayList<Integer> hered) {
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
		//6. add that ArrayList to 2d ArrayList of all node outputs for the network
		//7. repeat until all nodes are used.  Last column of outputs is network output.
		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<Double> nextOutput = new ArrayList<Double>();
			for (int j = 0; j < nodes.get(i).size(); j++) {
				Node n = this.nodes.get(i).get(j);
				if (n instanceof InputNode) {
					InputNode nInput = (InputNode)n;
					nInput.setInput(inputs.get(j));
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
		String str = "Network {\r\n  Generation: " + this.heredity.size() + "  ";
		str += "  Score: " + this.getScore() + "\r\n";
		str += "  Heredity " + this.heredity.toString() + "\r\n";;
		str += "  Network Structure " + Arrays.toString(this.getStructure()) + "\r\n";
		str += "  Nodes::";
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
	
	public String toSave() {
		String save = "Network{\r\n";
		save += "  Heredity{" + this.heredity.toString() + "}\r\n";
		save += "  Score: " + this.getScore() + "\r\n";
		save += "  Nodes: ";
		for (int j = 0; j < this.nodes.get(0).size(); j++) {
			Node n = this.nodes.get(0).get(j);
			save += " node{InputNode(HistCapasity: " + ((InputNode)n).getHistCapasity() + ")";
			save += n.toSaveWeights();
			save += "}\r\n";
		}

		 
		for (int i = 1; i < this.nodes.size(); i++) {
			for (int j = 0; j < this.nodes.get(i).size(); j++) {
				Node n = this.nodes.get(i).get(j);
				save += " node{";
				save += n.toSaveWeights();
				save += "}\r\n";
				}

			} 
		save += "}/Nodes}/Network\r\n";
		
		return save;
	}

	public ArrayList<Double> getOutput() {
		return this.networkOutput;
	}

	public void incScore() {
		this.score += 1;
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
	
	
	public ArrayList<Integer> getHeredity(){
		return this.heredity;
	}
	
	public int getGeneration() {
		return this.heredity.size();
	}

	public ArrayList<Double> getNetworkOutput() {
		return this.networkOutput;
	}

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
	public int compareTo(Network that) { //to run a sort method on ArrayList<Network> networks need to be able to be compared. 
		Double thisNet = this.getScore();
		Double thatNet = that.getScore();
		thisNet -= this.getHeredity().size() * .0000001;
		thatNet -= that.getHeredity().size() * .0000001;
		return thisNet.compareTo(thatNet); //This Override effectively tells the compare function which variable to use.
	} 

}
