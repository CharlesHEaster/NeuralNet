import java.util.ArrayList;

public class Network implements Comparable<Network>{

	private ArrayList<ArrayList<Node>> nodes;
	private Integer score;
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
				} else {
					temp.add(new Node(structure[i - 1]));
				}
			}
			this.nodes.add(temp);
		}
	}
	
	public ArrayList<Double> run(double[] inputs) {
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
	
	public String toString() {
		String str = "Network Structure[";
		for (int i = 0; i < this.nodes.size(); i++) {
			str += this.nodes.get(i).size();
			if (i == this.nodes.size() - 1) {
				str += "]";
			} else {
				str += ", ";
			}
		}
		str += "  Heredity [";
		for (int i = 0; i < this.heredity.size(); i++) {
			str += this.heredity.get(i);
			if (i == this.heredity.size() - 1) {
				str += "]";
			} else {
				str += ", ";
			}
		}
		if (!this.networkOutput.isEmpty()) {
			str += "  Outputs [";
			for (int i = 0; i < this.networkOutput.size(); i++) {
				str += String.format("%.3f, ", this.networkOutput.get(i));
				if (i == this.networkOutput.size() - 1) {
					str += "]";
				} else {
					str += ", ";
				}
			}
		}

		return str;	
	}
	
	public String toStringDetail() {
		String message = "\n";
		for (int i = 0; i < nodes.size(); i++) {
			message += "[";
			for (int j = 0; j < nodes.get(i).size(); j++) {
				message += this.nodes.get(i).get(j).toStringBasic();
				if (j != nodes.get(i).size() - 1) {
					message += " ";
				}
			}
			message += "]\n";
		}

		return this.toString() + message; 
	}

	public ArrayList<Double> getOutput() {
		return this.networkOutput;
	}

	public void incScore() {
		this.score++;
	}

	public Integer getScore() {
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
		this.score = 0;
		this.heredity = new ArrayList<Integer>();
		this.heredity.add(Network.FirstGen);
		Network.FirstGen++;
		int children = 0;
		this.outputs = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> out1 = new ArrayList<Double>();
		outputs.add(out1);
		this.networkOutput = new ArrayList<Double>();
	}

	public Network(ArrayList<ArrayList<Node>> nodes, ArrayList<Integer> hered) {
		this.nodes = nodes;
		this.heredity = hered;
		this.nodes = new ArrayList<ArrayList<Node>>();
		this.score = 0;
		int children;
		this.outputs = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> out1 = new ArrayList<Double>();
		outputs.add(out1);
		this.networkOutput = new ArrayList<Double>();
	   }

//         ----Non Implemented or Tested Phone Code----
////	public void show(graphics g){
////	   //set x and y canvas max
////	   //set node size as fraction of y
////	   //draw nodes and weights
////	   int yInterval = yMax / this.nodes.length;
////	   for (int i = 0; i < this.nodes.length; i++){
////	      Int xInterval = xMax /   
////	         this.nodes[i].length;
////	      for ( int j = 0; j < this.nodes[i].length;
////	         j++){
////	         //draw the node, shaded for output
////	           g.elipse( (j+1)*xInterval, (i+1)*yInterval, idontcareanymore.  Figureitoutwhenyouarecoding
////	         //draw weights, stroke=map(weight, -1, 1, 0, strokeMax)
////	}
//
	@Override
	public int compareTo(Network o) {

		return this.getScore().compareTo(o.getScore());
	} 

}
