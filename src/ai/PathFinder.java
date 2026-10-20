package ai;

import java.util.ArrayList;
import main.GamePanel;

public class PathFinder {
	/*
	 * 
	 */
	
	GamePanel gp;
	Node[][] node;
	ArrayList<Node> openList=new ArrayList<>();
	public ArrayList<Node> pathList=new ArrayList<>();
	Node startNode, goalNode, currentNode;
	boolean goalReached=false;
	int step=0;
	
	
	//Constructor
	public PathFinder(GamePanel gp) {
		
		this.gp=gp;
		instantiateNodes();
		
	}
	
	
	//Instantiates all nodes to the node array
	public void instantiateNodes() {
		
		node=new Node[gp.maxWorldCol][gp.maxWorldRow];
		
		int col=0;
		int row=0;
		
		while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
			
			node[col][row]=new Node(col, row);
			
			col++;
			if(col==gp.maxWorldCol) {
				
				col=0;
				row++;
				
			}
			
		}
		
	}
	
	
	//Reset all nodes and data
	public void resetNodes() {
		
		int col=0;
		int row=0;
		
		while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
			
			//Reset all node data
			node[col][row].open=false;
			node[col][row].checked=false;
			node[col][row].solid=false;
			
			col++;
			if(col==gp.maxWorldCol) {
				
				col=0;
				row++;
				
			}
			
		}
		
		//Reset other settings
		openList.clear();
		pathList.clear();
		goalReached=false;
		step=0;
		
	}
	
	
	public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
	    
	    resetNodes();
	    
	    //Set start and goal node
	    startNode=node[startCol][startRow];
	    currentNode=startNode;
	    goalNode=node[goalCol][goalRow];
	    openList.add(currentNode);
	    
	    int col=0;
	    int row=0;
	    
	    // Primera pasada: marcar tiles sólidos
	    while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
	        
	        int tileNum=gp.tm.mapTile[gp.currentMap][col][row];
	        
	        if(gp.tm.tile[tileNum].collision) {
	            node[col][row].solid=true;
	        }
	        
	        col++;
	        if(col==gp.maxWorldCol) {
	            col=0;
	            row++;
	        }
	        
	    }
	    
	    // Segunda pasada: expandir nodos sólidos para respetar el ancho/alto del NPC
	    col=0;
	    row=0;
	    while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
	        
	        if(node[col][row].solid) {
	            if(col-1>=0) node[col-1][row].solid=true;
	            if(row-1>=0) node[col][row-1].solid=true;
	        }
	        
	        col++;
	        if(col==gp.maxWorldCol) {
	            col=0;
	            row++;
	        }
	        
	    }
	    
	    // Asegurarse de que el nodo de inicio y meta no sean sólidos
	    startNode.solid=false;
	    goalNode.solid=false;
	    
	    // Calcular costes
	    col=0;
	    row=0;
	    while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
	        
	        getCost(node[col][row]);
	        
	        col++;
	        if(col==gp.maxWorldCol) {
	            col=0;
	            row++;
	        }
	        
	    }
	    
	}
	
	
	//Gets and sets the cost of a node
	public void getCost(Node node) {
		
		//Get G cost (Distance from the start node)
		int xDistance=Math.abs(node.col-startNode.col);
		int yDistance=Math.abs(node.row-startNode.row);
		node.gCost=xDistance+yDistance;
		
		//Get H cost (Distance from the goal node)
		xDistance=Math.abs(node.col-goalNode.col);
		yDistance=Math.abs(node.row-goalNode.row);
		node.hCost=xDistance+yDistance;
		
		//Get F cost (Total cost)
		node.fCost=node.gCost+node.hCost;
			
	}
	
	
	//Checks all adjacents ndoes
	public boolean search() {
		
		while(!goalReached && step<500) {
			
			int col=currentNode.col;
			int row=currentNode.row;
			
			//Check the current node
			currentNode.checked=true;
			openList.remove(currentNode);
			
			//Open the up node
			if(row-1>=0) {
				
				openNode(node[col][row-1]);
				
			}			
			
			//Open the left node
			if(col-1>=0) {
				
				openNode(node[col-1][row]);
				
			}			
			
			//Open the down node
			if(row+1<gp.maxWorldRow) {
				
				openNode(node[col][row+1]);
				
			}			
			
			//Open the right node
			if(col+1<gp.maxWorldCol) {
				
				openNode(node[col+1][row]);
				
			}
			
			
			//Find the best node
			int bestNodeIndex=0;
			int bestNodefCost=Integer.MAX_VALUE;
			
			for(int i=0;i<openList.size();i++) {
				
				//Check if this node's F cost is better
				if(openList.get(i).fCost<bestNodefCost) {
					
					bestNodeIndex=i;
					bestNodefCost=openList.get(i).fCost;
					
				}
				
				//If F cost is equal, check the G cost
				else if(openList.get(i).fCost==bestNodefCost) {
					
					if(openList.get(i).gCost<openList.get(bestNodeIndex).gCost) {
						
						bestNodeIndex=i;
						
					}
					
				}
				
				//If there is no node in the openList, end the loop
				if(openList.size()==0) {
					
					step=501;
					
				}
				
			}
			
			if(openList.isEmpty()) {
			    return false; // no path found
			}

			currentNode = openList.get(bestNodeIndex);

			if(currentNode == goalNode) {
			    goalReached = true;
			    trackThePath();
			}

			step++;
			
		}
		
		return goalReached;
		
	}
	
	
	//Opens a node and adds it to openList array
	public void openNode(Node node) {
		
		if(!node.open && !node.checked && !node.solid) {
			
			//If the node is not opened yet, add it to the open list
			node.open=true;
			node.parent=currentNode;
			openList.add(node);
			
		}
		
	}
	
	
	//Gets the past finded
	private void trackThePath() {
		
		//Backtrack and draw the best path
		Node current=goalNode;
		
		while(current!=startNode) {
			
			pathList.add(0, current);
			current=current.parent;
			
		}
		
	}
	
	
}



















































