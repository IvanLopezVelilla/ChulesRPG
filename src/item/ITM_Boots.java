package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Boots extends Entity{
	/*
	 * Boots that boost the player speed
	 */
	
	public static final String itemName="boots";
	
	
	public ITM_Boots(GamePanel gp) {
		super(gp);
		
		this.name=itemName;
		this.description="Boots that gives no speed";
		this.down1=setup("/items/shitAssBoot.png");
		this.isPickable=true;
		this.type=TYPE.SHOE;
		this.price=1;
		this.itemSpeed=3;
		
	}
	
	
	//Changes equipment
	public ActionResult combatUse(CombatAction action) {
		
		if(action.source==gp.playerRpg) {
			
			gp.playerRpg.currentShoes=this;
			gp.player.currentShoes=this;
			gp.player.rpgSpeed=gp.player.getSpeed();
			gp.playerRpg.rpgSpeed=gp.playerRpg.getSpeed();
			
		}
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add(result.source.name + " changed his shoes to " + name + "!");
		
	}
	
}