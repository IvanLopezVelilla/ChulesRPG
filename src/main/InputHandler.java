package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import combat.ACTIONTYPE;
import combat.COMBATSTATE;
import combat.TURN;
import entity.Entity;
import entity.TYPE;
import skill.Skill;

public class InputHandler implements KeyListener{
	/*
	 * Handles the player inputs in all game states
	 */
	
	public void keyTyped(KeyEvent e){} //No use in this game
	
	//Variables
	public boolean upPressed, downPressed, rightPressed, leftPressed, kPressed;
	public boolean showDebugText=false;
	GamePanel gp;
	
	
	//Constructor
	public InputHandler(GamePanel gp) {
		
		this.gp=gp;
		
	}
		
	
	//Sets all directions to a boolean
	private void setAllDirections(boolean value) {
		
		upPressed=value;
		downPressed=value;
		rightPressed=value;
		leftPressed=value;
		
	}
	
	
	//Reads an input from the player
	public void keyPressed(KeyEvent e) {
		
		int keyCode=e.getKeyCode();
		
		
		//TITLE STATE
		if(gp.gameState==GAMESTATE.TITLESTATE) titleState(keyCode);
		
		//PLAY STATE
		if(gp.gameState==GAMESTATE.PLAYSTATE) playState(keyCode);
		
		//PAUSE STATE
		else if(gp.gameState==GAMESTATE.PAUSESTATE) pauseState(keyCode);	
		
		//DIALOGUE STATE
		else if(gp.gameState==GAMESTATE.DIALOGUESTATE) dialogueState(keyCode);
		
		//MENU STATE
		else if(gp.gameState==GAMESTATE.PLAYERMENUSTATE) playerMenuState(keyCode);	
		
		//OPTIONS STATE
		else if(gp.gameState==GAMESTATE.OPTIONSSTATE) optionsState(keyCode);
		
		//TRADE STATE
		else if(gp.gameState==GAMESTATE.TRADESTATE) tradeState(keyCode);
		
		//MAP STATE
		else if(gp.gameState==GAMESTATE.MAPSTATE) mapState(keyCode);
		
		//COMBAT STATE
		else if(gp.gameState==GAMESTATE.COMBATSTATE) combatState(keyCode);
		
		//GAME OVER STATE
		else if(gp.gameState==GAMESTATE.GAMEOVERSTATE) gameOverState(keyCode);
		
		
		
		//DEBUG
		//Show draw time
		if(keyCode==KeyEvent.VK_T) showDebugText=!showDebugText;
		
		//Refresh map
		if(keyCode==KeyEvent.VK_R) {
			
			switch(gp.currentMap) {
			
			case 0: gp.tm.loadMap("/maps/mainLoby.txt", 0); break;
			case 1: gp.tm.loadMap("/maps/shop.txt", 1); break;
			case 2: gp.tm.loadMap("/maps/dungeon01.txt", 2); break;
			case 3: gp.tm.loadMap("/maps/snow.txt", 3); break;
			case 4: gp.tm.loadMap("/maps/fabrik.txt", 4); break;
			
			}
			
		}
		
	}
	
	
	//Reads a release of an input of the player
	public void keyReleased(KeyEvent e) {
		
		int keyCode=e.getKeyCode();
		
		if(keyCode==KeyEvent.VK_W) {
			upPressed=false;
		}
		
		if(keyCode==KeyEvent.VK_S) {
			downPressed=false;
		}
		
		if(keyCode==KeyEvent.VK_D) {
			rightPressed=false;
		}
		
		if(keyCode==KeyEvent.VK_A) {
			leftPressed=false;
		}
		
	}
	
	
	//Manages the title state inputs
	public void titleState(int keyCode) {
		
		if(keyCode==KeyEvent.VK_W) {
			
			gp.playSound("cursor");
			
			gp.ui.pointer--;
			
			if(gp.ui.pointer==-1) {
				
				gp.ui.pointer=2;
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_S) {
			
			gp.playSound("cursor");
			
			gp.ui.pointer++;
			
			if(gp.ui.pointer==3) {
				
				gp.ui.pointer=0;
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_SPACE || keyCode==KeyEvent.VK_K) {
			
			//New game
			if(gp.ui.pointer==0) {
				
				gp.gameState=GAMESTATE.PLAYSTATE;
				gp.playMusic("Fantavision");
				gp.player.getPlayerImage();
				
			}
			
			//Load game
			if(gp.ui.pointer==1) {
				
				gp.sl.load();
				gp.gameState=GAMESTATE.PLAYSTATE;
				gp.playMusic("Fantavision");
				gp.ui.pointer=0;
				
			}
			
			//Quit game
			if(gp.ui.pointer==2) {
				
				System.exit(0);
				
			}
			
		}
		
	}
	
	
	//Manages the gameplay state inputs
	public void playState(int keyCode) {
		
		//Player's movement
		if(keyCode==KeyEvent.VK_W) {
			setAllDirections(false);
			upPressed=true;
		}
		
		if(keyCode==KeyEvent.VK_S) {
			setAllDirections(false);
			downPressed=true;
		}
		
		if(keyCode==KeyEvent.VK_D) {
			setAllDirections(false);
			rightPressed=true;
		}
		
		if(keyCode==KeyEvent.VK_A) {
			setAllDirections(false);
			leftPressed=true;
		}
		
		//Interact with something
		if(keyCode==KeyEvent.VK_K) {
			kPressed=true;							
		}		
		
		//Pause game
		if(keyCode==KeyEvent.VK_P) {
			
			if(gp.gameState==GAMESTATE.PLAYSTATE) {
				
				gp.gameState=GAMESTATE.PAUSESTATE;
				gp.music.pause();
				keyCode=0;
				
			}
			
		}		
		
		//Open player menu
		if(keyCode==KeyEvent.VK_L) {
			
			if(gp.gameState==GAMESTATE.PLAYSTATE) {
				
				gp.gameState=GAMESTATE.PLAYERMENUSTATE;
				
			}
			
		}
		
		//Show options menu
		if(keyCode==KeyEvent.VK_ESCAPE) {
			
			gp.gameState=GAMESTATE.OPTIONSSTATE;
			
		}
		
		//Open the map
		if(keyCode==KeyEvent.VK_M) {
			
			gp.gameState=GAMESTATE.MAPSTATE;
			
		}
		
		//Open the mini map
		if(keyCode==KeyEvent.VK_X) {
			
			if(gp.map.miniMapOn) {
				
				gp.map.miniMapOn=false;
				
			}else {
				
				gp.map.miniMapOn=true;
				
			}
			
		}
		
		
	}
	
	
	//Manages the pause state inputs
	public void pauseState(int keyCode) {
		
		if(keyCode==KeyEvent.VK_P) {
			
			gp.gameState=GAMESTATE.PLAYSTATE;
			gp.music.resume();
								
		}
		
	}
	
	
	//manages the map state inputs
	public void mapState(int keyCode) {
		
		if(keyCode==KeyEvent.VK_M) {
			
			gp.gameState=GAMESTATE.PLAYSTATE;
			
		}
		
	}
	
	
	//Manages the combat state inputs
	public void combatState(int keyCode) {
		
		switch(gp.combatM.combatState) {
		
		case MAINOPTIONSSTATE: combatMainOptionsState(keyCode); break; //Main options
		case ITEMSTATE: combatItemsState(keyCode); break; //Items
		case SKILLSTATE: combatSkillsState(keyCode); break; //Skill
		case DIALOGUESTATE: combatDialogueState(keyCode); break; //Dialogues
		
		}
		
		//Show stats globally
		if(keyCode==KeyEvent.VK_I) {
			
			gp.playSound("click");
			gp.ui.showStats=!gp.ui.showStats;
			
		}
		
	}
	
	
	//Manages the inputs for the main options menu in combat
	public void combatMainOptionsState(int keyCode) {
		
		//Select option
		if(keyCode==KeyEvent.VK_K) {
			
			gp.playSound("click");			
				
			switch(gp.ui.pointer) {
			
			//Attack
			case 0:  
											
			    gp.combatM.currentTurn=TURN.PLAYERTURN;

			    gp.combatM.startAction(gp.playerRpg, gp.combatM.currentEnemyRpg, ACTIONTYPE.ATTACK, null, null);
			    gp.combatM.chooseEnemyAction();

			    gp.combatM.resolveTurn();
				
			break;
			
			//Skills
			case 1:  
				
				gp.combatM.combatState=COMBATSTATE.SKILLSTATE;
				gp.ui.previousPointer=gp.ui.pointer;
				gp.ui.pointer=0;
				
			break;
			
			//Items
			case 2:
				
				gp.combatM.combatState=COMBATSTATE.ITEMSTATE;
				gp.ui.previousPointer=gp.ui.pointer;
				gp.ui.pointer=0;
				
			break; 
			
			//Stats
			case 3: gp.ui.showStats=!gp.ui.showStats; break;	
			
			//Run
			case 4:
				
				gp.combatM.endCombat();
				
			break;
			
			}				
			
		}
		
		//Go back
		if(keyCode==KeyEvent.VK_L) {
			
			if(gp.combatM.combatState!=COMBATSTATE.MAINOPTIONSSTATE) {
				
				gp.combatM.combatState=COMBATSTATE.MAINOPTIONSSTATE;
				gp.ui.pointer=gp.ui.previousPointer;				
				
			}
			
		}
		
		//Move cursor
		if(keyCode==KeyEvent.VK_W) {
			
				
			gp.playSound("cursor");
			
			if(gp.ui.pointer==0) {
				gp.ui.pointer=4;
			}else {
				gp.ui.pointer--;
			}				
				
			
			
		}
		
		if(keyCode==KeyEvent.VK_S) {
			
				
				gp.playSound("cursor");
				
				if(gp.ui.pointer==4) {
					gp.ui.pointer=0;
				}else {
					gp.ui.pointer++;
				}				
				
			
		}
		
		if(keyCode==KeyEvent.VK_D) {
			
			gp.playSound("cursor");
			
		}
		
		if(keyCode==KeyEvent.VK_A) {
			
			gp.playSound("cursor");
			
		}
		
	}
	
	
	//Manages the inputs for the items in the combat state
	public void combatItemsState(int keyCode) {
		
		//Select item
		if(keyCode==KeyEvent.VK_K) {
			
			gp.playSound("click");
			
			if(gp.ui.pointer==-1) { //Go back
				
				gp.combatM.combatState=COMBATSTATE.MAINOPTIONSSTATE;
				gp.ui.pointer=gp.ui.previousPointer;
				gp.ui.page=0;
				
			}else {
				
				//Should be player method
				gp.combatM.currentTurn=TURN.PLAYERTURN;
				
				int itemIndex=gp.ui.page==0 ? gp.ui.pointer:gp.ui.pointer+10;
				Entity selectedItem=gp.player.inventory.get(itemIndex);
				
				gp.combatM.startAction(gp.playerRpg, gp.combatM.currentEnemyRpg, ACTIONTYPE.ITEM, selectedItem, null);
				gp.combatM.chooseEnemyAction();

			    gp.combatM.resolveTurn();
			    
				if(selectedItem.type==TYPE.CONSUMABLE) {
					
					//Remove item if neccesary						
					if(selectedItem.amount>1) {
						
						selectedItem.amount--;
						
					}else {
						
						gp.player.inventory.remove(itemIndex);
						
					}					
					
				}
				
			}
			
		}
		
		//Go back
		if(keyCode==KeyEvent.VK_L) {
			
			if(gp.combatM.combatState!=COMBATSTATE.MAINOPTIONSSTATE) {
				
				gp.playSound("click");
				gp.combatM.combatState=COMBATSTATE.MAINOPTIONSSTATE;
				gp.ui.pointer=gp.ui.previousPointer;
				gp.ui.page=0;
				
			}
			
		}
		
		//Move cursor
		if(keyCode==KeyEvent.VK_W) {
			
			gp.playSound("cursor");

			//Page 0
			if(gp.ui.page==0) {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.inventory.size()>=4 ? 4:gp.player.inventory.size()-1;
					
				}else {
					
					//Move to go back button
					if(gp.ui.pointer==5) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move up
						//Check if pointer is at the limit
						if(gp.ui.pointer!=0) gp.ui.pointer--;										
						
					}						
					
				}

			//Page 1
			}else {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.inventory.size()>=14 ? 4:gp.player.inventory.size()-11;
					
				}else {
					
					//Move to go back button
					if(gp.ui.pointer==5) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move up
						//Check if pointer is at the limit
						if(gp.ui.pointer==0) {
							
							gp.ui.pointer=9;
							gp.ui.page=0;
								
						}else {
							
							gp.ui.pointer--;
							
						}
						
					}					
					
				}
				
			}

		}
		
		if(keyCode==KeyEvent.VK_S) {		
			
			gp.playSound("cursor");

			//Page 0
			if(gp.ui.page==0) {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.inventory.size()>=5 ? 5:gp.player.inventory.size()-1;
					
				}else {
					
					//Move to go back button
					int maxPointer=gp.player.inventory.size()>4 ? 4:gp.player.inventory.size()-1;
					if(gp.ui.pointer==maxPointer) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move down
						//Check if pointer is at the limit and if there's another page
						maxPointer=gp.player.inventory.size()>=10 ? 9:gp.player.inventory.size()-1;
						if(gp.ui.pointer==maxPointer) {
							
							if(gp.player.inventory.size()>10) {
								
								gp.ui.pointer=0;
								gp.ui.page=1;
								
							}
							
						}else {
							
							gp.ui.pointer++;
							
						}													
						
					}						
					
				}

			//Page 1
			}else {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.inventory.size()>=15 ? 5:gp.player.inventory.size()-11;
					
				}else {
					
					//Move to go back button
					int maxPointer=gp.player.inventory.size()>14 ? 4:gp.player.inventory.size()-11;
					if(gp.ui.pointer==maxPointer) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move down
						//Check if pointer is at the limit and if there's another page
						maxPointer=gp.player.inventory.size()-11;
						
						if(gp.ui.pointer!=maxPointer) gp.ui.pointer++;
						
					}					
					
				}
				
			}					

		}
		
		//Go to right column
		if(keyCode==KeyEvent.VK_D) {
			
			gp.playSound("cursor");
			
			//Do nothing when cursor is on <
			if(gp.ui.pointer!=-1) {
				
				//Only change column when there's an item on right column
				try {
					
					if(gp.player.inventory.get(gp.ui.page==0 ? gp.ui.pointer+5:gp.ui.pointer+15)!=null) {
						
						//If cursor is in the right column and there's another page, change page
						if(gp.ui.pointer>=5) {
							
							if(gp.player.inventory.size()>10) {
								
								gp.ui.page=1; //Should have maxPage
								gp.ui.pointer-=5;
								
							}
							
						}else{
							
							gp.ui.pointer+=5;				
							
						}						
						
					}				
					
				}catch(Exception e) {
					
					//Check if player is trying to change page
					if(gp.player.inventory.size()>10 && gp.ui.page==0) {
						
						gp.ui.page=1;
						gp.ui.pointer=gp.player.inventory.size()-11;
						
					}
					
				}
				
			}

		}
		
		if(keyCode==KeyEvent.VK_A) {
			
			gp.playSound("cursor");
			
			if(gp.ui.pointer!=-1) {
				
				if(gp.ui.pointer<5) {
					
					gp.ui.page=0;
					
				}else {
					
					gp.ui.pointer-=5;
					
				}
				
			}
			
		}
		
	}
	
	
	//Manages the inputs for the skills in the combat state
	public void combatSkillsState(int keyCode) {
		
		//Select skill
		if(keyCode==KeyEvent.VK_K) {
			
			gp.playSound("click");
			
			if(gp.ui.pointer==-1) { //Go back
				
				gp.combatM.combatState=COMBATSTATE.MAINOPTIONSSTATE;
				gp.ui.pointer=gp.ui.previousPointer;
				gp.ui.page=0;
				
			}else {
				
				//Should be player method
				gp.combatM.currentTurn=TURN.PLAYERTURN;
				
				int skillIndex=gp.ui.page==0 ? gp.ui.pointer:gp.ui.pointer+10;
				Skill selectedSkill=gp.player.skillSet.get(skillIndex);
				
				gp.combatM.startAction(gp.playerRpg, gp.combatM.currentEnemyRpg, ACTIONTYPE.SKILL, null, selectedSkill);
				gp.combatM.chooseEnemyAction();

			    gp.combatM.resolveTurn();
				
			}
			
		}
		
		//Go back
		if(keyCode==KeyEvent.VK_L) {
			
			if(gp.combatM.combatState!=COMBATSTATE.MAINOPTIONSSTATE) {
				
				gp.playSound("click");
				gp.combatM.combatState=COMBATSTATE.MAINOPTIONSSTATE;
				gp.ui.pointer=gp.ui.previousPointer;
				gp.ui.page=0;
				
			}
			
		}
		
		//Move cursor
		if(keyCode==KeyEvent.VK_W) {
			
			gp.playSound("cursor");

			//Page 0
			if(gp.ui.page==0) {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.skillSet.size()>=4 ? 4:gp.player.skillSet.size()-1;
					
				}else {
					
					//Move to go back button
					if(gp.ui.pointer==5) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move up
						//Check if pointer is at the limit
						if(gp.ui.pointer!=0) gp.ui.pointer--;										
						
					}						
					
				}

			//Page 1
			}else {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.skillSet.size()>=14 ? 4:gp.player.skillSet.size()-11;
					
				}else {
					
					//Move to go back button
					if(gp.ui.pointer==5) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move up
						//Check if pointer is at the limit
						if(gp.ui.pointer==0) {
							
							gp.ui.pointer=9;
							gp.ui.page=0;
								
						}else {
							
							gp.ui.pointer--;
							
						}
						
					}					
					
				}
				
			}

		}
		
		if(keyCode==KeyEvent.VK_S) {		
			
			gp.playSound("cursor");

			//Page 0
			if(gp.ui.page==0) {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.skillSet.size()>=5 ? 5:gp.player.skillSet.size()-1;
					
				}else {
					
					//Move to go back button
					int maxPointer=gp.player.skillSet.size()>4 ? 4:gp.player.skillSet.size()-1;
					if(gp.ui.pointer==maxPointer) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move down
						//Check if pointer is at the limit and if there's another page
						maxPointer=gp.player.skillSet.size()>=10 ? 9:gp.player.skillSet.size()-1;
						if(gp.ui.pointer==maxPointer) {
							
							if(gp.player.skillSet.size()>10) {
								
								gp.ui.pointer=0;
								gp.ui.page=1;
								
							}
							
						}else {
							
							gp.ui.pointer++;
							
						}													
						
					}						
					
				}

			//Page 1
			}else {
				
				//Move back button
				if(gp.ui.pointer==-1) {
					
					gp.ui.pointer=gp.player.skillSet.size()>=15 ? 5:gp.player.skillSet.size()-11;
					
				}else {
					
					//Move to go back button
					int maxPointer=gp.player.skillSet.size()>14 ? 4:gp.player.skillSet.size()-11;
					if(gp.ui.pointer==maxPointer) {
						
						gp.ui.pointer=-1;
						
					}else {
						
						//Move down
						//Check if pointer is at the limit and if there's another page
						maxPointer=gp.player.skillSet.size()-11;
						
						if(gp.ui.pointer!=maxPointer) gp.ui.pointer++;
						
					}					
					
				}
				
			}					

		}
		
		//Go to right column
		if(keyCode==KeyEvent.VK_D) {
			
			gp.playSound("cursor");
			
			//Do nothing when cursor is on <
			if(gp.ui.pointer!=-1) {
				
				//Only change column when there's an item on right column
				try {
					
					if(gp.player.skillSet.get(gp.ui.page==0 ? gp.ui.pointer+5:gp.ui.pointer+15)!=null) {
						
						//If cursor is in the right column and there's another page, change page
						if(gp.ui.pointer>=5) {
							
							if(gp.player.skillSet.size()>10) {
								
								gp.ui.page=1; //Should have maxPage
								gp.ui.pointer-=5;
								
							}
							
						}else{
							
							gp.ui.pointer+=5;				
							
						}						
						
					}				
					
				}catch(Exception e) {
					
					//Check if player is trying to change page
					if(gp.player.skillSet.size()>10 && gp.ui.page==0) {
						
						gp.ui.page=1;
						gp.ui.pointer=gp.player.skillSet.size()-11;
						
					}
					
				}
				
			}

		}
		
		if(keyCode==KeyEvent.VK_A) {
			
			gp.playSound("cursor");
			
			if(gp.ui.pointer!=-1) {
				
				if(gp.ui.pointer<5) {
					
					gp.ui.page=0;
					
				}else {
					
					gp.ui.pointer-=5;
					
				}
				
			}
			
		}
		
	}
	
	
	//Manage the combat dialogue state inputs
	public void combatDialogueState(int keyCode) {
						
		//Pass current dialogue
		if(keyCode==KeyEvent.VK_K) {
			
	        gp.combatM.nextDialogue();
			
		}
		
	}
	
	
	//Manages the game over state inputs
	public void gameOverState(int keyCode){
		
		gp.playSound("cursor");
		
		//Move pointer up
		if(keyCode==KeyEvent.VK_W) {
			
			gp.ui.pointer--;
			if(gp.ui.pointer<0) gp.ui.pointer=1;
			
		}
		
		//Move pointer down
		if(keyCode==KeyEvent.VK_S) {
			
			gp.ui.pointer++;
			if(gp.ui.pointer>1) gp.ui.pointer=0;
			
		}
		
		//Select an option
		if(keyCode==KeyEvent.VK_K) {
			
			//Retry
			if(gp.ui.pointer==0) gp.retry();
			else if(gp.ui.pointer==1) gp.restart();
			
		}
		
	}
	
	
	//Manages the dialogue state inputs
	public void dialogueState(int keyCode) {
		
		//Pass current dialogue
		if(keyCode==KeyEvent.VK_K) {
			
			kPressed=true;
			
		}
		
	}
	
	
	//Manages the player menu state inputs
	public void playerMenuState(int keyCode) {
		
		//Close menu
		if(keyCode==KeyEvent.VK_L) {
			
			if(gp.gameState==GAMESTATE.PLAYERMENUSTATE) {
				
				gp.gameState=GAMESTATE.PLAYSTATE;
				gp.ui.playerSlotCol=0;
				gp.ui.playerSlotRow=0;
				
			}
			
		}
		
		//Select an item
		if(keyCode==KeyEvent.VK_K) {
			
			gp.player.selectItem();
			
		}
		
		//Move cursor
		playerInventoryCursor(keyCode);
		
	}
	
	
	//Manages the inputs in the options menu
	public void optionsState(int keyCode) {
		
		if(keyCode==KeyEvent.VK_ESCAPE) {
			
			gp.gameState=GAMESTATE.PLAYSTATE;
			gp.ui.subState=0;
			gp.ui.pointer=0;
			
		}
		
		if(keyCode==KeyEvent.VK_K) {
			
			gp.playSound("click");
			kPressed=true;
			
		}
		
		//Pointer
		int maxPointerNum=0;
		switch(gp.ui.subState) {
		
		case 0: maxPointerNum=5; break;
		case 3: maxPointerNum=1; break;
		
		}
		
		if(keyCode==KeyEvent.VK_W) {
			
			gp.ui.pointer--;
			gp.playSound("cursor");
			if(gp.ui.pointer<0) {
				
				gp.ui.pointer=maxPointerNum;
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_S) {
			
			gp.ui.pointer++;
			gp.playSound("cursor");
			if(gp.ui.pointer>maxPointerNum) {
				
				gp.ui.pointer=0;
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_A) {
			
			if(gp.ui.subState==0) {
				
				if(gp.ui.pointer==1 && gp.music.volumeScale>0) {
					
					gp.music.volumeScale--;
					gp.music.checkVolume();
					gp.playSound("cursor");
					
				}
				
				if(gp.ui.pointer==2 && gp.se.volumeScale>0) {
					
					gp.se.volumeScale--;
					gp.playSound("cursor");
					
				}
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_D) {
			
			if(gp.ui.subState==0) {
				
				if(gp.ui.pointer==1 && gp.music.volumeScale<5) {
					
					gp.music.volumeScale++;
					gp.music.checkVolume();
					gp.playSound("cursor");
					
				}
				
				if(gp.ui.pointer==2 && gp.se.volumeScale<5) {
					
					gp.se.volumeScale++;
					gp.playSound("cursor");
					
				}
				
			}
			
		}
		
	}
	
	
	//Manages the inputs for the trade system state
	public void tradeState(int keyCode) {
		
		if(keyCode==KeyEvent.VK_K) {
			
			kPressed=true;
			
		}
		
		
		//Select action subState
		if(gp.ui.subState==0) {
			
			if(keyCode==KeyEvent.VK_W) {
				
				gp.ui.pointer--;
				if(gp.ui.pointer<0) {
					
					gp.ui.pointer=2;
					
				}
				
				gp.playSound("cursor");
				
			}
			
			if(keyCode==KeyEvent.VK_S) {
				
				gp.ui.pointer++;
				if(gp.ui.pointer>2) {
					
					gp.ui.pointer=0;
					
				}
				
				gp.playSound("cursor");
				
			}
			
			if(keyCode==KeyEvent.VK_L) {
				
				gp.gameState=GAMESTATE.PLAYSTATE;
				gp.ui.pointer=0;
				gp.ui.charIndex=0;
				gp.ui.combinedText="";
				
			}
			
		}		
		
		//Buy subState
		if(gp.ui.subState==1) {
			
			npcInventoryCursor(keyCode);
			
			if(keyCode==KeyEvent.VK_L) {
				
				gp.ui.subState=0;
				
			}
			
		}
		
		//Sell subState
		if(gp.ui.subState==2) {
			
			playerInventoryCursor(keyCode);
			
			if(keyCode==KeyEvent.VK_L) {
				
				gp.ui.subState=0;
				
			}
			
		}
		
	}
	
	
	//Manages the cursor movement in the player's inventory
	public void playerInventoryCursor(int keyCode) {
		
		if(keyCode==KeyEvent.VK_W) {
			
			if(gp.ui.playerSlotRow!=0) {
				
				gp.ui.playerSlotRow--;
				gp.playSound("cursor");
				
			}//Could add a sound here
			
		}
		
		if(keyCode==KeyEvent.VK_A) {
			
			if(gp.ui.playerSlotCol!=0) {
				
				gp.ui.playerSlotCol--;
				gp.playSound("cursor");
				
			}			
			
		}
		
		if(keyCode==KeyEvent.VK_S) {
			
			if(gp.ui.playerSlotRow!=3) {
				
				gp.ui.playerSlotRow++;
				gp.playSound("cursor");				
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_D) {
			
			if(gp.ui.playerSlotCol!=4) {
				
				gp.ui.playerSlotCol++;
				gp.playSound("cursor");				
				
			}
			
		}
		
	}
	
	
	//Manages the cursor movement in the npc inventory
	public void npcInventoryCursor(int keyCode) {
		
		if(keyCode==KeyEvent.VK_W) {
			
			if(gp.ui.npcSlotRow!=0) {
				
				gp.ui.npcSlotRow--;
				gp.playSound("cursor");
				
			}//Could add a sound here
			
		}
		
		if(keyCode==KeyEvent.VK_A) {
			
			if(gp.ui.npcSlotCol!=0) {
				
				gp.ui.npcSlotCol--;
				gp.playSound("cursor");
				
			}			
			
		}
		
		if(keyCode==KeyEvent.VK_S) {
			
			if(gp.ui.npcSlotRow!=3) {
				
				gp.ui.npcSlotRow++;
				gp.playSound("cursor");				
				
			}
			
		}
		
		if(keyCode==KeyEvent.VK_D) {
			
			if(gp.ui.npcSlotCol!=4) {
				
				gp.ui.npcSlotCol++;
				gp.playSound("cursor");				
				
			}
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}















