package combat;

import java.util.ArrayList;
import java.util.Random;

import entity.Entity;
import entity.TYPE;
import main.GAMESTATE;
import main.GamePanel;
import skill.Skill;

public class CombatManager {

	public Entity currentEnemy;
	public CombatEntity currentEnemyRpg;
	
	GamePanel gp;
	
	public Entity combatMaster; //This is god. He speaks to the player directly. He is not real, like any god from any religion
	
	ActionResolver ar;
	ActionResult currentResult;
	
	public TURN currentTurn;
	FLOW currentFlow;
	
	CombatAction playerAction;
	CombatAction enemyAction;
	ArrayList<CombatAction> turnActions=new ArrayList<>();
	
	
	//Combat states
	public COMBATSTATE combatState;
	
	
	//Constructor
	public CombatManager(GamePanel gp) {
		
		this.combatMaster=new Entity(gp);
		this.gp=gp;
		this.ar=new ActionResolver(gp);
		this.combatState=COMBATSTATE.MAINOPTIONSSTATE;
		
	}	
	
	
	//Starts a battle
	public void startBattle(Entity currentEnemy) {
		
		this.currentEnemy=currentEnemy;
		gp.ui.pointer=0;
		gp.gameState=GAMESTATE.COMBATSTATE;
		currentTurn=TURN.PLAYERTURN; //Should change this and add speed calcs
		currentFlow=FLOW.COMBAT;
		
		combatMaster.dialogues[0][0]=currentEnemy.name + " wants to fight!";
		combatMaster.showCombatMessage(combatMaster, 0);
		
		gp.stopMusic();
		gp.playMusic("combat");
		
		this.currentEnemyRpg=gp.newCombatEntity(currentEnemy);
		gp.playerRpg=gp.newCombatEntity(gp.player);
		combatState=COMBATSTATE.MAINOPTIONSSTATE;
		
	}
	
	
	//Creates and stores a new action
	public void startAction(CombatEntity source, CombatEntity target, ACTIONTYPE type, Entity item, Skill skill) {
		
		CombatAction action=null;
		
		switch(type) {
		
			case ATTACK: action=new CombatAction(source, target, type); break;
			case ITEM: action=new CombatAction(source, target, type, item); break;
			case SKILL: action=new CombatAction(source, target, type, skill); break;
		
		}	
		
		if(currentTurn==TURN.PLAYERTURN) playerAction=action;
		else enemyAction=action;
		
	}
	
	
	//Resolves a singular action
	public void resolveAction(CombatAction action) {
		
		combatState=COMBATSTATE.DIALOGUESTATE;
		gp.ui.resetCombatDialogue();
		currentResult=ar.resolveAction(action);
		setDialogue(currentResult.dialogues);
		
	}
	
	
	//Starts all actions for this turn, resolving it
	public void resolveTurn() {
		
		turnActions.clear();
		TURNORDER order=determineTurnOrder();

	    if(order==TURNORDER.PLAYER_FIRST) {

	    	turnActions.add(playerAction);
			turnActions.add(enemyAction);

	    }else{

	    	turnActions.add(enemyAction);
			turnActions.add(playerAction);

	    }

	    playerAction=null;
	    enemyAction=null;

	    resolveNextAction();
		
	}
	
	
	// Resolves the next action in queue
	public void resolveNextAction() {

		// No more actions->new turn
		if(turnActions.isEmpty()) {

			startPlayerTurn();

		}else {
			
			CombatAction action=turnActions.remove(0);
			if(action.source.currentLife<=0) resolveNextAction();
			else resolveAction(action);
			
		}

	}
	
	
	//Determines the next turn's order
	public TURNORDER determineTurnOrder() {
		
	    if(gp.playerRpg.getSpeed()>currentEnemyRpg.getSpeed()) {

	        return TURNORDER.PLAYER_FIRST;

	    }

	    if(currentEnemyRpg.getSpeed()>gp.playerRpg.getSpeed()) {

	        return TURNORDER.ENEMY_FIRST;

	    }

	    // Same speed
	    Random random=new Random();
	    if(random.nextBoolean()) {

	        return TURNORDER.PLAYER_FIRST;

	    }

	    return TURNORDER.ENEMY_FIRST;
		
	}
	
	
	//Checks the current state of the battle and decides what happens next
	public void continueCombatFlow() {
		
		//Victory flow
		if(currentFlow==FLOW.VICTORY) {
			
			endCombat();
			//Should have exp and money rewards here
			
		//Defeat flow
		}else if(currentFlow==FLOW.DEFEAT) {
			
			endCombat();
			//Should have game over here
			
		}else {
			
			//Someone died
			if(currentResult.targetDied) {
				
				if(currentResult.target==gp.playerRpg) showDefeat();
				else showVictory();
				
			}else {
				
				//Continue action queue
				resolveNextAction();
				
			}
			
		}
				
	}
	
	
	//Starts the player turn
	public void startPlayerTurn() {
		
		resetEnemyDialogue();
		gp.player.getPlayerImage();
    	combatState=COMBATSTATE.MAINOPTIONSSTATE;
    	currentTurn=TURN.PLAYERTURN;
    	currentResult=null;
    	gp.ui.pointer=0;
		
	}
	
	
	//Starts the enemy turn
	public void chooseEnemyAction() {
		
	    currentTurn=TURN.ENEMYTURN;
	    startAction(currentEnemyRpg, gp.playerRpg, ACTIONTYPE.ATTACK, null, null);
		
	}
	
	
	//Sets the dialogues for the battle
	public void setDialogue(ArrayList<String> dialogue) {
		
		int index=0;
		
		while(index<dialogue.size()) {
			
			combatMaster.dialogues[0][index]=dialogue.get(index);
			index++;
			
		}
		
	}
	
	
	//Resets the enemy dialogue
	//Want to expand this in the future to get a message from a random pool
	public void resetEnemyDialogue() {
		
		combatMaster.dialogues[0][0]=currentEnemy.name + " wants to fight!";
		
	}
	
	
	//Passes to the next dialogue
	public void nextDialogue() {
		
		gp.ui.charIndex=0;
        gp.ui.combinedText="";
        gp.ui.currentDialogue="";

        combatMaster.dialogueIndex++;
        gp.ih.kPressed=false;
		
	}
	
	
	//Ends the dialogue state
	public void endDialogue() {
		
		combatMaster.dialogueIndex=0;	    
	    if(currentResult!=null) continueCombatFlow();
	    		
	}
	
	
	//Shows the victory screen for the player
	public void showVictory() {
		
		gp.ui.resetCombatDialogue();
		
		if(currentEnemy.type==TYPE.ENEMY) {
			
			showDeadEntity(currentEnemy);		
			
		}
		
		gp.music.stop();
		gp.music.play("victoryTheme2");
		
		combatMaster.dialogues[0][0]=currentEnemy.name + " was defeated!";
		combatMaster.dialogues[0][1]=gp.playerRpg.name + " won " + currentEnemy.exp + " exp and " + currentEnemy.money + "€";
		currentFlow=FLOW.VICTORY;
				
	}
	
	
	//Shows the defeat screen for the player
	public void showDefeat() {
		
		gp.ui.resetCombatDialogue();
		
		gp.music.stop();
		gp.music.play("defeat");
		
		combatMaster.dialogues[0][0]=gp.playerRpg.name + " collapsed...";
		currentFlow=FLOW.DEFEAT;
		
		showDeadEntity(gp.player);
		
	}
	
	
	//Ends a battle and reset all needed values
	public void endCombat() {
		
		gp.gameState=GAMESTATE.PLAYSTATE;
		gp.ui.pointer=0;
		gp.ui.previousPointer=0;
		
		gp.combatM.combatMaster.dialogues=new String[30][30];
		gp.ui.charIndex=0;
		gp.ui.combinedText="";
		
		if(currentFlow!=FLOW.DEFEAT)gp.playNextAreaMusic();
		
		combatOutcome();
		
	}
	
	
	//Aplies the combat changes to the overworld
	public void combatOutcome() {
				
		gp.player.currentLife=gp.playerRpg.currentLife;
		gp.player.currentMana=gp.playerRpg.currentMana;
		
		if(currentFlow==FLOW.VICTORY) {
			
			currentEnemy.defeated=true;
			gp.player.money+=currentEnemy.money;
			gp.player.exp+=currentEnemy.exp;
			gp.player.checkLevelUp();
			
		}else if(currentFlow==FLOW.DEFEAT) {
			
			gp.gameState=GAMESTATE.GAMEOVERSTATE;
			gp.player.money-=gp.player.money/2;
			
		}
				
	}
	
	
	//Changes entity attributes to show that it's dead
	public void showDeadEntity(Entity entity) {
		
		entity.up1=entity.setup("/enemies/dead.png");
		entity.up2=entity.setup("/enemies/dead.png");
		entity.right1=entity.setup("/enemies/dead.png");
		entity.right2=entity.setup("/enemies/dead.png");
		entity.left1=entity.setup("/enemies/dead.png");
		entity.left2=entity.setup("/enemies/dead.png");
		entity.down1=entity.setup("/enemies/dead.png");
		entity.down2=entity.setup("/enemies/dead.png");
		entity.name="RIP " + entity.name;	
		
	}
	
	
}


























