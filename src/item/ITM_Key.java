package item;

import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Key extends Entity{
	/*
	 * Key that opens doors
	 */
	
	GamePanel gp;
	public static final String itemName="key";
	
	
	public ITM_Key(GamePanel gp) {
		super(gp);
		
		this.gp=gp;
		this.name=itemName;
		this.description="A key that opens no door in the world";
		this.down1=setup("/items/shitAssKey.png");
		this.isPickable=true;
		this.type=TYPE.CONSUMABLE;
		this.price=5;
		this.stackable=true;
		setDialogue();
		
	}
	
	
	public void setDialogue() {
		
		dialogues[0][0]="You used the key! Did it taste good?";
		
		dialogues[1][0]="What are you doing?";
		
	}
	
	
	//Use key
	public boolean use(Entity entity) {
		
		boolean consume=false;
		gp.ui.playerSlotCol=0;
		gp.ui.playerSlotRow=0;
		
		//Open door
		int itemIndex=getDetected(entity, gp.items, "door");
		
		if(itemIndex!=1109) {
			
			gp.playSound("door");
			startDialogue(this, 0);
			gp.items[gp.currentMap][itemIndex]=null;
			consume=true;
			
		}else {
			
			startDialogue(this, 1);
			
		}

		
		return consume;
		
	}
	
}