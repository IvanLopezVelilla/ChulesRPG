package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Vega extends Entity{
	
	public NPC_Vega(GamePanel gp) {
		super(gp);
		
		direction="down";
		worldSpeed=1;
		hasCollisions=true;
		this.name="Vega";

		getImage();
		setDialogue();
		this.dialogueSet=-1;
		
		//SOLID AREA may change it
		solidArea.x=8;
		solidArea.y=16;
		solidAreaDefaultX=solidArea.x;
		solidAreaDefaultY=solidArea.y;
		solidArea.height=32;
		solidArea.width=32;
		
	}
	
	
	//Renders all the sprite for the oldman
	public void getImage() {
			
		up1=setup("/npc/vegaDown1.png");
		up2=setup("/npc/vegaDown2.png");
		down1=setup("/npc/vegaDown1.png");
		down2=setup("/npc/vegaDown2.png");
		right1=setup("/npc/vegaDown1.png");
		right2=setup("/npc/vegaDown2.png");
		left1=setup("/npc/vegaDown1.png");
		left2=setup("/npc/vegaDown2.png");
		
	}
	
	
	//Sets the oldman dialogues
	public void setDialogue() {
		
		dialogues[0][0]="¡Hola! Soy Vega, ¡y molo un montón! c:";
		
	}
	
	
	//Example of specific actions for an specific NPC when speaking
	public void speak() {
		
		facePlayer();
		startDialogue(this, dialogueSet);
		
		dialogueSet++;
		
		if(dialogues[dialogueSet][0]==null) {
			
			dialogueSet=0;
			
		}
		
//		onPath=true;
		
	}
	
	
	//Character specific actions
	public void setAction() {
			
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

































