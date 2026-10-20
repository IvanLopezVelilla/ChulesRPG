package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_BetterShield extends Entity{
	
	public static final String itemName="Better Shield";

	public ITM_BetterShield(GamePanel gp) {
		super(gp);
		
		this.name=itemName;
		this.description="A shield that protects from little damage";
		this.down1=setup("/items/betterShield.png");
		this.itemDefense=4;
		this.isPickable=true;
		this.type=TYPE.SHIELD;
		this.price=6;
		
	}
	
	
	//Changes equipment
	public ActionResult combatUse(CombatAction action) {
		
		if(action.source==gp.playerRpg) {
			
			gp.playerRpg.currentShield=this;
			gp.player.currentShield=this;
			gp.player.defense=gp.player.getDefense();
			gp.playerRpg.defense=gp.playerRpg.getDefense();
			
		}
		
		action.source.currentMana+=action.source.maxMana/2;
		if(action.source.currentMana>action.source.maxMana) action.source.currentMana=action.source.maxMana;
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add(result.source.name + " changed his shield to " + name + "!");
		dialogues.add(result.source.name + " recovered mana!");
		
	}
	
	
}





