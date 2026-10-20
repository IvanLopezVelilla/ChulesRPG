package combat;

import entity.Entity;
import skill.Skill;

public class CombatAction {
	/*
	 * This class represents a combat action
	 * it does NOT carry the logic, logic will be processed in TurnManager
	 */

	public CombatEntity source;
	public CombatEntity target;
	public Entity item;	
	public Skill skill;
	
	//Action types
	public ACTIONTYPE actionType;
	
	
	public CombatAction(CombatEntity source, CombatEntity target, ACTIONTYPE type) {
		
		this.source=source;
		this.target=target;
		this.actionType=type;
		
	}
	
	
	//Combat actiopn with item
	public CombatAction(CombatEntity source, CombatEntity target, ACTIONTYPE type, Entity item) {
		
		this.source=source;
		this.target=target;
		this.actionType=type;
		this.item=item;
		
	}
	
	
	//Combat actiopn with skill
	public CombatAction(CombatEntity source, CombatEntity target, ACTIONTYPE type, Skill skill) {
		
		this.source=source;
		this.target=target;
		this.actionType=type;
		this.skill=skill;
		
	}
	
	
}






























