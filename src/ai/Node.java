package ai;

public class Node {
	/*
	 * 
	 */

	Node parent;
	public int col;
	public int row;
	int gCost;
	int hCost;
	int fCost;
	boolean solid;
	boolean open;
	boolean checked;
	
	
	//Constructor
	public Node(int col, int row) {
		
		this.col=col;
		this.row=row;
		
	}
	
	
}












































