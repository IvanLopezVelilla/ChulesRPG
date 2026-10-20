package skill;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;


import combat.ActionResult;
import combat.CombatAction;
import main.GamePanel;
import main.UtilityTool;

public class Skill {

	GamePanel gp;
	public String name;
	public int value;
	public int cost;
	public BufferedImage image;
	public String description;
	
	public int type;
	public static final int damageType=0;
	public static final int healType=1;
	public static final int defenseType=2;
	
	public int status=0;
	public static final int poisonStatus=1;
	public static final int burntStatus=2;
	
	
	public Skill(GamePanel gp, String name, int value, int cost, BufferedImage image, String description, int type) {
		
		this.gp=gp;
		this.name=name;
		this.value=value;
		this.cost=cost;
		this.image=setup(image);
		this.description=description;
		this.type=type;
		
	}
	
	
	//Setups an image
	public BufferedImage setup(BufferedImage image) {
		
		UtilityTool ut=new UtilityTool();		
		image=ut.scaleImage(image, gp.tileSize, gp.tileSize);
		return image;
		
	}
	
	
	//Uses the skill in combat
	public ActionResult combatUse(CombatAction action, boolean enoughMana) {
		
		boolean targetDied=false;
		boolean canDo=enoughMana;

		if(!enoughMana) {

			canDo=new Random().nextBoolean()
				&& new Random().nextBoolean();

		}
		
		if(canDo) {
			
			//Will change in the future to create individual skill objects
			switch(type) {
			
				case damageType:
					
					//Calculate damage
					int damage=value-action.target.defense;
					if(damage<=0) damage=1;
					
					//Aply damage
					action.target.currentLife-=damage;
					if(action.target.currentLife<0) action.target.currentLife=0;
					
					targetDied=action.target.currentLife==0;
					
				break;
				
				case defenseType:
					
					action.source.defenseBoost.add(value);
					action.source.defenseBoost.add(value);
					action.source.defenseBoost.add(value);
					
				break; 
				
				case healType:
					
					action.source.currentLife+=value;
					if(action.source.currentLife>action.source.maxLife) {action.source.currentLife=action.source.maxLife;}
					
					gp.playSound("1up");
					
				break; 
			
			}			
			
		}
		
		action.source.currentMana-=action.skill.cost;
		if(action.source.currentMana<0) action.source.currentMana=0;
		ActionResult result=new ActionResult(action.actionType, action.source, action.target, value, targetDied, this);
		if(canDo && !enoughMana) result.luckySkill=true;
		if(!canDo && !enoughMana) result.notEnoughMana=true;
		result.generateDialogue();
		return result;
		
	}
	
	
	//Generate skill dialogues
	public void generateCombatDialogues(ActionResult actionResult, ArrayList<String> dialogues) {
		
		dialogues.add(actionResult.source.name + " used " + name + "!");
		if(actionResult.notEnoughMana) dialogues.add("Not enough mana!");
		if(actionResult.luckySkill) dialogues.add("Skill worked luckily!");
		
		if(!actionResult.notEnoughMana) {
			
			//Will change in the future to create individual skill objects
			switch(type) {
				
				case damageType:
					
					dialogues.add(actionResult.target.name + " received " + value + " damage!");
					
				break;
				
				case defenseType:
					
					dialogues.add(actionResult.source.name + " received a defense boost for 3 turns!");
					
				break; 
				
				case healType:
					
					dialogues.add(actionResult.source.name + " healed " + value + " HP!");
					
				break; 
			
			}			
			
		}
	
	}
	
	
}


















