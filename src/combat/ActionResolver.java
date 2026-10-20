package combat;

import main.GamePanel;

public class ActionResolver {
	/*
	 * This class is the calculator of combat actions
	 */

	GamePanel gp;
	CombatManager combatM;
	
	
	public ActionResolver(GamePanel gp) {
		
		this.gp=gp;
		this.combatM=gp.combatM;
		
	}
	
	
	//Resolves an action and generates an actionResult
	public ActionResult resolveAction(CombatAction action) {
		
		//Check action type
		switch(action.actionType) {
		
			case ATTACK: return resolveAttack(action);
			case ITEM: return resolveItem(action);
			case SKILL: return resolveSkill(action);
		
		}
		
		return null;
		
	}
	
	
	//Resolves an attack type action
	public ActionResult resolveAttack(CombatAction action) {
		
		//Calculate damage
		int damage=action.source.attack-action.target.defense;
		
		//Check for defense boost
		if(action.target.defenseBoost.size()>0) {
			
			damage-=action.target.defenseBoost.remove(0);
			
		}
		
		if(damage<=0) damage=1;
		
		//Aply damage
		action.target.currentLife-=damage;
		if(action.target.currentLife<0) action.target.currentLife=0;
		
		boolean targetDied=action.target.currentLife==0;
		
		return new ActionResult(action.actionType, action.source, action.target, damage, targetDied);
		
	}
	
	
	//Resolves an item type action
	public ActionResult resolveItem(CombatAction action) {
		
		return action.item.combatUse(action);
		
	}
	
	
	//Resolves a skill type action
	public ActionResult resolveSkill(CombatAction action) {
		
		if(action.source.currentMana < action.skill.cost) return action.skill.combatUse(action, false);;
		return action.skill.combatUse(action, true);
		
	}
	
	
	
}























