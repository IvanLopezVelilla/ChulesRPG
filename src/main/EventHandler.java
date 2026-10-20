package main;

import entity.Entity;

public class EventHandler {
	/*
	 * Manage the game events
	 */
	
	GamePanel gp;
	EventRect eventRect[][][];
	Entity eventMaster; //This is god. He speaks to the player directly. He is not real, like any god from any religion
	
	int previousEventX, previousEventY;
	boolean canTouchEvent=true;
	
	public EventHandler(GamePanel gp) {
		
		this.gp=gp;
		
		this.eventMaster=new Entity(gp);
		this.eventRect=new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldCol];
		
		int col=0;
		int row=0;
		int map=0;
		
		while(map<gp.maxMap && col<gp.maxWorldCol && row<gp.maxWorldRow) {
			
			eventRect[map][col][row]=new EventRect();
			eventRect[map][col][row].x=23;
			eventRect[map][col][row].y=23;
			eventRect[map][col][row].width=2;
			eventRect[map][col][row].height=2;
			eventRect[map][col][row].eventRectDefaultX=eventRect[map][col][row].x;
			eventRect[map][col][row].eventRectDefaultY=eventRect[map][col][row].y;
			
			col++;
			if(col==gp.maxWorldCol) {
				
				col=0;
				row++;
				
				if(row == gp.maxWorldRow) {
					
					row=0;
					map++;
					
				}
				
			}
			
		}
		
		setDialogue();
		
	}
	
	
	//Sets the dialogues for any event to happen
	public void setDialogue() {
		
		eventMaster.dialogues[0][0]="Progress have been save, you may go out\nand live your life now";
		eventMaster.dialogues[0][1]="Please go out and be a functional person";
		eventMaster.dialogues[1][0]="You look at the bed...";
		eventMaster.dialogues[1][1]="You feel rested?";
		
	}
	
	
	//Checks what event the player collided with and calls it
	public void checkEvent() {
		
		//Check if the player is 1 tile away from last event
		int xDistance=Math.abs(gp.player.worldX-previousEventX);
		int yDistance=Math.abs(gp.player.worldY-previousEventY);
		int distance=Math.max(xDistance, yDistance);
		
		if(distance>gp.tileSize) {
			
			canTouchEvent=true;
			
		}
		
		if(canTouchEvent) {
			
			//0 Main loby 
			if(hit(0,17,3,"any")) {teleport(1,24,21,AREA.SHOP);} //enter shop
			
			else if(hit(0,2,13,"any")) {teleport(2,15,44,AREA.DUNGEON);} //enter dungeon
			else if(hit(0,27,2,"any")) {teleport(2,25,14,AREA.DUNGEON);} //enter dungeon
			
			else if(hit(0,28,14,"any")) {teleport(3,8,21,AREA.SNOW);} //enter snow
			else if(hit(0,33,3,"any")) {teleport(3,19,41,AREA.SNOW);} //enter snow
			
			else if(hit(0,48,14,"any")) {teleport(4,48,1,AREA.FABRIK);} //enter fabrik
			
			
			//1 Shop			
			else if(hit(1,24,21,"any")) {teleport(0,17,3,AREA.MAINLOBBY);} //exit shop
			
			else if(hit(1, 24, 15, "up")) {speak(gp.npcs[1][0]);} //Talk with merchant
			
			else if(hit(1, 26, 14, "up")) {rest();} //Rest
			
			else if(hit(1, 22, 14, "up")) {save();} //Save
			
			
			//2 Dungeon
			else if(hit(2,15,44,"any")) {teleport(0,2,13,AREA.MAINLOBBY);} //exit dungeon
			else if(hit(2,25,14,"any")) {teleport(0,27,2,AREA.MAINLOBBY);} // exit dungeon
			
			
			//3 Snow
			else if(hit(3,8,21,"any")) {teleport(0,28,14,AREA.MAINLOBBY);} //exit snow
			else if(hit(3,19,41,"any")) {teleport(0,33,3,AREA.MAINLOBBY);} //exit snow
			
			
			//4 Fabrick
			else if(hit(4,48,1,"any")) {teleport(0,48,14,AREA.MAINLOBBY);} //exit fabrik
			
		}
		
	}
	
	
	//Checks if the player collided with an event tile
	public boolean hit(int map, int col, int row, String reqDirection) {
		
		boolean hit=false;
		
		if(map==gp.currentMap) {
			
			gp.player.solidArea.x+=gp.player.worldX;
			gp.player.solidArea.y+=gp.player.worldY;
			eventRect[map][col][row].x=col*gp.tileSize+eventRect[map][col][row].x;
			eventRect[map][col][row].y=row*gp.tileSize+eventRect[map][col][row].y;
			
			if(gp.player.solidArea.intersects(eventRect[map][col][row]) && !eventRect[map][col][row].eventDone) {
				
				if(gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
					
					hit=true;
					
					//Keep track of the tile the event gets called
					previousEventX=gp.player.worldX;
					previousEventY=gp.player.worldY;
					
				}
				
			}
			
			gp.player.solidArea.x=gp.player.solidAreaDefaultX;
			gp.player.solidArea.y=gp.player.solidAreaDefaultY;
			eventRect[map][col][row].x=eventRect[map][col][row].eventRectDefaultX;
			eventRect[map][col][row].y=eventRect[map][col][row].eventRectDefaultY;			
			
		}
		
		return hit;
		
	}
	
	
	//Teleports the player to another tile and/or map
	public void teleport(int map, int col, int row, AREA area) {
		
		gp.currentMap=map;
		gp.nextArea=area;
		gp.player.worldX=gp.tileSize*col;
		gp.player.worldY=gp.tileSize*row;
		
		previousEventX=gp.player.worldX;
		previousEventY=gp.player.worldY;
		canTouchEvent=false;
		
		gp.playSound("stairs");
		gp.changeArea();
		
	}
	
	
	//Talk with npcs when not being next to them
	public void speak(Entity entity) {
		
		if(gp.ih.kPressed) {
			
			gp.gameState=GAMESTATE.DIALOGUESTATE;
			entity.speak();
			
		}
		
	}
	
	
	//Saves the game progress
	public void save() {
		
		if(gp.ih.kPressed) {
			
			gp.sl.save();
			eventMaster.startDialogue(eventMaster, 0);
			
		}
		
	}
	
	
	//Heals the player
	public void rest() {
		
		if(gp.ih.kPressed) {
			
			gp.player.currentLife=gp.player.maxLife;
			gp.player.currentMana=gp.player.maxMana;
			eventMaster.startDialogue(eventMaster, 1);
			gp.playSound("1up");
			
		}
		
	}
	
	
	//Test event
	public void testEvent(GAMESTATE gameState) {
		
		eventMaster.startDialogue(eventMaster, 1);
		//eventRect[col][row].eventDone=true;
		canTouchEvent=false;
		
	}
	
	
	//Test event when pressing a key
	public void testEventWhenPressingAkey(int gameState) {
		
		if(gp.ih.kPressed) {
			
			eventMaster.startDialogue(eventMaster, 2);
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
