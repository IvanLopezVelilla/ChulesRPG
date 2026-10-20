package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Lantern extends Entity{
	
	public static final String itemName="Lantern";

	public ITM_Lantern(GamePanel gp) {
		super(gp);
		
		type=TYPE.LIGHT;
		name=itemName;
		down1=setup("/items/lantern.png");
		description="Source of light";
		price=8;
		lightRadius=250;
		isPickable=true;
		
	}
	
	//Changes equipment
	public ActionResult combatUse(CombatAction action) {
		
		if(action.source==gp.playerRpg) {
			
			gp.player.currentLight=this;
			gp.player.lightUpdated=true;
			
		}
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add(result.source.name + " changed his light to " + name + "!");
		dialogues.add("It did nothing...");
		
	}
	
	
}







































