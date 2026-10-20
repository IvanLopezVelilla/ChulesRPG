package item;

import java.util.ArrayList;

import combat.ActionResult;
import combat.CombatAction;
import entity.Entity;
import entity.TYPE;
import main.GAMESTATE;
import main.GamePanel;

public class ITM_Tent extends Entity{
	
	public static final String itemName="Tent";

	GamePanel gp;
	
	public ITM_Tent(GamePanel gp) {
		super(gp);
		
		this.gp=gp;
		this.value=15;
		type=TYPE.CONSUMABLE;
		name=itemName;
		down1=setup("/items/tent.png");
		description="You can live here without paying a house";
		price=6;
		stackable=true;
		
	}
	
	
	//Changes the current dayState to dawn
	public boolean use(Entity entity) {
		
		gp.gameState=GAMESTATE.SLEEPSTATE;
		gp.playSound("sleep");
		gp.player.getSleepingImage(down1);
		gp.music.pause();
		gp.ui.pointer=0;
		return true;
		
	}
	
	
	//Heals the user in combat
	public ActionResult combatUse(CombatAction action) {
		
		action.source.currentLife+=value;
		if(action.source.currentLife>action.source.maxLife) {action.source.currentLife=action.source.maxLife;}
		
		gp.playSound("sleep");
		gp.player.getSleepingImage(down1);
		
		return new ActionResult(action.actionType, action.source, action.target, value, false, this);
		
	}
	
	
	//Generates combat dialogues
	public void generateCombatDialogues(ActionResult result, ArrayList<String> dialogues) {
		
		dialogues.add(result.source.name + " sleeped and healed " + value + " HP!");
		
	}
	
	
}

















