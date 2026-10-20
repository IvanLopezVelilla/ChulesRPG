package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Sword extends Entity{
	
	public static final String itemName="Normal Sword";

	public ITM_Sword(GamePanel gp) {
		super(gp);
		
		this.name=itemName;
		this.description="A sword that deals no damage";
		this.down1=setup("/items/sword.png");
		this.itemAttack=2;
		this.isPickable=true;
		this.type=TYPE.WEAPON;
		this.price=1;
		
	}
	
	
	//Changes equipment
	public ActionResult combatUse(CombatAction action) {
		
		if(action.source==gp.playerRpg) {
			
			gp.playerRpg.currentWeapon=this;
			gp.player.currentWeapon=this;
			gp.player.attack=gp.player.getAttack();
			gp.playerRpg.attack=gp.playerRpg.getAttack();
			
		}
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add(result.source.name + " changed his weapon to " + name + "!");
		
	}

	
	
}
