package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Revolo extends Entity{
	/*
	 * Revolo
	 */
	
	GamePanel gp;
	public static final String itemName="Revolo";
	
	public ITM_Revolo(GamePanel gp) {
		super(gp);
		
		this.gp=gp;
		this.name=itemName;
		this.description="Revolo";
		this.down1=setup("/items/revolo.png");
		this.isPickable=true;
		this.type=TYPE.CONSUMABLE;
		this.price=1;
		this.stackable=true;
		setDialogue();
		
	}
	
	
	public void setDialogue() {
		
		dialogues[0][0]="Revolo!";	
		
	}
	
	
	//Revolo
	public boolean use(Entity entity) {
		
		startDialogue(this, 0);
		
		gp.playSound("door");
		
		gp.ui.playerSlotCol=0;
		gp.ui.playerSlotRow=0;
		
		return true;
		
	}
	
	
	//Revolo does no thing
	public ActionResult combatUse(CombatAction action) {
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add("Revolo attacks!");
		dialogues.add("Revolo attacks...");
		dialogues.add("Revolo did nothing");
		dialogues.add("You lost 20 sanity points");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}