import java.util.ArrayList;

public class Network implements Comparable<Network>{

	private ArrayList<ArrayList<Node>> nodes;
	private Double score;
	private ArrayList<Integer> heredity;
	private int children;
	private ArrayList<ArrayList<Double>> outputs;
	private ArrayList<Double> networkOutput;
	// Network State variables for some future iteration
//	private double[] stateVar; 
//	private String[] stateString;
	private static int FirstGen; // Static in Network assumes only one group of networks at a time, which is true

	//utilities
	private void createNodes(int[] structure) {
		this.nodes = new ArrayList<ArrayList<Node>>();
		for (int i = 0; i < structure.length; i++) {
			ArrayList<Node> temp = new ArrayList<Node>();
			for (int j = 0; j < structure[i]; j++) {
				if (i == 0) {
					temp.add(new InputNode());
				} else if (i == structure.length - 1){
					temp.add(new OutputNode(structure[i - 1]));
				} else {
					temp.add(new Node(structure[i - 1]));
				}
			}
			this.nodes.add(temp);
		}
	}
	
	public ArrayList<Double> run(Double[] inputs) {
	//1. Iterate through levels (columns) of nodes.
	//2. Iterate through individual nodes (step down each column)
	//3. if first column of nodes (hence input nodes), input only the corresponding input from "inputs"
	//  3b. else input all of the previous outputs as inputs
	//4. calculate output
	//5. add it to the ArrayList for the next group of outputs
	//6. add that ArrayList to double ArrayList of all node outputs for the network
	//7. repeat until all nodes are used.  Last column of outputs is network output.
		this.outputs.clear();
		
		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<Double> nextOutput = new ArrayList<Double>();
			for (int j = 0; j < nodes.get(i).size(); j++) {
				if (nodes.get(i).get(j) instanceof InputNode ) {
					((InputNode) nodes.get(i).get(j)).setInput(inputs[j]);
					nextOutput.add(nodes.get(i).get(j).calcOutput());

				} else {
					nodes.get(i).get(j).setInputs(this.outputs.get(i - 1));
					nextOutput.add(nodes.get(i).get(j).calcOutput());
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
	
	public String toStringHered() {
		String h = "";
		for (int i = 0; i < this.heredity.size(); i++) {
			h += heredity.get(i).toString() + "-";
		}
		return h;
	}
	
	public String toString() {
		String str = "Network {\nHeredity [";
		for (int i = 0; i < this.heredity.size(); i++) {
			str += this.heredity.get(i);
			if (i == this.heredity.size() - 1) {
				str += "]";
			} else {
				str += ", ";
			}
		}
		str += "  Network Structure[";
		for (int i = 0; i < this.nodes.size(); i++) {
			str += this.nodes.get(i).size();
			if (i == this.nodes.size() - 1) {
				str += "]  ";
			} else {
				str += ", ";
			}
		}
		str += "Score: " + this.getScore() + "\n";
		str += "Nodes::";
		for (int i = 0; i < this.nodes.size(); i++) {
			for (int j = 0; j < this.nodes.get(i).size(); j++) {
				str += "\n   Node[" + i + "][" + j + "] ";
				Node n = this.nodes.get(i).get(j);
				if (i == 0 && n instanceof InputNode) {
					str += "--InputNode--";
				} else if (i == this.nodes.size() - 1 && n instanceof OutputNode) {
					str += "--OutputNode--";
				}
				str += "\n      ";
				for (int k = 0; k < n.getWeights().length; k++) {
					if (!(n instanceof OutputNode) && k == n.getWeights().length - 1) {
						str += "[BIAS:" + n.getWeight(k) + "]";
					} else {
						str += "[" + n.getWeight(k) + "] ";
					}
					if ( k != 0 && k % 5 == 0 && k != n.getWeights().length - 1) {
						str += "\n      "; 
					} 
				}
				
			}
		}
		str += "\n}\n";
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
	
	public ArrayList<Double> getNetworkOutput() {
		return this.networkOutput;
	}

	//Network State variables for some future iteration
//	public double getStateVar(int i) {
//		return this.stateVar[i];
//	}
//
//	public void setStateVar(int i, double var){
//		this.stateVar[i] = var;
//	}
//
//	public double[ ] getStateVar() {
//		return this.stateVar;
//	}
//
//	public void setStateVar(double[ ] var){
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

	public Network morph(){
		ArrayList<ArrayList<Node>> newNodes = new ArrayList<ArrayList<Node>>();
		for (int i = 0; i < this.nodes.size(); i++){
			ArrayList<Node> temp = new ArrayList<Node>();
			for (int j = 0; j < this.nodes.get(i).size(); j++){
				temp.add(this.nodes.get(i).get(j).morph());
			}
			newNodes.add(temp);
		}

		ArrayList<Integer> newHered = new ArrayList<Integer>();
		newHered.addAll(this.heredity); 
		newHered.add(this.children);
		this.children++;
		return new Network(newNodes, newHered);
	}

	//constructors
	public Network(int[] structure) {  //new network, random nodes, first gen
		createNodes(structure);
		this.score = 0.0;
		this.heredity = new ArrayList<Integer>();
		this.heredity.add(Network.FirstGen);
		Network.FirstGen++;
		int children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> out1 = new ArrayList<Double>();
		outputs.add(out1);
		this.networkOutput = new ArrayList<Double>();
	}

	public Network(ArrayList<ArrayList<Node>> nodes, ArrayList<Integer> hered) {//new Network, product of a morph
		this.nodes = nodes;
		this.heredity = hered;
		this.score = 0.0;
		int children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> out1 = new ArrayList<Double>();
		outputs.add(out1);
		this.networkOutput = new ArrayList<Double>();
	   }
	
	@Override
	public int compareTo(Network o) { //to run a sort method on ArrayList<Network> networks need to be able to be compared.  
		return this.getScore().compareTo(o.getScore()); //This Override effectively tells the compare function which variable to use.
	} 

}
