package combat;

import java.util.ArrayList;

import entity.Entity;
import skill.Skill;

public class ActionResult {
	/*
	 * This class processes and carries all the information about and action
	 * that will be shown to the player in the UI
	 */
	
	public CombatEntity source;
	public CombatEntity target;
	int value;
	boolean targetDied;
	ArrayList<String> dialogues;
	ACTIONTYPE actionType;
	Entity item;
	Skill skill;
	
	//Specified values
	public boolean luckySkill=false;
	public boolean notEnoughMana=false;
	
	
	public ActionResult(ACTIONTYPE actionType, CombatEntity source, CombatEntity target, int value, boolean targetDied) {
		
		this.source=source;
		this.target=target;
		this.value=value;
		this.targetDied=targetDied;
		this.actionType=actionType;
		generateDialogue();
		
	}
	
	//Constructor with item
	public ActionResult(ACTIONTYPE actionType, CombatEntity source, CombatEntity target, int value, boolean targetDied, Entity item) {
		
		this.source=source;
		this.target=target;
		this.value=value;
		this.targetDied=targetDied;
		this.actionType=actionType;
		this.item=item;
		generateDialogue();
		
	}
	
	
	//Constructor with skill
	public ActionResult(ACTIONTYPE actionType, CombatEntity source, CombatEntity target, int value, boolean targetDied, Skill skill) {
		
		this.source=source;
		this.target=target;
		this.value=value;
		this.targetDied=targetDied;
		this.actionType=actionType;
		this.skill=skill;
		
	}
	
	
	//Generates the dialogues for the action
	public void generateDialogue() {
		
		dialogues=new ArrayList<>();
		
		switch(actionType) {
		
			case ATTACK: attackDialogue(); break;
			case ITEM: itemDialogue(dialogues); break;
			case SKILL: skillDialogue(dialogues); break;
		
		}
		
	}
	
	
	//Generates the dialogues for an attack type action
	public void attackDialogue() {
		
		dialogues.add(source.name + " attacked " + target.name + "!");
		dialogues.add(value + " of damage to " + target.name + "!");
		if(targetDied) dialogues.add(target.name + " died!"); //Check if target died
		
	}
	
	
	//Generates the dialogues for an attack type action
	public void itemDialogue(ArrayList<String> dialogues) {
		
		item.generateCombatDialogues(this, dialogues);
		
	}
	
	
	//Generates the dialogues for a skill type action
	public void skillDialogue(ArrayList<String> dialogues) {
		
		skill.generateCombatDialogues(this, dialogues);
		
	}
	
	
	
}

















