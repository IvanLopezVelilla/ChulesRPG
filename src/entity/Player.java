package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import database.Asset;
import item.ITM_Shield;
import item.ITM_Sword;
import main.GAMESTATE;
import main.GamePanel;
import main.InputHandler;
import main.UtilityTool;
import skill.Skill;

public class Player extends Entity{
	
	InputHandler ih;
	
	public final int screenX;
	public final int screenY;
	
	public boolean lightUpdated=false; 
	
	//Player specific variables
	public boolean invincible=false; //Determines if the player can start a battle right now (either by touching an enemy or an enemy touching him
	public int invincibleCounter=0;
	
	//Growth rates for level up
	public int attackGrowth=3;
	public int defenseGrowth=2;
	public int speedGrowth=3;
	public int vitalityGrowth=4;
	public int manaGrowth=3;
	
	//Asset
	int assetIndex=-1;
	ArrayList<Asset> availablePlayers=new ArrayList<>();
	
	
	//Constructor
	public Player(GamePanel gp, InputHandler ih) {
		super(gp);
		
		this.ih=ih;
		
		this.screenX=gp.screenWidth/2-(gp.tileSize/2);
		this.screenY=gp.screenHeight/2-(gp.tileSize/2);
		
		solidArea=new Rectangle();
		solidArea.x=8;
		solidArea.y=16;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		solidArea.height=30;
		solidArea.width=30;
		hasCollisions=true;
		
		//Asset image
	    for(Asset asset : gp.db.assets) {

	        if(asset.category==1) {

	        	availablePlayers.add(asset);

	        }

	    }
		
		setDefaultValues();
		getPlayerImage();
		setItems();
		setSkills();
		setDialogues();
		
	}
	
	
	//Sets the default values of the player
	public void setDefaultValues() {
		
		//Default world position
		this.worldX=gp.tileSize*2; //2
		this.worldY=gp.tileSize*2; //2	
		
		//Default rpg stats
		this.name="You";
		this.level=1;
		this.maxLife=20;
		this.currentLife=maxLife;
		this.maxMana=30;
		this.currentMana=maxMana;
		this.naturalAttack=1; //The more natural attack he has, the more damage he deals
		this.naturalDefense=1; //The more natual defense he has, the less damage he takes
		this.naturalSpeed=1;
		this.money=20;
		this.exp=0;
		this.nextLevelExp=6;
		this.currentWeapon=new ITM_Sword(gp);
		this.currentShield=new ITM_Shield(gp);
		this.attack=getAttack(); //The total attack value is decided by naturalAttack + weapon
		this.defense=getDefense(); //The total defense value is decided by naturalDefense + shield
		this.rpgSpeed=getSpeed(); //The total speed value is decided by naturalSpeed + shoes
		this.worldSpeed=7;
		this.direction="down";
		
	}
	
	
	//Sets the player default positions
	public void setDefaultPositions() {
		
		this.worldX=gp.tileSize*2; //2
		this.worldY=gp.tileSize*2; //2
		this.direction="down";
		
	}
	
	
	//Restores player life and mana
	public void restoreLifeAndMana() {
		
		this.currentLife=maxLife;
		this.currentMana=maxMana;
		invincible=false;
		
	}
	
	
	//Sets player items on his inventory
	public void setItems() {
		
		inventory.clear();
		inventory.add(currentWeapon);
		inventory.add(currentShield);
		
	}
	
	
	//Sets player skills
	public void setSkills() {
		
		int maxSkills=10;
		ArrayList<Asset> availableSkills=new ArrayList<>();		
		for(Asset asset:gp.db.assets) if(asset.category==3) availableSkills.add(asset);
		
		while(skillSet.size()<maxSkills && availableSkills.size()>0) {
			
	        int randomIndex=(int)(Math.random()*availableSkills.size());
	        Asset asset=availableSkills.get(randomIndex);
	        availableSkills.remove(randomIndex);

	        skillSet.add(new Skill(gp,asset.name,asset.value,asset.cost,gp.db.loader.loadImage(asset.files.get(0).image),asset.description,asset.type));
			
		}
		
	}
	
	
	//Sets player dialogues
	public void setDialogues() {
		
		dialogueIndex=0;
		dialogueSet=0;
		
		//Level up
		dialogues[0][0]=name + " level is " + level + "!";
		
	}
	
	
	//Renders all the sprite for the player
	public void getPlayerImage() {
	    
	    if(availablePlayers.size()>0) {
	    	
	    	int index;
	    	
	    	if(assetIndex==-1) {
	    		
	    		index=(int)(Math.random()*availablePlayers.size());
	    		assetIndex=index;
	    		
	    	}else {
	    		
	    		index=assetIndex;
	    		
	    	}
	    	
	    	Asset player=availablePlayers.get(index);
	    	
	    	up1=setupAssetImage(gp.db.loader.loadImage(player.files.get(0).image));
			up2=setupAssetImage(gp.db.loader.loadImage(player.files.get(1).image));
			down1=setupAssetImage(gp.db.loader.loadImage(player.files.get(2).image));
			down2=setupAssetImage(gp.db.loader.loadImage(player.files.get(3).image));
			right1=setupAssetImage(gp.db.loader.loadImage(player.files.get(6).image));
			right2=setupAssetImage(gp.db.loader.loadImage(player.files.get(7).image));
			left1=setupAssetImage(gp.db.loader.loadImage(player.files.get(4).image));
			left2=setupAssetImage(gp.db.loader.loadImage(player.files.get(5).image));
			
			name=player.name;
	    	
	    }else {
	    	
	    	up1=setup("/player/up1.png");
			up2=setup("/player/up2.png");
			down1=setup("/player/down1.png");
			down2=setup("/player/down2.png");
			right1=setup("/player/right1.png");
			right2=setup("/player/right2.png");
			left1=setup("/player/left1.png");
			left2=setup("/player/left2.png");
	    	
	    }
	
	}
	
	
	//Setups an image from an asset
	public BufferedImage setupAssetImage(BufferedImage image) {
		
		UtilityTool ut=new UtilityTool();		
		image=ut.scaleImage(image, gp.tileSize, gp.tileSize);
		return image;
		
	}
	
	
	//Renders the sleeping image for the player
	public void getSleepingImage(BufferedImage image) {
		
		up1=image;
		up2=image;
		down1=image;
		down2=image;
		right1=image;
		right2=image;
		left1=image;
		left2=image;
		
	}
	
	
	//Returns the slot of the current weapon
	public int getCurrentWeaponSlot() {
		
		int currentWeaponSlot=0;
		for(int i=0;i<inventory.size();i++) {
			
			if(inventory.get(i)==currentWeapon) {
				
				currentWeaponSlot=i;
				
			}
			
		}
		
		return currentWeaponSlot;
		
	}
	
	
	//Returns the slot of the current shield
	public int getCurrentShieldSlot() {
		
		int currentShieldSlot=0;
		for(int i=0;i<inventory.size();i++) {
			
			if(inventory.get(i)==currentShield) {
				
				currentShieldSlot=i;
				
			}
			
		}
		
		return currentShieldSlot;
		
	}
	
	//Returns the slot of the current shoes
	public int getCurrentShoesSlot() {
		
		int currentShoesSlot=0;
		for(int i=0;i<inventory.size();i++) {
			
			if(inventory.get(i)==currentShoes) {
				
				currentShoesSlot=i;
				
			}
			
		}
		
		return currentShoesSlot;
		
	}
	
	//Returns the slot of the current light
	public int getCurrentLightSlot() {
		
		int currentLightSlot=0;
		for(int i=0;i<inventory.size();i++) {
			
			if(inventory.get(i)==currentLight) {
				
				currentLightSlot=i;
				
			}
			
		}
		
		return currentLightSlot;
		
	}
	
	
	//Updates the player information
	public void update() {
		
		//Player movement
		if(ih.upPressed || ih.downPressed || ih.rightPressed || ih.leftPressed
				|| ih.kPressed) {
			
			if(ih.upPressed) {direction="up";}
			if(ih.downPressed) {direction="down";}
			if(ih.rightPressed) {direction="right";}
			if(ih.leftPressed) {direction="left";}
			
			
			//Check if the player collided with a solid tile
			isColliding=false;
			gp.cm.checkTile(this);
			
			//Check if the player collided with an item
			int itemIndex=gp.cm.checkItem(this, true);
			processItem(itemIndex);//If the player collided with an item, process it
			
			//Check if the player collided with an entity
			int npcIndex=gp.cm.checkEntity(this, gp.npcs);
			processEntity(npcIndex); //If the player collided with an entity, process it
			
			//Check if the player collided with an enemy
			int enemyIndex=gp.cm.checkEntity(this, gp.enemies);
			processEnemy(enemyIndex);
			
			//Check if the player colided with an event tile
			gp.eh.checkEvent();
			
			//Check if the player collided with a solid tile
			checkCollision();			
			
			
			ih.kPressed=false;
			
		}else {
			
			//Maybe I can add an idle animation or just setting sprite to 1 here
			//I'm not sure how it will work with different animations
			
		}
		
		//Invincible counter
		if(invincible) {
			
			invincibleCounter++;
			if(invincibleCounter==60) {
				
				invincibleCounter=0;
				invincible=false;
				
			}
			
		}
		
		
	}
	

	//Manage the item interactions
	public void processItem(int itemIndex) {
		
		if(itemIndex!=1109) {
			
			//PICKUP ONLY ITEMS
			if(gp.items[gp.currentMap][itemIndex].type==TYPE.PICKUPITEM) {
				
				gp.items[gp.currentMap][itemIndex].use(this);
				gp.items[gp.currentMap][itemIndex]=null;
				
			}
			
			//OBSTACLE ITEM
			else if(gp.items[gp.currentMap][itemIndex].type==TYPE.OBSTACLE) {
				
				if(ih.kPressed) {
					
					gp.items[gp.currentMap][itemIndex].interact();
					
				}
				
			}
			
			else {
				
				//INVENTORY ITEMS
				if(gp.items[gp.currentMap][itemIndex].isPickable) {
				
					if(canObtainItem(gp.items[gp.currentMap][itemIndex])) {
						
						gp.playSound("coin");
						gp.items[gp.currentMap][itemIndex]=null;
						//Should add text here
						
					} else {
						
						//Should add text here
						
						//Should make it so you can't interact
						//with the same item for a brief time so
						//it doesn't spam full inventory message
						
					}
					
				}				
				
			}		

		}
		
	}
	
	
	//Manage the npc interactions
	public void processEntity(int npcIndex) {
		
		if(npcIndex!=1109) {
			
			if(gp.ih.kPressed && gp.npcs[gp.currentMap][npcIndex].type!=TYPE.ENEMY) {
				
				gp.npcs[gp.currentMap][npcIndex].speak();
							
			}
			
		}
		
	}
	
	
	//Manage the enemy interactions
	//NEEDS UPDATE
	public void processEnemy(int enemyIndex) {
		
		if(enemyIndex!=1109 && !invincible &&
				 gp.enemies[gp.currentMap][enemyIndex].type==TYPE.ENEMY &&
				 !gp.enemies[gp.currentMap][enemyIndex].defeated) {
			
			gp.combatM.startBattle(gp.enemies[gp.currentMap][enemyIndex]);
			invincible=true;
			
		}
		
	}
	
	
	//Manages the items when the player selects them on the inventory
	public void selectItem() {
		
		int itemIndex=gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
		
		if(itemIndex<inventory.size()) {
			
			Entity selectedItem=inventory.get(itemIndex);
			
			//Check item type			
			if(selectedItem.type==TYPE.WEAPON) {
				
				if(currentWeapon==selectedItem) {
					
					currentWeapon=null;
					
				}else {
					
					currentWeapon=selectedItem;
					
				}				
				attack=getAttack();
				gp.playSound("click");
				
			}
			
			if(selectedItem.type==TYPE.SHIELD) {
				
				if(currentShield==selectedItem) {
					
					currentShield=null;
					
				}else {
					
					currentShield=selectedItem;
					
				}
				defense=getDefense();
				gp.playSound("click");
				
			}
			
			if(selectedItem.type==TYPE.SHOE) {
				
				if(currentShoes==selectedItem) {
					
					currentShoes=null;
					
				}else {
					
					currentShoes=selectedItem;
					
				}
				rpgSpeed=getSpeed();
				gp.playSound("click");
				
			}
			
			if(selectedItem.type==TYPE.LIGHT) {
				
				gp.playSound("click");
				if(currentLight==selectedItem) {
					
					currentLight=null;
					
				}else {
					
					currentLight=selectedItem;
					
				}
				
				lightUpdated=true;
				
			}
			
			if(selectedItem.type==TYPE.CONSUMABLE) {
				
				boolean remove=selectedItem.use(this);
				
				//Remove item if neccesary
				if(remove) {
					
					if(selectedItem.amount>1) {
						
						selectedItem.amount--;
						
					}else {
						
						inventory.remove(itemIndex);
						
					}
					
				}
				
			}
			
		}
		
	}
	
	
	//Player specific collision check
	public void checkCollision() {
		
		//If collision is false, the entity can move
		if(!isColliding && !ih.kPressed) {
			
			switch(direction) {
			
				case "up": worldY-=worldSpeed; break;
				case "down": worldY+=worldSpeed; break;
				case "right": worldX+=worldSpeed; break;
				case "left": worldX-=worldSpeed; break;
			
			}
			
			//Change the sprite for walking animation
			if(gp.gameState==GAMESTATE.PLAYSTATE) {
				
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
		
		gp.ih.kPressed=false;
		
	}
	
	
	//Checks if the player has a certain item in his inventory
	public int searchItemInInventory(String itemName) {
		
		int itemIndex=1109;
		
		for(int i=0;i<inventory.size();i++) {
			
			if(inventory.get(i).name.equals(itemName)) {
				
				itemIndex=i;
				
			}
			
		}
		
		return itemIndex;
		
	}
	
	
	//Checks if player can obtain a certain item
	public boolean canObtainItem(Entity item) {
		
		Entity newItem=gp.eg.getItem(item.name);
		
		boolean canObtain=false;
		
		//Check if the item is stackable
		if(newItem.stackable) {
			
			int index=searchItemInInventory(newItem.name);
			
			if(index!=1109) {
				
				inventory.get(index).amount++;
				canObtain=true;
				
			}else { //New item, so need to check space
				
				if(inventory.size()!=maxInventorySize) {
					
					inventory.add(newItem);
					canObtain=true;
					
				}
				
			}
			
		}else { //Not stackable
			
			if(inventory.size()!=maxInventorySize) {
				
				inventory.add(newItem);
				canObtain=true;
				
			}
			
		}
		
		return canObtain;
		
	}
	
	
	//Checks if the player has leveled up
	public void checkLevelUp() {

		if(exp>=nextLevelExp) {

			level++;
			nextLevelExp=nextLevelExp + level*8;

			boolean bigLevel=false;
			int aux;

			if(level%5==0) {
				bigLevel=true;
			}
			
			dialogues[0][0]=name + " level is now " + level + "!";
			if(bigLevel) dialogues[0][0]+=" Oh baby!";

			aux=randomStat(attackGrowth, bigLevel);
			naturalAttack+=aux;
			dialogues[0][1]="Attack + " + aux;
			
			aux=randomStat(defenseGrowth, bigLevel);
			naturalDefense+=aux;
			dialogues[0][2]="Defense + " + aux;
			
			aux=randomStat(speedGrowth, bigLevel);
			naturalSpeed+=aux;
			dialogues[0][3]="Speed + " + aux;

			aux=randomStat(vitalityGrowth, bigLevel)*2;
			maxLife+=aux;
			dialogues[0][4]="HP + " + aux;
			
			aux=randomStat(manaGrowth, bigLevel);
			maxMana+=aux;
			dialogues[0][5]="MP + " + aux;

			attack=getAttack();
			defense=getDefense();
			rpgSpeed=getSpeed();
			
			gp.playSound("levelUp");
			startDialogue(this, 0);

		}

	}
	
	
	//Gets a random growth for a stat
	public int randomStat(int growth, boolean bigLevel) {

		int value=growth;
		Random random=new Random();

		if(bigLevel) {
			value+=2;
		}

		value+=random.nextInt(3)-1;

		if(value<1) {
			value=1;
		}

		return value;

	}
	
	
	//Draws the player on the screen
	public void draw(Graphics2D g2) {
		
		BufferedImage image=null;
		
		switch(direction){
			
			case "up":
				
				if(sprite==1) {
					image=up1;
				}
				if(sprite==2) {
					image=up2;
				}
				
			break;
			
			case "down":
				
				if(sprite==1) {
					image=down1;
				}
				if(sprite==2) {
					image=down2;
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
			
			case "left":
				
				if(sprite==1) {
					image=left1;
				}
				if(sprite==2) {
					image=left2;
				}
				
			break;
			
		}
		
		if(invincible) {
			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
			
		}
		
		
		int x=screenX;
		int y=screenY;
		
		if(screenX>worldX) {
			
			x=worldX;
			
		}
		if(screenY>worldY) {
			
			y=worldY;
			
		}
		int rightOffset=gp.screenWidth-screenX;
		if(rightOffset>gp.worldWidth-worldX) {
			
			x=gp.screenWidth-(gp.worldWidth-worldX);
			
		}
		int bottomOffset=gp.screenHeight-screenY;
		if(bottomOffset>gp.worldHeight-worldY) {
			
			y=gp.screenHeight-(gp.worldHeight-worldY);
			
		}
		
		
		g2.drawImage(image, x, y, null);		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		
		/*
		//HAS ERRORS, collision box doesn't align with the player when next to world limits
		//DEBUG, view player collision box
		g2.setColor(Color.red);
		g2.drawRect(screenX+solidArea.x, screenY+solidArea.y, solidArea.width, solidArea.height);
		
		//DEBUG, view the invincible counter
		g2.setFont(new Font("Arial", Font.PLAIN, 26));
		g2.setColor(Color.white);
		g2.drawString("Invincible: "+invincibleCounter+" invincible: "+invincible, 10, 400);
		*/
		
	}
	
}













