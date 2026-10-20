package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import combat.ActionResult;
import combat.CombatAction;
import main.GAMESTATE;
import main.GamePanel;
import main.UtilityTool;
import skill.Skill;

public class Entity {
	/*
	 * Class that contains all the shared variables for the entities of the game
	 */
	
	public GamePanel gp;
	
	public int worldX, worldY; //The tile where the entity appears
	public int worldSpeed; //The speed which the entity moves at
	
	public BufferedImage up1, up2, down1, down2, right1, right2, left1, left2; //The images of the entity
	public String direction="down"; //The current direction the entity is looking at
	
	//Sprite cicle management
	public int spriteCounter=0;
	public int sprite=1;
	
	public Rectangle solidArea=new Rectangle(0, 0, 45, 45); //The area that colides with collisionable tiles and entities
	public int solidAreaDefaultX, solidAreaDefaultY; //Default values of the solid area
	public boolean isColliding; //Determines if an entity is colliding with something
	public boolean hasCollisions; //Determines if an entity has collision
	public boolean onPath=false;
	
	public int actionCounter=0;
	
	public String dialogues[][]=new String[30][30]; //NPC dialogues
	public int dialogueIndex=0; //Current dialogue line
	public int dialogueSet=0;
	
	public BufferedImage image;
	public BufferedImage image2;
	public String name;
	public boolean collission=false;
	
	//Type of entity
	public TYPE type;
	
	//RPG stats
	public int maxLife;
	public int currentLife;
	public int maxMana;
	public int currentMana;
	public int level;
	public int naturalAttack; //Attack value without weapon
	public int naturalDefense; //Defense value without shield
	public int naturalSpeed;
	public int attack;
	public int defense;
	public int rpgSpeed;
	public int exp;
	public int nextLevelExp;
	public int money;
	public Entity currentWeapon;
	public Entity currentShield;
	public Entity currentShoes;
	public Entity currentLight;
	public boolean defeated=false;
	
	//Item attributes
	public int itemAttack; //Attack boost for weapon item types
	public int itemDefense; //Defense boost for shield item types
	public int itemSpeed; //Speed boost for shoe item types
	public String description="";
	public boolean isPickable=false;
	public int value;
	public ArrayList<Entity> inventory=new ArrayList<>();
	public final int maxInventorySize=20;
	public int price;
	public boolean stackable=false;
	public int amount=1;
	public int lightRadius; //Might delete, will be needed if more light sources added
	public Entity loot; //Chest
	public boolean opened=false; //Chest
	
	//Skills attributes
	public ArrayList<Skill> skillSet=new ArrayList<>();
	
	
	//Constructor
	public Entity(GamePanel gp) {
		
		this.gp=gp;
		
	}
	
	
	//Getters for solid area entity's position
	public int getLeftX() {return worldX+solidAreaDefaultX;}
	public int getRightX() {return worldX+solidAreaDefaultX+solidArea.width;}
	public int getTopY() {return worldY+solidAreaDefaultY;}
	public int getBottomY() {return worldY+solidAreaDefaultY+solidArea.height;}
	
	
	//Getters for current entity's col and row
	public int getCol() {return (worldX+solidAreaDefaultX)/gp.tileSize;}
	public int getRow() {return (worldY+solidAreaDefaultY)/gp.tileSize;}
	
	
	//Getters for chasing values
	public int getXdistance(Entity target) {return Math.abs(worldX-target.worldX);}
	public int getYdistance(Entity target) {return Math.abs(worldY-target.worldY);}
	public int getTileDistance(Entity target) {return (getXdistance(target)+getYdistance(target))/gp.tileSize;}
	public int getGoalCol(Entity target) {return target.getCol();}
	public int getGoalRow(Entity target) {return target.getRow();}
	
	
	//Returns the entity's attack (taking account his weapon)
	public int getAttack() {
		
		if(this.currentWeapon==null) {
			
			return this.naturalAttack;
			
		}
		
		return this.naturalAttack+this.currentWeapon.itemAttack;
		
	}
	
	
	//Returns the entity's defense (taking account his shield)
	public int getDefense() {
		
		if(this.currentShield==null) {
			
			return this.naturalDefense;
			
		}
		
		return this.naturalDefense+this.currentShield.itemDefense;
		
	}
	
	
	//Returns the entity's speed (taking account his shoes)
	public int getSpeed() {
		
		if(this.currentShoes==null) {
			
			return this.naturalSpeed;
			
		}
		
		return this.naturalSpeed+this.currentShoes.itemSpeed;
		
	}
	
	
	//Checks if it should stop chasing the target
	public void checkStopChasing(Entity target, int distance, int rate) {
		
		if(getTileDistance(target)>distance) {
			
			int i=new Random().nextInt(rate);
			if(i==0) {
				
				onPath=false;
				
			}
			
		}
		
	}
	
	
	//Checks if it should start chasing the target
	public void checkStartChasing(Entity target, int distance, int rate) {

		if(getTileDistance(target)<distance) {

			int i=new Random().nextInt(rate);
			
			if(i==0) {
				
				onPath=true;
				
			}
			
		}
		
	}
	
	
	//Gets a random direction to go next
	public void getRandomDirection() {
		
		actionCounter++;
		
		//Change action each 2 second
		if(actionCounter==120) {
			
			actionCounter=0;
			
			Random random=new Random();
			int i=random.nextInt(100);//Random int from 0 to 99 (I think)
			
			if(i<24) {direction="down";}
			else if(i>24 && i<49) {direction="up";}
			else if(i>49 && i<74) {direction="left";}
			else if(i>74 && i<99) {direction="right";}			
			actionCounter=0;
			
		}
		
	}
	
	
	//Manages the interaction between player and obstacleType entity
	public void interact() {}
	
	
	//Sets the loot for a chest item
	public void setLoot(Entity loot) {}
	
	
	//Makes the npc face to hte player's direction
	public void facePlayer() {
		
		//Change NPC direction so it faces the player
		switch(gp.player.direction) {
		
			case "up": direction="down"; break;
			case "down": direction="up"; break;			
			case "right": direction="left"; break;
			case "left": direction="right"; break;
		
		}
		
	}
	
	
	//Shows the NPC dialogue
	public void speak() {
					
		//Default for all entities
		facePlayer();
		startDialogue(this, dialogueSet);
		
		dialogueSet++;
		
		if(dialogues[dialogueSet][0]==null) {
			
			dialogueSet=0;
			
		}
		
	}
	
	
	//Starts a dialogue
	public void startDialogue(Entity entity, int setNumber) {
		
		gp.gameState=GAMESTATE.DIALOGUESTATE;
		gp.ui.npc=entity;
		dialogueSet=setNumber;
		
	}
	
	
	//Shows combat messages, similar to dialogues
	public void showCombatMessage(Entity entity, int setNumber) {
		
		gp.gameState=GAMESTATE.COMBATSTATE;
		gp.ui.npc=entity;
		dialogueSet=setNumber;
		
	}
	
	
	//Sets charcater behavior
	public void setAction() {
		
		if(this.type==TYPE.ENEMY) {
			
			if(onPath) {
				
				//Check if it stops chasing
				checkStopChasing(gp.player, 5, 100);
				
				//Search the direction to go
				worldSpeed=2;			
				searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
				
			}else {
				
				worldSpeed=1;
				
				//Check if it starts chasing
				checkStartChasing(gp.player, 5, 50);
				
				//Get a random direction
				getRandomDirection();
				
			}
			
		}else if(this.type==TYPE.NPC) {
			
			if(onPath) {
				
				int goalCol=15;
				int goalRow=19;
				worldSpeed=2;
				
				searchPath(goalCol, goalRow);
				
			}else {
				
				//Default actions for all entities
				actionCounter++;
				
				//Change action each 2 second
				if(actionCounter==120) {
					
					actionCounter=0;
					
					Random random=new Random();
					int i=random.nextInt(100);//Random int from 0 to 99 (I think)
					
					if(i<24) {
						
						direction="down";
						
					}else if(i>24 && i<49) {
						
						direction="up";
						
					}else if(i>49 && i<74) {
						
						direction="left";
						
					}else if(i>74 && i<99) {
						
						direction="right";
						
					}
					
				}			
				
			}
			
		}
		
	}
	
	
	//Updates charcater based on behaviour
	public void update() {
		
		setAction();
		
		checkAllCollisions();
		
		checkCollision();
		
	}	
	
	
	//Checks for all types of collision
	public void checkAllCollisions() {
		
		isColliding=false;
		gp.cm.checkTile(this);
		gp.cm.checkItem(this, false);
		gp.cm.checkEntity(this, gp.npcs);
		gp.cm.checkEntity(this, gp.enemies);
		boolean contactPlayer=gp.cm.checkPlayer(this);
		
		if(this.type==TYPE.ENEMY && contactPlayer && !gp.player.invincible) {
			
			gp.combatM.startBattle(this);
			gp.player.invincible=true;
			
		}
		
	}
	
	
	//Checks if the tile where the entity wants to move has collision
	public void checkCollision() {
		
		//If collision is false, the entity can move
		if(isColliding==false) {
			
			switch(direction) {
			
				case "up": worldY-=worldSpeed; break;
				case "down": worldY+=worldSpeed; break;
				case "right": worldX+=worldSpeed; break;
				case "left": worldX-=worldSpeed; break;
			
			}
			
			//Change the sprite for walking animation
			spriteCounter++;
			if(spriteCounter>40/worldSpeed) {
				
				if(sprite==1) {
					sprite=2;
				}else if(sprite==2) {
					sprite=1;
				}
				
				spriteCounter=0;
			}
			
		}
		
	}
	
	
	//Makes the entity follow a path to reach a goalTile
	public void searchPath(int goalCol, int goalRow) {
		
		int startCol=getCol();
		int startRow=getRow();
		
		gp.pf.setNodes(startCol, startRow, goalCol, goalRow);
		
		if(gp.pf.search()) {
			
			//Next worldX and worldY
			int nextX=gp.pf.pathList.get(0).col*gp.tileSize;
			int nextY=gp.pf.pathList.get(0).row*gp.tileSize;
			
			//Entity solidArea position
			int enLeftX=getLeftX();
			int enRightX=getRightX();
			int enTopY=getTopY();
			int enBottomY=getBottomY();
			
			if(enTopY > nextY && enLeftX >= nextX && enRightX < nextX+gp.tileSize) {
				
				direction="up";
				
			}
			
			else if(enTopY < nextY && enLeftX >= nextX && enRightX < nextX+gp.tileSize) {
				
				direction="down";
				
			}
			
			else if(enTopY >= nextY && enBottomY < nextY+gp.tileSize) {
				
				//Can go either left or right
				if(enLeftX > nextX) {
					
					direction="left";
										
				}
				
				if(enLeftX < nextX) {
					
					direction="right";
					
				}
				
			}
			
			else if(enTopY > nextY && enLeftX > nextX) {
				
				//Can go either up or left
				direction="up";
				checkAllCollisions();
				
				if(isColliding) {
					
					direction="left";
					isColliding=false;
					
				}
				
			}
			
			else if(enTopY > nextY && enLeftX < nextX) {
				
				//Can go either up or right
				direction="up";
				checkAllCollisions();
				
				if(isColliding) {
					
					direction="right";
					isColliding=false;
					
				}
				
			}
			
			else if(enTopY < nextY && enLeftX > nextX) {
				
				//Can go either down or left
				direction="down";
				checkAllCollisions();
				
				if(isColliding) {
					
					direction="left";
					isColliding=false;
					
				}
				
			}
			
			else if(enTopY < nextY && enLeftX < nextX) {
				
				//Can go either down or right
				direction="down";
				checkAllCollisions();
				
				if(isColliding) {
					
					direction="right";
					isColliding=false;
					
				}
				
			}
			
			//Stop the search when the goal is reached
			int nextCol=gp.pf.pathList.get(0).col;
			int nextRow=gp.pf.pathList.get(0).row;
			
			if(nextCol==goalCol && nextRow==goalRow) {
				
//				onPath=false;
				
			}
			
		}
		
	}
	
	
	//
	public int getDetected(Entity user, Entity target[][], String targetName) {
		
		int index=1109;
		
		//Check surrounding objects
		int nextWorldX=user.getLeftX();
		int nextWorldY=user.getTopY();
		
		switch(user.direction) {
		
			case "up": nextWorldY=user.getTopY()-user.worldSpeed; break;
			case "down": nextWorldY=user.getBottomY()+user.worldSpeed; break;
			case "left": nextWorldX=user.getLeftX()-user.worldSpeed; break;
			case "right": nextWorldX=user.getRightX()+user.worldSpeed; break;
		
		}
		
		int col=nextWorldX/gp.tileSize;
		int row=nextWorldY/gp.tileSize;
		
		for(int i=0;i<target[gp.currentMap].length;i++) {
			
			if(target[gp.currentMap][i]!=null) {
				
				if(target[gp.currentMap][i].getCol()==col &&
						target[gp.currentMap][i].getRow()==row &&
						target[gp.currentMap][i].name.equals(targetName)) {
					
					index=i;
					break; //would like to not use this break
					
				}
				
			}
			
		}		
		
		return index;
		
	}
	
	//Use item in overworld
	public boolean use() {return false;}	
	
	//Use item in combat
	public ActionResult combatUse(CombatAction action) {
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	//Generate item dialogues
	public void generateCombatDialogues(ActionResult actionResult, ArrayList<String> dialogues) {
		
		dialogues.add(name + " did nothing!");
		
	}
	
	
	//Draw method
	public void draw(Graphics2D g2) {
		  
		BufferedImage image = null;
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		// STOP MOVING CAMERA
		if(gp.player.worldX < gp.player.screenX) {
			  
			screenX = worldX;
		   
		 }
		if(gp.player.worldY < gp.player.screenY) {
			  
			screenY = worldY;
		   
		}   
		int rightOffset = gp.screenWidth - gp.player.screenX;      
		if(rightOffset > gp.worldWidth - gp.player.worldX) {
			
			screenX = gp.screenWidth - (gp.worldWidth - worldX);
		
		} 
		int bottomOffset = gp.screenHeight - gp.player.screenY;
		if(bottomOffset > gp.worldHeight - gp.player.worldY) {
			  
			screenY = gp.screenHeight - (gp.worldHeight - worldY);
		   
		}
		  
		switch(direction) {
		  
			case "up":
				if(sprite==1) {
					   
					image=up1;
				    
				}
				   
				if(sprite==2) {
					   
					image=up2;
				    
				}
			break;
				   
			case "down":
			case "":
				if(sprite==1) {
					   
					image=down1;
				    
				}
				   
				if(sprite==2) {
					   
					image=down2;
				    
				}
				   
			break;
				   
			case "left":
				if(sprite==1) {
					   
					image=left1;    
				    
				}   
				   
				if(sprite==2) {
					   
					image=left2;
				    
				}
				   
			break;
				   
			case "right":
					  
				if(sprite==1) {
					   
					image=right1;
				    
				}   
				   
				if(sprite==2) {
					   
					image=right2;
				    
				}
				   
			break;
		   
		}
		  
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
				worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) { 
			  
			g2.drawImage(image, screenX, screenY, null);
		   
		}
		// If player is around the edge, draw everything
		else if(gp.player.worldX < gp.player.screenX ||
				gp.player.worldY < gp.player.screenY ||
				rightOffset > gp.worldWidth - gp.player.worldX ||
				bottomOffset > gp.worldHeight - gp.player.worldY) {
			  
			g2.drawImage(image, screenX, screenY, null); 
		   
		}
		  
	}
	
	
	//Setups the entity images by scaling it to tileSize
	public BufferedImage setup(String imagePath) {
		
		UtilityTool ut=new UtilityTool();
		BufferedImage image=null;
		
		try {
			
			image=ImageIO.read(getClass().getResourceAsStream(imagePath));
			image=ut.scaleImage(image, gp.tileSize, gp.tileSize);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;		
		
	}
	
	
	//Setups the player images by scaling it to preferred size
	public BufferedImage setup(String imagePath, int x, int y) {
		
		UtilityTool ut=new UtilityTool();
		BufferedImage image=null;
		
		try {
			
			image=ImageIO.read(getClass().getResourceAsStream(imagePath));
			image=ut.scaleImage(image, x, y);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;		
		
	}
	
	
	//Method for item type entities to be used
	/*
	 * If item should be consumed after using, return true
	 * Keys, for example, only consumes if door opened
	 */
	public boolean use(Entity entity) {return false;}
	
}

























