package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_8potion extends Entity{
	/*
	 * Potion with an 8
	 */
	
	GamePanel gp;
	public static final String itemName="8 Potion";
	
	public ITM_8potion(GamePanel gp) {
		super(gp);
		
		this.gp=gp;
		this.name=itemName;
		this.description="A potion that gives no power. Heals "+value+" hp";
		this.down1=setup("/items/8potion.png");
		this.isPickable=true;
		this.type=TYPE.CONSUMABLE;
		this.value=5;
		this.price=5;
		this.stackable=true;
		setDialogue();
		
	}
	
	
	public void setDialogue() {
		
		dialogues[0][0]="You drinked the "+name+"!\nYou recovered "+value+ "hp";
		
	}
	
	
	//Cure the entity using the item
	public boolean use(Entity entity) {
		
		startDialogue(this, 0);
		
		entity.currentLife+=value;
		if(entity.currentLife>entity.maxLife) {entity.currentLife=entity.maxLife;}
		
		gp.playSound("1up");
		
		gp.ui.playerSlotCol=0;
		gp.ui.playerSlotRow=0;
		
		return true;
		
	}
	
	
	//Heals the user in combat
	public ActionResult combatUse(CombatAction action) {
		
		action.source.currentLife+=value;
		if(action.source.currentLife>action.source.maxLife) {action.source.currentLife=action.source.maxLife;}
		
		gp.playSound("1up");
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add(result.source.name + " healed " + value + " HP!");
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}