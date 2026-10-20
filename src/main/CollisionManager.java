package main;

import entity.Entity;
import entity.TYPE;

public class CollisionManager {
	/*
	 * The manager for the collision system
	 */
	
	GamePanel gp;

	//Constructor
	public CollisionManager(GamePanel gp) {
		
		this.gp=gp;
		
	}
	
	
	//Checks the tile an entity is moving to and checks if it's solid
	public void checkTile(Entity entity) {
		
		//Variables of the collision area of the entity
		int collisionLeft=entity.worldX+entity.solidArea.x;
		int collisionRight=entity.worldX+entity.solidArea.x+entity.solidArea.width;
		int collisionUp=entity.worldY+entity.solidArea.y;
		int collisionDown=entity.worldY+entity.solidArea.y+entity.solidArea.height;
		
		//Position of each tile surrounding the entity
		int tileLeft=collisionLeft/gp.tileSize;
		int tileRight=collisionRight/gp.tileSize;
		int tileUp=collisionUp/gp.tileSize;
		int tileDown=collisionDown/gp.tileSize;
		
		//Tiles making contact with the entity
		int tileNum1, tileNum2;
		
		switch(entity.direction) {
		
			case "up":
				
				//Get the right and left tiles above the entity
				tileUp=(collisionUp-entity.worldSpeed)/gp.tileSize;
				tileNum1=gp.tm.mapTile[gp.currentMap][tileLeft][tileUp];
				tileNum2=gp.tm.mapTile[gp.currentMap][tileRight][tileUp];
				
				//If one of the 2 tiles has collisions, set entity collision true
				if(gp.tm.tile[tileNum1].collision==true || gp.tm.tile[tileNum2].collision==true) {
					
					entity.isColliding=true;
					
				}
				
			break;
			
			case "down":
				
				//Get the right and left tiles under the entity
				tileDown=(collisionDown+entity.worldSpeed)/gp.tileSize;
				tileNum1=gp.tm.mapTile[gp.currentMap][tileLeft][tileDown];
				tileNum2=gp.tm.mapTile[gp.currentMap][tileRight][tileDown];
				
				//If one of the 2 tiles has collisions, set entity collision true
				if(gp.tm.tile[tileNum1].collision==true || gp.tm.tile[tileNum2].collision==true) {
					
					entity.isColliding=true;
					
				}
				
			break;
			
			case "right":
				
				//Get the up and down tiles to the entity's right
				tileRight=(collisionRight+entity.worldSpeed)/gp.tileSize;
				tileNum1=gp.tm.mapTile[gp.currentMap][tileRight][tileUp];
				tileNum2=gp.tm.mapTile[gp.currentMap][tileRight][tileDown];
				
				//If one of the 2 tiles has collisions, set entity collision true
				if(gp.tm.tile[tileNum1].collision==true || gp.tm.tile[tileNum2].collision==true) {
					
					entity.isColliding=true;
					
				}
				
			break;
			
			case "left":
				
				//Get the up and down tiles to the entity's left
				tileLeft=(collisionLeft-entity.worldSpeed)/gp.tileSize;
				tileNum1=gp.tm.mapTile[gp.currentMap][tileLeft][tileUp];
				tileNum2=gp.tm.mapTile[gp.currentMap][tileLeft][tileDown];
				
				//If one of the 2 tiles has collisions, set entity collision true
				if(gp.tm.tile[tileNum1].collision==true || gp.tm.tile[tileNum2].collision==true) {
					
					entity.isColliding=true;
					
				}
				
			break;
		
		}
		
	}
	
	
	//Checks the item an entity is moving to and checks if it's solid
	//If it's the player, manage item interactions
	public int checkItem(Entity entity, boolean player) {
		
		int index=1109;
		
		for(int i=0;i<gp.items[1].length;i++) {
			
			if(gp.items[gp.currentMap][i]!=null) {
				
				//Get entity's solid area
				entity.solidArea.x=entity.worldX+entity.solidArea.x;
				entity.solidArea.y=entity.worldY+entity.solidArea.y;
				
				//Get item's solid area
				gp.items[gp.currentMap][i].solidArea.x=gp.items[gp.currentMap][i].worldX+gp.items[gp.currentMap][i].solidArea.x;
				gp.items[gp.currentMap][i].solidArea.y=gp.items[gp.currentMap][i].worldY+gp.items[gp.currentMap][i].solidArea.y;
				
				//Check the entity's movement
				switch(entity.direction) {
				
					case "up": entity.solidArea.y-=entity.worldSpeed; break;					
					case "down": entity.solidArea.y+=entity.worldSpeed; break;					
					case "right": entity.solidArea.x+=entity.worldSpeed; break;					
					case "left": entity.solidArea.x-=entity.worldSpeed; break;
				
				}
				
				if(entity.solidArea.intersects(gp.items[gp.currentMap][i].solidArea)) {
					
					//Check if the item has collisions
					if(gp.items[gp.currentMap][i].hasCollisions) {
						
						entity.isColliding=true;
						
					}
					
					//Check if entity is the player, in which case return the item's index
					if(player) {
						
						index=i;
						
					}
					
				}
				
				//Reset solid area values for entity and item
				entity.solidArea.x=entity.solidAreaDefaultX;
				entity.solidArea.y=entity.solidAreaDefaultY;
				
				gp.items[gp.currentMap][i].solidArea.x=gp.items[gp.currentMap][i].solidAreaDefaultX;
				gp.items[gp.currentMap][i].solidArea.y=gp.items[gp.currentMap][i].solidAreaDefaultY;
				
			}
			
		}
		
		return index;
		
	}
	
	
	//Checks the entity another entity is moving to and checks if it's solid
	public int checkEntity(Entity entity, Entity[][] target) {
		
		int index=1109;
		
		for(int i=0;i<target[1].length;i++) {
			
			if(target[gp.currentMap][i]!=null) {
				
				//Don't check for defeated entities
				if(target[gp.currentMap][i].type==TYPE.ENEMY && target[gp.currentMap][i].defeated) return index;
				
				//Get entity's solid area
				entity.solidArea.x=entity.worldX+entity.solidArea.x;
				entity.solidArea.y=entity.worldY+entity.solidArea.y;
				
				//Get second entity's solid area
				target[gp.currentMap][i].solidArea.x=target[gp.currentMap][i].worldX+target[gp.currentMap][i].solidArea.x;
				target[gp.currentMap][i].solidArea.y=target[gp.currentMap][i].worldY+target[gp.currentMap][i].solidArea.y;
				
				//Check the entity's movement
				switch(entity.direction) {
				
					case "up": entity.solidArea.y-=entity.worldSpeed; break;					
					case "down": entity.solidArea.y+=entity.worldSpeed; break;					
					case "right": entity.solidArea.x+=entity.worldSpeed; break;					
					case "left": entity.solidArea.x-=entity.worldSpeed; break;
				
				}
				
				if(entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
					
					//Check if the second entity's has collisions
					if(target[gp.currentMap][i].hasCollisions && target[gp.currentMap][i]!=entity) {
						
						entity.isColliding=true;
						
					}
						
					index=i;
													
				}
				
				//Reset solid area values for entity and second entity's
				entity.solidArea.x=entity.solidAreaDefaultX;
				entity.solidArea.y=entity.solidAreaDefaultY;
				
				target[gp.currentMap][i].solidArea.x=target[gp.currentMap][i].solidAreaDefaultX;
				target[gp.currentMap][i].solidArea.y=target[gp.currentMap][i].solidAreaDefaultY;
				
			}
			
		}
		
		return index;
		
	}
	
	
	//Checks if an entity is moving to the player
	public boolean checkPlayer(Entity entity) {
		
		boolean contactPlayer=false;
						
		//Get entity's solid area
		entity.solidArea.x=entity.worldX+entity.solidArea.x;
		entity.solidArea.y=entity.worldY+entity.solidArea.y;
		
		//Get second entity's solid area
		gp.player.solidArea.x=gp.player.worldX+gp.player.solidArea.x;
		gp.player.solidArea.y=gp.player.worldY+gp.player.solidArea.y;
		
		//Check the entity's movement
		switch(entity.direction) {
		
			case "up": entity.solidArea.y-=entity.worldSpeed; break;			
			case "down": entity.solidArea.y+=entity.worldSpeed; break;			
			case "right": entity.solidArea.x+=entity.worldSpeed; break;			
			case "left": entity.solidArea.x-=entity.worldSpeed; break;		
		}
		
		if(entity.solidArea.intersects(gp.player.solidArea)) {
			
			//Check if the second entity's has collisions
			if(gp.player.hasCollisions) {
				
				entity.isColliding=true;
				contactPlayer=true;
				
			}					
		}
		
		//Reset solid area values for entity and second entity's
		entity.solidArea.x=entity.solidAreaDefaultX;
		entity.solidArea.y=entity.solidAreaDefaultY;
		
		gp.player.solidArea.x=gp.player.solidAreaDefaultX;
		gp.player.solidArea.y=gp.player.solidAreaDefaultY;
				
		return contactPlayer;
		
	}
	
	
}



















