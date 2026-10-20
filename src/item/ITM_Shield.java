package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Shield extends Entity{
	
	public static final String itemName="Normal Shield";

	public ITM_Shield(GamePanel gp) {
		super(gp);
		
		this.name=itemName;
		this.description="A shield that protects from no damage";
		this.down1=setup("/items/shield.png");
		this.itemDefense=2;
		this.isPickable=true;
		this.type=TYPE.SHIELD;
		this.price=1;
		
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
