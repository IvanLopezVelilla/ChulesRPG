package entity;

import java.util.Random;

import main.GamePanel;

public class NPC_Oldman extends Entity{
	/*
	 * An old man
	 * Not an NPC that I like
	 * Only use him for coding the basics
	 * Want to change him as soon as posible
	 * He's ugly
	 */
	
	
	public NPC_Oldman(GamePanel gp) {
		super(gp);
		
		direction="down";
		worldSpeed=1;
		hasCollisions=true;
		this.name="Old man";

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
			
		up1=setup("/npc/oldman_up_1.png");
		up2=setup("/npc/oldman_up_2.png");
		down1=setup("/npc/oldman_down_1.png");
		down2=setup("/npc/oldman_down_2.png");
		right1=setup("/npc/oldman_right_1.png");
		right2=setup("/npc/oldman_right_2.png");
		left1=setup("/npc/oldman_left_1.png");
		left2=setup("/npc/oldman_left_2.png");
		
	}
	
	
	//Sets the oldman dialogues
	public void setDialogue() {
		
		dialogues[0][0]="Hello!";
		dialogues[0][1]="This game is still under development, more things\nare on the way!";
		
		dialogues[1][0]="Just so you know, every enemy and skill\nin this game comes from the website";
		dialogues[1][1]="That means that they were all created\nby people from the community";
		dialogues[1][2]="You can join them and upload your creations!\nThey would be on this game!";
		
		dialogues[3][0]="Enjoy this beta version!";
		
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

































