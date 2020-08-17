import java.util.ArrayList;

public class Network implements Comparable<Network>{

	ArrayList<ArrayList<Node>> nodes;
	Integer score;
	ArrayList<Integer> heredity;
	int children;
	ArrayList<ArrayList<Double>> outputs;
	ArrayList<Double> networkOutput;
	String[] netInputLegend;
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
		this.outputs.clear();
		for (int i = 0; i < nodes.size(); i++) {
			ArrayList<Double> nextOutput = new ArrayList<Double>();
			for (int j = 0; j < nodes.get(i).size(); j++) {
				if (i == 0) {
					nodes.get(i).get(j).setInputs(inputs[i]);
					nextOutput.add(nodes.get(i).get(j).calcOutput());
				} else {
				nodes.get(i).get(j).setInputs(this.outputs.get(i - 1));
				nextOutput.add(nodes.get(i).get(j).calcOutput());
				}
			}
			this.outputs.add(nextOutput);
		}
		return networkOutput;
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
			
		
		return str;
		
		
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
	   }

	public Network(ArrayList<ArrayList<Node>> nodes, ArrayList<Integer> hered) {
	   this.nodes = nodes;
	   this.heredity = hered;
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
