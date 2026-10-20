package item;

import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Door extends Entity{
	/*
	 * Door that may be opened with a key
	 */
	
	public static final String itemName="door";
	
	public ITM_Door(GamePanel gp) {
		super(gp);
		
		this.name=itemName;
		this.description="A door with no key to be opened";
		this.down1=setup("/items/shitAssDoor.png");	
		hasCollisions=true;
		this.type=TYPE.OBSTACLE;
		setDialogue();
		
		//SOLID AREA may change it
		solidArea.x=0;
		solidArea.y=16;
		solidArea.width=38;
		solidArea.height=32;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		
	}
	
	
	public void setDialogue() {
		
		dialogues[0][0]="You need a key to open this door, genious";
		
	}
	
	
	public void interact() {
		
		startDialogue(this, 0);
		
	}
	
	
}





























