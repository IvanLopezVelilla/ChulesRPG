package entity;

import item.ITM_8potion;
import item.ITM_Axe;
import item.ITM_BetterShield;
import item.ITM_Boots;
import item.ITM_Lantern;
import item.ITM_Revolo;
import item.ITM_Tent;
import main.GAMESTATE;
import main.GamePanel;

public class NPC_HatCreature extends Entity{

	public NPC_HatCreature(GamePanel gp){
		super(gp);
		
		direction="down";
		worldSpeed=1;
		hasCollisions=true;

		getImage();
		setDialogue();
		setItems();
		
		//Solid area
		solidArea.x=0;
		solidArea.y=16;
		solidArea.width=48;
		solidArea.height=32;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		
	}
	
	
	//Renders all the sprite for the entity
	public void getImage() {
			
		up1=setup("/npc/hatCreature_down_1.png");
		up2=setup("/npc/hatCreature_down_2.png");
		down1=setup("/npc/hatCreature_down_1.png");
		down2=setup("/npc/hatCreature_down_2.png");
		right1=setup("/npc/hatCreature_down_1.png");
		right2=setup("/npc/hatCreature_down_2.png");
		left1=setup("/npc/hatCreature_down_1.png");
		left2=setup("/npc/hatCreature_down_2.png");
		
	}
	
	
	//Sets the oldman entity
	public void setDialogue() {
		
		dialogues[0][0]="You know the drill, I buy and sell things";
		dialogues[1][0]="Bye mate";
		dialogues[2][0]="Not enough € mate";
		dialogues[3][0]="You don't have space mate";
		dialogues[4][0]="Can't buy what you have equiped mate";
		
	}
	
	
	//Sets all the items that the entity will sell
	public void setItems() {
		
		inventory.add(new ITM_Axe(gp));
		inventory.add(new ITM_BetterShield(gp));
		inventory.add(new ITM_Boots(gp));
		inventory.add(new ITM_8potion(gp));
		inventory.add(new ITM_Tent(gp));
		inventory.add(new ITM_Lantern(gp));
		inventory.add(new ITM_Revolo(gp));
		
	}
	
	
	//Override to trade system
	public void speak() {
		
		gp.gameState=GAMESTATE.TRADESTATE;
		gp.ui.npc=this;	
		
	}
	
	
	//Sets charcater behavior
	public void setAction() {
				
		direction="";		
		
	}
	
	
	
	
}























