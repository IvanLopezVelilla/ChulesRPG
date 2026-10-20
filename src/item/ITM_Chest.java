package item;


import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Chest extends Entity{
	/*
	 * Chest that may be opened with a key
	 */
	
	GamePanel gp;
	public static final String itemName="chest";
	
	public ITM_Chest(GamePanel gp) {
		super(gp);
		this.gp=gp;
		
		this.name=itemName;
		this.description="A chest with no reward";
		this.type=TYPE.OBSTACLE;
		this.image=setup("/items/shitAssChest.png");
		this.image2=setup("/items/Chest_Open.png");
		down1=image;
		hasCollisions=true;
		
		solidArea.x=4;
		solidArea.y=16;
		solidArea.width=38;
		solidArea.height=32;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		
	}
	
	
	public void setLoot(Entity loot) {
		
		this.loot=loot;
		this.opened=false;
		this.down1=image;
		
		setDialogue();
		
	}
	
	
	public void setDialogue() {
		
		dialogues[0][0]="You open the chest and find a " + loot.name + "!\n... But you cannot carry any more!";
		dialogues[1][0]="You open the chest and find a " + loot.name + "!\n... You obtained the " + loot.name +"!";
		dialogues[2][0]="Nothing to see here";
		
	}
	
	
	//Checks the chest loop and if the player can take it
	public void interact() {
		
		if(!opened) {
			
			//Inventory full
			if(!gp.player.canObtainItem(loot)) {
				
				startDialogue(this, 0);
				
			}else {
				
				gp.playSound("door");
				startDialogue(this, 1);
				down1=image2;
				opened=true;
				
			}
			
		}else {
			
			startDialogue(this, 2);
			
		}
		
	}
	
	
}






















