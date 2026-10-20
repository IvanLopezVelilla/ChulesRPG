package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import combat.CombatEntity;
import entity.Entity;
import skill.Skill;


public class UI {
	/*
	 * Manages the text that the player sees on screen
	 */
	
	GamePanel gp;
	Graphics2D g2;
	public Font maruMonica;
	public boolean messageOn=false;
	public String message="";
	int messageCounter=0;
	public boolean gameFinished=false;
	public String currentDialogue="";
	public int pointer=0;
	public int previousPointer=pointer;
	public int page=0;
	int subState;
	public Entity npc;
	public int counter;
	public int charIndex=0;
	public String combinedText="";
	private long lastSpeakSoundTime=0;
	private final long speakSoundCooldown=35;
	
	//Inventory
	public int playerSlotCol=0;
	public int playerSlotRow=0;
	public int npcSlotCol=0;
	public int npcSlotRow=0;
	
	//Combat
	public boolean showStats=false;
	
	
	public UI(GamePanel gp) {
		
		this.gp=gp;
				
		try {
			
			InputStream is=getClass().getResourceAsStream("/font/MaruMonica.ttf");
			maruMonica=Font.createFont(Font.TRUETYPE_FONT, is);
			
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	//Draws a custom message on the screen
	public void showMessage(String text) {
		
		message=text;
		messageOn=true;
		
	}
	
	
	//Draws the according UI elements on the screen
	public void draw(Graphics2D g2) {
		
		this.g2=g2;
		
		g2.setFont(maruMonica);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); //No hace nada con maruMonica
		g2.setColor(Color.white);
		
		//GAME STATES
		//Title state
		if(gp.gameState==GAMESTATE.TITLESTATE) drawTitleScreen();
		
		//Play state
		if(gp.gameState==GAMESTATE.PLAYSTATE) {/*Nothing for now*/}
		
		//Pause state
		if(gp.gameState==GAMESTATE.PAUSESTATE) drawPauseScreen();
		
		//Dialogue state
		if(gp.gameState==GAMESTATE.DIALOGUESTATE) drawDialogueScreen();

		
		//Player menu state
		if(gp.gameState==GAMESTATE.PLAYERMENUSTATE) {
			
			drawPlayerMenuScreen();
			drawInventory(gp.player, true);
			
		}
		
		//Options menu state
		if(gp.gameState==GAMESTATE.OPTIONSSTATE)drawOptionsScreen();
		
		//Trading state
		if(gp.gameState==GAMESTATE.TRADESTATE) drawTradeScreen();
		
		//Sleep state
		if(gp.gameState==GAMESTATE.SLEEPSTATE) drawSleepScreen();
		
		//Combat state
		if(gp.gameState==GAMESTATE.COMBATSTATE) drawCombatScreen();
		
		//Game over state
		if(gp.gameState==GAMESTATE.GAMEOVERSTATE) drawGameOverScreen();
		
	}
	
	
	//Draws the title screen
	public void drawTitleScreen() {
		
		//Title name
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96F));
		String text="ChulesRPG";
		int x=getXforCenteredText(text);
		int y=gp.tileSize*3;
		
		//Text shadow
		g2.setColor(Color.black);
		g2.drawString(text, x+5, y+5);
	
		//Main color
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		
		//Player image
		x=gp.screenWidth/2-(gp.tileSize*4)/2;
		y+=gp.tileSize/2;
		g2.drawImage(gp.player.down1, x, y, gp.tileSize*4, gp.tileSize*4, null);
		
		//Menu
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48));
		
		text="NEW GAME";
		x=getXforCenteredText(text);
		y+=gp.tileSize*5;
		g2.setColor(Color.black);
		g2.drawString(text, x+5, y+5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(pointer==0) {
			
			g2.setColor(Color.black);
			g2.drawString(">", x-gp.tileSize+4, y+5);
			g2.setColor(Color.white);
			g2.drawString(">", x-gp.tileSize, y);
			
		}
		
		text="LOAD GAME";
		x=getXforCenteredText(text);
		y+=gp.tileSize;
		g2.setColor(Color.black);
		g2.drawString(text, x+5, y+5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(pointer==1) {
			
			g2.setColor(Color.black);
			g2.drawString(">", x-gp.tileSize+4, y+5);
			g2.setColor(Color.white);
			g2.drawString(">", x-gp.tileSize, y);
			
		}
		
		text="QUIT";
		x=getXforCenteredText(text);
		y+=gp.tileSize;
		g2.setColor(Color.black);
		g2.drawString(text, x+5, y+5);
		g2.setColor(Color.white);
		g2.drawString(text, x, y);
		if(pointer==2) {
			
			g2.setColor(Color.black);
			g2.drawString(">", x-gp.tileSize+4, y+5);
			g2.setColor(Color.white);
			g2.drawString(">", x-gp.tileSize, y);
			
		}
		
	}
	
	
	//Manages the speak method soundwise
	private void playDialogueSound() {

	    long now=System.currentTimeMillis();

	    if(now-lastSpeakSoundTime>=speakSoundCooldown) {

	        gp.playSound("speak");
	        lastSpeakSoundTime=now;
	        
	    }
	    
	}
	
	
	//Draws the dialogues
	public void drawDialogueScreen() {
		
		//Window
		int x=gp.tileSize*3;
		int y=gp.tileSize/2;
		int width=gp.screenWidth-(gp.tileSize*6);
		int height=gp.tileSize*4;
		drawSubWindow(x, y, width, height);
		
		//Text
		x+=gp.tileSize;
		y+=gp.tileSize;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
		
		if(npc.dialogues[npc.dialogueSet][npc.dialogueIndex]!=null) {
			
			char characters[]=npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();
			
			if(charIndex<characters.length) {
				
				char c=characters[charIndex];

				if(c!=' ' && c!='.' && c!=',' && c!='\n') {

				    playDialogueSound();
				    
				}
				
				String s=String.valueOf(characters[charIndex]);
				combinedText=combinedText+s;
				currentDialogue=combinedText;
				
				charIndex++;
				
			}
			
			
			if(gp.ih.kPressed) {
				
				charIndex=0;
				combinedText="";
				currentDialogue="";
				
				if(gp.gameState==GAMESTATE.DIALOGUESTATE) {
					
					npc.dialogueIndex++;
					gp.ih.kPressed=false;
					
				}
				
			}
			
		}else { //If no text is in the array, this conversation is over
			
			npc.dialogueIndex=0;
			
			if(gp.gameState==GAMESTATE.DIALOGUESTATE) {
				
				gp.gameState=GAMESTATE.PLAYSTATE;
				
			}
			
		}
		
		//Line breaks
		for(String line:currentDialogue.split("\n")) {
			
			g2.drawString(line, x, y);
			y+=40;
			
		}		
		
	}
	
	
	//Draws the combat screen
	public void drawCombatScreen() {
		
		//Window
		int x=gp.tileSize/2;
		int y=gp.tileSize/2;
		int width=gp.screenWidth-(gp.tileSize);
		int height=gp.tileSize*11;
		drawSubWindow(x, y, width, height);
		
		//Combat text
		x+=gp.tileSize;
		y+=gp.tileSize;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
		
		if(gp.combatM.combatMaster.dialogues[gp.combatM.combatMaster.dialogueSet][gp.combatM.combatMaster.dialogueIndex]!=null) {
			
			char characters[]=gp.combatM.combatMaster.dialogues[gp.combatM.combatMaster.dialogueSet][gp.combatM.combatMaster.dialogueIndex].toCharArray();
			
			if(charIndex<characters.length) {
				
				char c = characters[charIndex];

				if(c!=' ' && c!='.' && c!=',' && c!='\n') {

				    playDialogueSound();
				    
				}
				
				String s=String.valueOf(characters[charIndex]);
				combinedText=combinedText+s;
				currentDialogue=combinedText;
				
				charIndex++;
				
			}
			
		}
		
		//Line breaks
		for(String line:currentDialogue.split("\n")) {
			
			g2.drawString(line, x, y);
			y+=40;
			
		}	
		
		
		//Draw options for each state
		switch(gp.combatM.combatState) {
		
			case MAINOPTIONSSTATE: drawCombatOptions(); break;	//Player main options	
			case ITEMSTATE:	drawCombatItems(); break; //Player items
			case SKILLSTATE: drawCombatSkills(); break; //Player skills
			case DIALOGUESTATE: drawCombatDialogue(); break; //Combat dialogue
		
		}
		
		x=62;
		y=501;		
		
		//Player and enemy images
		drawCombatPortrait(gp.tileSize*9, gp.tileSize*6+(gp.tileSize/2), gp.player, Color.gray);
		drawCombatPortrait(gp.tileSize*14, 50+(gp.tileSize/2), gp.combatM.currentEnemy, Color.gray);
		g2.setColor(Color.white);
		g2.drawString("VS", (x*10)+(x/2), y-190);
		
		//Player and enemy stats
		drawRpgStats((x*10)+(x/2)+10, y-150, gp.playerRpg);
		drawRpgStats(x*8+(gp.tileSize/2)+5, y-400, gp.combatM.currentEnemyRpg);
		
		//Player equipment
		if(gp.player.currentWeapon!=null) g2.drawImage(gp.player.currentWeapon.down1, (x*7), y-(gp.tileSize*4)+13, null);
		if(gp.player.currentShield!=null) g2.drawImage(gp.player.currentShield.down1, (x*7), y-(gp.tileSize*3)+13, null);
		if(gp.player.currentShoes!=null) g2.drawImage(gp.player.currentShoes.down1, (x*7), y-(gp.tileSize*2)+13, null);
		
	}
	
	
	//Draws the player combat options
	public void drawCombatOptions() {
		
		int x=gp.tileSize+(gp.tileSize/2)-10;
		int y=gp.tileSize*3;
		final int lineHeight=80;
		
		//Cursor
		int cursorX=x-(gp.tileSize/3);
		int cursorY=(y-gp.tileSize)+10;
		int cursorWidth=gp.tileSize*3;
		int cursorHeight=gp.tileSize;
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		
		if(pointer==0) {
			
			g2.drawString(">", x, y);
			g2.drawString("Attack", x+50, y);
			g2.drawRoundRect(cursorX+50, cursorY, cursorWidth, cursorHeight, 10, 10);
			
		}else {
			
			g2.drawString("Attack", x, y);
			
		}
		y+=lineHeight;
		cursorY+=lineHeight;

		
		if(pointer==1) {
			
			g2.drawString(">", x, y);
			g2.drawString("Skill", x+70, y);
			g2.drawRoundRect(cursorX+50, cursorY, cursorWidth, cursorHeight, 10, 10);
			
		}else {
			
			g2.drawString("Skill", x, y);
			
		}
		y+=lineHeight;
		cursorY+=lineHeight;
		
		if(pointer==2) {
			
			g2.drawString(">", x, y);
			g2.drawString("Items", x+55, y);
			g2.drawRoundRect(cursorX+50, cursorY, cursorWidth, cursorHeight, 10, 10);
			
		}else {
			
			g2.drawString("Items", x, y);
			
		}
		y+=lineHeight+(lineHeight/2)+(lineHeight/10);
		cursorY+=lineHeight+(lineHeight/2)+(lineHeight/10);
		
		if(pointer==3) {
			
			g2.drawString(">", x, y);
			g2.drawString("Stats", x+50, y);
			g2.drawRoundRect(cursorX+50, cursorY, cursorWidth, cursorHeight, 10, 10);
			
		}else {
			
			g2.drawString("Stats", x, y);
			
		}
		y+=lineHeight-(lineHeight/7);
		cursorY+=lineHeight-(lineHeight/7);
		
		if(pointer==4) {
			
			g2.drawString(">", x, y);
			g2.drawString("Run", x+70, y);
			g2.drawRoundRect(cursorX+50, cursorY, cursorWidth, cursorHeight, 10, 10);
			
		}else {
			
			g2.drawString("Run", x, y);
			
		}
		
	}
	
	
	//Draws the player item options in the combat screen
	public void drawCombatItems() {
		
		int x=gp.tileSize*2-(gp.tileSize/2);
		int y=gp.tileSize*2;
		final int lineHeight=80;
		
		drawSubWindow(x-17, y-15, gp.tileSize*4+25, gp.tileSize*9-15);
		
		//Cursor
		int cursorX=x+(gp.tileSize/4)-5;
		int cursorY=(y-gp.tileSize)+45;
		int cursorWidth=gp.tileSize+5;
		int cursorHeight=gp.tileSize+5;
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		int itemX;
		
		for(int i=0;i<gp.player.inventory.size();i++) {
			
			if(page==0 || i+10<gp.player.inventory.size()) {
				
				if(i%5==0 && i!=0) {
					
					x=x*2+(x/2);
					y=gp.tileSize*2;
					cursorX=x+(gp.tileSize/4)-5;
					cursorY=(y-gp.tileSize)+45;
					
				}
				
				if(pointer==i) {

				    itemX=x+30;

				    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
				    g2.drawString(">", x, y+40);
				    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));

				    g2.drawImage(gp.player.inventory.get(page==0 ? i:i+10).down1,itemX, y, null);
				    g2.drawRoundRect(cursorX+20, cursorY,cursorWidth, cursorHeight,10, 10);
				    
				    drawItemDescription(gp.player.inventory.get(page==0 ? i:i+10), 270, 80);

				}else{

				    itemX=x;
				    g2.drawImage(gp.player.inventory.get(page==0 ? i:i+10).down1,itemX, y, null);

				}
				
				//Display amount
				if(gp.player.inventory.get(page==0 ? i:i+10).amount>1) {

				    g2.setFont(g2.getFont().deriveFont(32f));
				    String s="" + gp.player.inventory.get(page==0 ? i:i+10).amount;

				    int amountX=getXforAlignToRightText(s, itemX + 44);
				    int amountY=y+gp.tileSize;

				    //Shadow
				    g2.setColor(new Color(60,60,60));
				    g2.drawString(s, amountX, amountY);

				    //Number
				    g2.setColor(Color.white);
				    g2.drawString(s, amountX-3, amountY);

				}
				
				y+=lineHeight;
				cursorY+=lineHeight;
				
				if(i+1==10) i=gp.player.inventory.size();
				
			}
			
		}
		
		//Go back arrow
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		g2.drawString("<", gp.tileSize*2-(gp.tileSize/2)-20, gp.tileSize*11+9);
		if(pointer==-1) g2.drawRoundRect(gp.tileSize, gp.tileSize*11-(gp.tileSize/2), cursorWidth/2+10, cursorHeight/2+10, 10, 10);
		
		//Page
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 38F));
		g2.drawString(String.valueOf(page+1), gp.tileSize*5-2, gp.tileSize*11+5);
		
	}
	
	
	//Draws the item description on the combat screen
	public void drawItemDescription(Entity item, int x, int y) {

	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));

	    final int maxCharsPerLine=12;
	    final int lineHeight=32;
	    final int padding=21;

	    //Stores all wrapped lines
	    ArrayList<String> lines=new ArrayList<>();

	    //Full text
	    String fullText="[" + item.name + "]\n" + item.description;

	    //Split manual line jumps first
	    String[] paragraphs=fullText.split("\n");

	    //Wrap each paragraph
	    for(String paragraph:paragraphs) {

	        String text=paragraph;

	        while(text.length()>maxCharsPerLine) {

	            int splitIndex=text.lastIndexOf(' ', maxCharsPerLine);

	            //If there are no spaces, cut the word
	            if(splitIndex==-1) {

	                splitIndex=maxCharsPerLine;

	            }

	            //Save line
	            lines.add(text.substring(0, splitIndex));

	            //Remove drawn text
	            text=text.substring(splitIndex).trim();

	        }

	        //Add remaining text
	        lines.add(text);

	    }

	    //Calculate dynamic width
	    int longestWidth=0;

	    for(String line:lines) {

	        int lineWidth=g2.getFontMetrics().stringWidth(line);

	        if(lineWidth>longestWidth) {

	            longestWidth=lineWidth;

	        }

	    }

	    //Dynamic window size
	    int width=longestWidth+(padding*2)-10;
	    int height=(lines.size()*lineHeight)+padding+10;

	    //Draw window
	    drawSubWindow(x, y, width, height);

	    //Text position
	    int textX=x+padding;
	    int textY=y+gp.tileSize-10;

	    //Draw all lines
	    for(String line:lines) {

	        g2.drawString(line, textX, textY);
	        textY+=lineHeight;

	    }

	}
	
	
	//Draws the skill description on the combat screen
	public void drawSkillDescription(Skill skill, int x, int y) {

	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24F));

	    final int maxCharsPerLine=12;
	    final int lineHeight=32;
	    final int padding=21;

	    //Stores all wrapped lines
	    ArrayList<String> lines=new ArrayList<>();

	    //Full text
	    String fullText="[" + skill.name + "]\n" + skill.description;

	    //Split manual line jumps first
	    String[] paragraphs=fullText.split("\n");

	    //Wrap each paragraph
	    for(String paragraph:paragraphs) {

	        String text=paragraph;

	        while(text.length()>maxCharsPerLine) {

	            int splitIndex=text.lastIndexOf(' ', maxCharsPerLine);

	            //If there are no spaces, cut the word
	            if(splitIndex==-1) {

	                splitIndex=maxCharsPerLine;

	            }

	            //Save line
	            lines.add(text.substring(0, splitIndex));

	            //Remove drawn text
	            text=text.substring(splitIndex).trim();

	        }

	        //Add remaining text
	        lines.add(text);

	    }

	    //Calculate dynamic width
	    int longestWidth=0;

	    for(String line:lines) {

	        int lineWidth=g2.getFontMetrics().stringWidth(line);

	        if(lineWidth>longestWidth) {

	            longestWidth=lineWidth;

	        }

	    }

	    //Dynamic window size
	    int width=longestWidth+(padding*2)-10;
	    int height=(lines.size()*lineHeight)+padding+10;

	    //Draw window
	    drawSubWindow(x, y, width, height);

	    //Text position
	    int textX=x+padding;
	    int textY=y+gp.tileSize-10;

	    //Draw all lines
	    for(String line:lines) {

	        g2.drawString(line, textX, textY);
	        textY+=lineHeight;

	    }
	    
	    //Draw mana cost
	    g2.setColor(Color.cyan);
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 23F));
	    g2.drawString("\nMana cost: " + skill.cost + "MP", textX-16, textY+14);
        
	}
	
	
	//Draws the player skills options in the combat screen
	public void drawCombatSkills() {
		
		int x=gp.tileSize*2-(gp.tileSize/2);
		int y=gp.tileSize*2;
		final int lineHeight=80;
		
		drawSubWindow(x-17, y-15, gp.tileSize*4+25, gp.tileSize*9-15);
		
		//Cursor
		int cursorX=x+(gp.tileSize/4)-5;
		int cursorY=(y-gp.tileSize)+45;
		int cursorWidth=gp.tileSize+5;
		int cursorHeight=gp.tileSize+5;
		
		g2.setColor(Color.white);
		g2.setStroke(new BasicStroke(3));
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
		int itemX;
		
		for(int i=0;i<gp.player.skillSet.size();i++) {
			
			if(page==0 || i+10<gp.player.skillSet.size()) {
				
				if(i%5==0 && i!=0) {
					
					x=x*2+(x/2);
					y=gp.tileSize*2;
					cursorX=x+(gp.tileSize/4)-5;
					cursorY=(y-gp.tileSize)+45;
					
				}
				
				if(pointer==i) {

				    itemX=x+30;

				    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
				    g2.drawString(">", x, y+40);
				    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));

				    g2.drawImage(gp.player.skillSet.get(page==0 ? i:i+10).image,itemX, y, null);
				    g2.drawRoundRect(cursorX+20, cursorY,cursorWidth, cursorHeight,10, 10);
				    
				    drawSkillDescription(gp.player.skillSet.get(page==0 ? i:i+10), 270, 80);

				}else{

				    itemX=x;
				    g2.drawImage(gp.player.skillSet.get(page==0 ? i:i+10).image,itemX, y, null);

				}
				
				//Display mana cost
			    g2.setFont(g2.getFont().deriveFont(32f));
			    String s="" + gp.player.skillSet.get(page==0 ? i:i+10).cost;

			    int amountX=getXforAlignToRightText(s, itemX + 44);
			    int amountY=y+gp.tileSize;

			    //Shadow
			    g2.setColor(new Color(60,60,60));
			    g2.drawString(s, amountX, amountY);

			    //Number
			    g2.setColor(Color.cyan);
			    g2.drawString(s, amountX-3, amountY);
			    g2.setColor(Color.white);
				
				y+=lineHeight;
				cursorY+=lineHeight;
				
				if(i+1==10) i=gp.player.skillSet.size();
				
			}
			
		}
		
		//Go back arrow
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		g2.drawString("<", gp.tileSize*2-(gp.tileSize/2)-20, gp.tileSize*11+9);
		if(pointer==-1) g2.drawRoundRect(gp.tileSize, gp.tileSize*11-(gp.tileSize/2), cursorWidth/2+10, cursorHeight/2+10, 10, 10);
		
		//Page
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 38F));
		g2.drawString(String.valueOf(page+1), gp.tileSize*5-2, gp.tileSize*11+5);
		
	}
	
	
	//Draws the current combat dialogue
	public void drawCombatDialogue() {
		
		int x=gp.tileSize/2;
		int y=gp.tileSize/2;
		
		//Text
		x+=gp.tileSize;
		y+=gp.tileSize;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));
		
		if(gp.combatM.combatMaster.dialogues[gp.combatM.combatMaster.dialogueSet][gp.combatM.combatMaster.dialogueIndex]!=null) {
			
			char characters[]=gp.combatM.combatMaster.dialogues[gp.combatM.combatMaster.dialogueSet][gp.combatM.combatMaster.dialogueIndex].toCharArray();
			
			if(charIndex<characters.length) {
				
				char c=characters[charIndex];

				if(c!=' ' && c!='.' && c!=',' && c!='\n') {

				    playDialogueSound();
				    
				}
				
				String s=String.valueOf(characters[charIndex]);
				combinedText=combinedText+s;
				currentDialogue=combinedText;
				
				charIndex++;
				
			}

		}else{
			
			gp.combatM.endDialogue();
		    
		}
		
		
		//Line breaks
		for(String line:currentDialogue.split("\n")) {
			
			g2.drawString(line, x, y);
			y+=40;
			
		}	
		
	}
	
	
	//Resets the current combat dialogue
	public void resetCombatDialogue() {
		
		previousPointer=gp.ui.pointer;
		pointer=0;		
		gp.combatM.combatMaster.dialogueIndex=0;
		gp.combatM.combatMaster.dialogues=new String[30][30];;
		charIndex=0;
		combinedText="";
		currentDialogue="";
		
	}
	
	
	//Draws the player/enemy frame and sprite for the battle screen
	public void drawCombatPortrait(int x, int y, Entity npc, Color backgroundColor) {
		
		npc.spriteCounter++;
		if(npc.spriteCounter>10) {
			
			if(npc.sprite==1) {
				npc.sprite=2;
			}else if(npc.sprite==2) {
				npc.sprite=1;
			}
			
			npc.spriteCounter=0;
		}

	    int boxSize=(gp.tileSize*5)-(gp.tileSize/2);
	    int spriteSize=(gp.tileSize*4)-(gp.tileSize/2);

	    //Name and level
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setColor(Color.white);
	    g2.drawString(npc.name +" - lvl:" + npc.level, x+5, y-6);
	    
	    //Background
	    g2.setColor(backgroundColor);
	    g2.fillRoundRect(x, y, boxSize, boxSize, 20, 20);

	    //Outer border
	    g2.setColor(Color.white);
	    g2.setStroke(new BasicStroke(3));
	    g2.drawRoundRect(x, y, boxSize, boxSize, 20, 20);

	    //Inner border
	    g2.setColor(Color.black);
	    g2.setStroke(new BasicStroke(1));
	    g2.drawRoundRect(x + 3, y + 3, boxSize - 6, boxSize - 6, 16, 16);

	    //Center sprite
	    int spriteX = x + (boxSize - spriteSize) / 2;
	    int spriteY = y + (boxSize - spriteSize) / 2;

	    g2.drawImage(npc.sprite==1 ? npc.down1:npc.down2, spriteX, spriteY, spriteSize, spriteSize, null);

	    //Reset stroke
	    g2.setStroke(new BasicStroke(1));

	}
	
	
	//Draws the player/enemy stats on the combat screen
	public void drawRpgStats(int x, int y, CombatEntity entity) {
		
		int lineHeight=40;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setColor(Color.white);
		
		if(showStats) {
			
			g2.drawString("Life: " + entity.currentLife + "/" + entity.maxLife, x, y);
			y+=lineHeight;
			
			g2.drawString("Mana: " + entity.currentMana + "/" + entity.maxMana, x, y);
			y+=lineHeight;
			
			if(entity.attackBoost.size()>0) g2.drawString("Attack: " + (entity.attack+entity.attackBoost.get(0)) + " +", x, y);
			else g2.drawString("Attack: " + entity.attack, x, y);
			y+=lineHeight;
			
			if(entity.defenseBoost.size()>0) g2.drawString("Defense: " + (entity.defense+entity.defenseBoost.get(0)) + " +", x, y);
			else g2.drawString("Defense: " + entity.defense, x, y);
			y+=lineHeight;
			
			if(entity.speedBoost.size()>0) g2.drawString("Speed: " + (entity.rpgSpeed+entity.speedBoost.get(0)) + " +", x, y);
			else g2.drawString("Speed: " + entity.rpgSpeed, x, y);
			
		}else {
			
			if(entity==gp.playerRpg) {
				
				g2.drawString("HP " + entity.currentLife + "/" + entity.maxLife, x, y);
				y+=lineHeight;
				g2.drawString("MP " + entity.currentMana + "/" + entity.maxMana, x, y);
				
				
			}else {
				
				g2.drawString("HP " + entity.currentLife + "/" + entity.maxLife, x+gp.tileSize-15, y);
				y+=lineHeight;
				g2.drawString("MP " + entity.currentMana + "/" + entity.maxMana, x+gp.tileSize-15, y);
				
			}			
			
		}
		
	}
	
	
	//Draws the game over screen
	public void drawGameOverScreen(){
		
		g2.setColor(new Color(0,0,0,200));
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x;
		int y;
		String text;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));
		
		text="You are dead";
		
		//Shadow
		g2.setColor(Color.BLACK);
		x=getXforCenteredText(text);
		y=gp.tileSize*4;
		g2.drawString(text, x, y);
		
		//Main
		g2.setColor(Color.white);
		g2.drawString(text, x-4, y-4);
		
		//Retry
		g2.setFont(g2.getFont().deriveFont(50f));
		text="Retry";
		x=getXforCenteredText(text);
		y+=gp.tileSize*4;
		g2.drawString(text, x, y);
		if(pointer==0) {
			
			g2.drawString(">", x-30, y);
			
		}
		
		//Back to the title screen
		text="Quit game";
		x=getXforCenteredText(text);
		y+=55; //Magic number
		g2.drawString(text, x, y);
		if(pointer==1) {
			
			g2.drawString(">", x-30, y);
			
		}
		
		
	}
	
	
	//Draws a subwindow on the screen
	public void drawSubWindow(int x, int y, int width, int height) {
		
		Color c=new Color(11, 0, 9, 220); //Gray
		g2.setColor(c);
		g2.fillRoundRect(x, y, width, height, 35, 35);
		
		//Border
		c=new Color(255, 255, 255);
		g2.setColor(c);
		g2.setStroke(new BasicStroke(5));
		g2.drawRoundRect(x+5, y+5, width-10, height-10, 25, 25);
		
	}
	
	
	//Draws the pause screen
	public void drawPauseScreen() {
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN,80F));
		String text="PAUSED";
		int x=getXforCenteredText(text);
		int y=gp.screenHeight/2;
		
		g2.drawString(text, x, y);		
		
	}
	
	
	//Draws the player menu on the screen
	public void drawPlayerMenuScreen() {
		
		//Create a frame
		final int frameX=gp.tileSize*2;
		final int frameY=gp.tileSize-(gp.tileSize/2);
		final int frameWidth=gp.tileSize*5;
		final int frameHeight=gp.tileSize*11;
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//Draw text
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		int textX=frameX+20;
		int textY=frameY+gp.tileSize;
		final int lineHeight=40;
		
		//Parameters name
		g2.drawString("Level", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Life", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Mana", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Attack", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Defense", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Speed", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Money", textX, textY);
		textY+=lineHeight;
		
		g2.drawString("Exp", textX, textY);
		textY+=lineHeight+15;
		
		g2.drawString("Weapon", textX, textY);
		textY+=lineHeight+10;
		
		g2.drawString("Shield", textX, textY);
		textY+=lineHeight+10;
		
		g2.drawString("Shoes", textX, textY);
		
		//Parameters values
		int tailX=(frameX+frameWidth)-30;
		textY=frameY+gp.tileSize;
		String value;
		
		value=String.valueOf(gp.player.level);
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.currentLife+"/"+gp.player.maxLife);
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.currentMana+"/"+gp.player.maxMana);
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.getAttack());
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.getDefense());
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.getSpeed());
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.money+"€");
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		value=String.valueOf(gp.player.exp);
		textX=getXforAlignToRightText(value, tailX);
		g2.drawString(value, textX, textY);
		textY+=lineHeight;
		
		if(gp.player.currentWeapon!=null) {
			
			g2.drawImage(gp.player.currentWeapon.down1, tailX-gp.tileSize, textY-14, null);
			
		}
		
		textY+=gp.tileSize;	
		
		if(gp.player.currentShield!=null) {
			
			g2.drawImage(gp.player.currentShield.down1, tailX-gp.tileSize, textY-15, null);
			
		}
		
		textY+=gp.tileSize;
		
		if(gp.player.currentShoes!=null) {
			
			g2.drawImage(gp.player.currentShoes.down1, tailX-gp.tileSize, textY-14, null);
			
		}
		
	}
	
	
	//Draws the inventory subwindow
	public void drawInventory(Entity entity, boolean cursor) {
		
		//Frame
		int frameX;
		int frameY;
		int frameWidth;
		int frameHeight;
		int slotCol=0;
		int slotRow=0;
		
		if(entity==gp.player) {
			
			frameX=gp.tileSize*12;
			frameY=gp.tileSize;
			frameWidth=gp.tileSize*6;
			frameHeight=gp.tileSize*5;		
			slotCol=playerSlotCol;
			slotRow=playerSlotRow;
			
		}else {
			
			frameX=gp.tileSize*2;
			frameY=gp.tileSize;
			frameWidth=gp.tileSize*6;
			frameHeight=gp.tileSize*5;		
			slotCol=npcSlotCol;
			slotRow=npcSlotRow;
			
		}
		
		
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		//Slot
		final int slotXstart=frameX+20;
		final int slotYstart=frameY+20;
		int slotX=slotXstart;
		int slotY=slotYstart;
		int slotSize=gp.tileSize+3;
		
		//Draw entity's items
		for(int i=0;i<entity.inventory.size();i++) {
			
			//Equip cursor
			if(entity.inventory.get(i)==entity.currentWeapon ||
					entity.inventory.get(i)==entity.currentShield ||
					entity.inventory.get(i)==entity.currentShoes ||
					entity.inventory.get(i)==entity.currentLight) {
				
				g2.setColor(new Color(240,190,90));
				g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10); 
				
			}
			
			g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
			
			//Display amount
			if(entity==gp.player && entity.inventory.get(i).amount>1) {
				
				g2.setFont(g2.getFont().deriveFont(32f));
				int amountX;
				int amountY;
				
				String s="" + entity.inventory.get(i).amount;
				amountX=getXforAlignToRightText(s, slotX+44);
				amountY=slotY+gp.tileSize;
				
				//Shadow of the number
				g2.setColor(new Color(60,60,60));
				g2.drawString(s, amountX, amountY);
				//Number
				g2.setColor(Color.white);
				g2.drawString(s, amountX-3, amountY);
				
			}
			
			slotX+=slotSize;
			
			if(i==4 || i==9 || i==14) {
				
				slotX=slotXstart;
				slotY+=slotSize;
				
			}
			
		}
		
		//Cursor
		if(cursor) {
								
			int cursorX=slotXstart+(slotSize*slotCol);
			int cursorY=slotYstart+(slotSize*slotRow);
			int cursorWidth=gp.tileSize;
			int cursorHeight=gp.tileSize;
			
			//Draw cursor
			g2.setColor(Color.white);
			g2.setStroke(new BasicStroke(3));
			g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
			
			//Item description frame
			int dFrameX=frameX;
			int dFrameY=frameY+frameHeight;
			int dFrameWidth=frameWidth;
			int dFrameHeight=gp.tileSize*3;		
			
			//Draw description text
			int textX=dFrameX+20;
			int textY=dFrameY+gp.tileSize;
			g2.setFont(g2.getFont().deriveFont(28F));
			
			int itemIndex=getItemIndexOnSlot(slotCol, slotRow);
			
			if(itemIndex<entity.inventory.size()) {
				
				drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
				
				String fullText = "[" + entity.inventory.get(itemIndex).name + "]\n"
				        + entity.inventory.get(itemIndex).description;

				for(String paragraph:fullText.split("\n")) {

				    String line=paragraph;

				    while(line.length()>22) {

				        int lineJumpIndex=line.lastIndexOf(' ', 22);

				        //If there are no spaces, cut the word
				        if(lineJumpIndex==-1) {

				            lineJumpIndex=22;

				        }

				        g2.drawString(line.substring(0, lineJumpIndex), textX, textY);

				        textY+=32;

				        line=line.substring(lineJumpIndex).trim();

				    }

				    //Draw remaining text
				    g2.drawString(line, textX, textY);

				    textY+=32;

				}
				
			}			
			
		}
		
	}
	
	
	//Draws the trade menus
	public void drawTradeScreen() {
		
		switch(subState) {
		
			case 0: trade_select(); break;			
			case 1: trade_buy(); break;			
			case 2: trade_sell(); break;
		
		}
		
		gp.ih.kPressed=false;
		
	}
	
	
	//Displays and manages the option select for the trade system
	public void trade_select() {
		
		npc.dialogueSet=0;
		drawDialogueScreen();
		
		//Options window
		int x=gp.tileSize*15;
		int y=gp.tileSize*4;
		int width=gp.tileSize*3;
		int height=(int)(gp.tileSize*3.5);
		drawSubWindow(x, y, width, height);
		
		//Draw texts
		x+=gp.tileSize;
		y+=gp.tileSize;
		
		//Buy
		if(pointer==0) {
			
			g2.drawString(">", x-24, y);
			
			if(gp.ih.kPressed) {
				
				subState=1;
				
			}
			
		}		
		g2.drawString("Buy", x, y);
		y+=gp.tileSize;
		
		//Sell
		if(pointer==1) {
			
			g2.drawString(">", x-24, y);
			
			if(gp.ih.kPressed) {
				
				subState=2;
				
			}
			
		}		
		g2.drawString("Sell", x, y);
		y+=gp.tileSize;
		
		//Leave
		if(pointer==2) {
			
			g2.drawString(">", x-24, y);
			
			if(gp.ih.kPressed) {
				
				pointer=0;
				npc.startDialogue(npc, 1);
				
			}
			
		}
		g2.drawString("Leave", x, y);
		
	}
	
	
	//Display the trade menus for buying
	public void trade_buy() {
		
		pointer=0;
		
		//Draw player's inventory
		drawInventory(gp.player, false);
		
		//Draw trader's inventory
		drawInventory(npc, true);
		
		//Draw hint window
		int x=gp.tileSize*2;
		int y=gp.tileSize*9;
		int width=gp.tileSize*6;
		int height=gp.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[L] Back", x+24, y+60);
		
		//Draw player coin window
		x=gp.tileSize*12;
		y=gp.tileSize*9;
		width=gp.tileSize*6;
		height=gp.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("Money: "+gp.player.money+"€", x+24, y+60);
		
		//Draw price window
		int itemIndex=getItemIndexOnSlot(npcSlotCol, npcSlotRow);
		if(itemIndex<npc.inventory.size()) {
			
			x=(int)(gp.tileSize*5.5);
			y=(int)(gp.tileSize*5.5);
			width=(int)(gp.tileSize*2.5);
			height=gp.tileSize;
			drawSubWindow(x, y, width, height);
			g2.drawString("€", x+10, y+32);
			
			int price=npc.inventory.get(itemIndex).price;
			String text=""+price;
			x=getXforAlignToRightText(text, gp.tileSize*8-20);
			g2.drawString(text, x, y+32);
			
			
			//Buy item
			if(gp.ih.kPressed) {
				
				//Not enough money
				if(price>gp.player.money) {
					
					//Maybe I would like it better to just play a sound here
					pointer=0;
					subState=0;
					npc.startDialogue(npc, 2);
					
				}
				
				//Succesfull purchase
				else if(gp.player.canObtainItem(npc.inventory.get(itemIndex))) {
					
					gp.player.money-=price;
					gp.playSound("buy");
					
				}
				
				//Inventory full
				else {
					
					pointer=0;
					subState=0;
					npc.startDialogue(npc, 3);
					
				}
				
			}
			
		}
		
	}
	
	
	//Manages the screen for the trading and selling state
	public void trade_sell() {
		
		pointer=0;
		
		//Draw player's inventory
		drawInventory(gp.player, true);
		
		
		//Draw hint window
		int x=gp.tileSize*2;
		int y=gp.tileSize*9;
		int width=gp.tileSize*6;
		int height=gp.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("[L] Back", x+24, y+60);
		
		//Draw player coin window
		x=gp.tileSize*12;
		y=gp.tileSize*9;
		width=gp.tileSize*6;
		height=gp.tileSize*2;
		drawSubWindow(x, y, width, height);
		g2.drawString("Money: "+gp.player.money+"€", x+24, y+60);
		
		//Draw price window
		int itemIndex=getItemIndexOnSlot(playerSlotCol, playerSlotRow);
		if(itemIndex<gp.player.inventory.size()) {
			
			x=(int)(gp.tileSize*15.5);
			y=(int)(gp.tileSize*5.5);
			width=(int)(gp.tileSize*2.5);
			height=gp.tileSize;
			drawSubWindow(x, y, width, height);
			g2.drawString("€", x+10, y+32);
			
			int price=gp.player.inventory.get(itemIndex).price/2;
			if(price<1) {price=1;}
			String text=""+price;
			x=getXforAlignToRightText(text, gp.tileSize*18-20);
			g2.drawString(text, x, y+32);
			
			
			//Sell item
			if(gp.ih.kPressed) {
				
				//Equiped item (can't sell)
				if(gp.player.inventory.get(itemIndex)==gp.player.currentWeapon ||
						gp.player.inventory.get(itemIndex)==gp.player.currentShield ||
						gp.player.inventory.get(itemIndex)==gp.player.currentLight) {
					
					pointer=0;
					subState=0;
					gp.gameState=GAMESTATE.DIALOGUESTATE;
					npc.startDialogue(npc, 4);
					
				}
				
				//Sucessfull sell
				else {
					
					if(gp.player.inventory.get(itemIndex).amount>1) {
						
						gp.player.inventory.get(itemIndex).amount--;
						
					}else {
						
						gp.player.inventory.remove(itemIndex);
						
					}
					
					gp.playSound("buy");
					gp.player.money+=price;
					
				}
				
			}
			
		}
		
	}
	
	
	//Draws the options menu
	public void drawOptionsScreen() {
		
		g2.setColor(Color.white);
		g2.setFont(g2.getFont().deriveFont(32F));
		
		//Sub window
		int frameX=gp.tileSize*6;;
		int frameY=gp.tileSize;
		int frameWidth=gp.tileSize*8;
		int frameHeight=gp.tileSize*10;
		drawSubWindow(frameX, frameY, frameWidth, frameHeight);
		
		switch(subState) {
		
			case 0: optionsTop(frameX, frameY);	break;			
			case 1:	optionsFullScreenNotification(frameX, frameY); break;			
			case 2: optionsControl(frameX, frameY); break;			
			case 3: quitGameConfirmation(frameX, frameY); break;
		
		}
		
		gp.ih.kPressed=false;
		
	}
	
	
	//Handles the options menu
	public void optionsTop(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		//Title
		String text="Options";
		textX=getXforCenteredText(text);
		textY=frameY+gp.tileSize;
		g2.drawString(text, textX, textY);
		
		//Full screen on/off
		textX=frameX+gp.tileSize;
		textY+=gp.tileSize*2;
		g2.drawString("Full screen", textX, textY);
		if(pointer==0) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed==true) {
				
				gp.fullScreen=!gp.fullScreen;
				
				subState=1;
				
			}
			
		}
		
		//Music
		textY+=gp.tileSize;
		g2.drawString("Music", textX, textY);
		if(pointer==1) {
			
			g2.drawString(">", textX-25, textY);
			
		}
		
		//Se
		textY+=gp.tileSize;
		g2.drawString("SE", textX, textY);
		if(pointer==2) {
			
			g2.drawString(">", textX-25, textY);
			
		}
		
		//Control
		textY+=gp.tileSize;
		g2.drawString("Control", textX, textY);
		if(pointer==3) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed) {
				
				subState=2;
				pointer=0;
				
			}
			
		}
		
		//Quit game
		textY+=gp.tileSize;
		g2.drawString("Quit game", textX, textY);
		if(pointer==4) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed) {
				
				subState=3;
				pointer=0;
				
			}
			
		}
		
		//Close options window
		textY+=gp.tileSize*2;
		g2.drawString("Back", textX, textY);
		if(pointer==5) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed) {
				
				gp.gameState=GAMESTATE.PLAYSTATE;
				pointer=0;
				
			}
			
		}
		
		//Full screen check box
		textX=frameX+(int)(gp.tileSize*4.5);
		textY=frameY+gp.tileSize*2+gp.tileSize/2;
		g2.setStroke(new BasicStroke(3));
		g2.drawRect(textX, textY, gp.tileSize/2, gp.tileSize/2);
		
		if(gp.fullScreen) {
			
			g2.fillRect(textX, textY, gp.tileSize/2, gp.tileSize/2);
			
		}
		
		//Music volume
		textY+=gp.tileSize;
		g2.drawRect(textX, textY, 120, gp.tileSize/2); //120/5=24
		int volumeWidth=24*gp.music.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		//SE volume
		textY+=gp.tileSize;
		g2.drawRect(textX, textY, 120, gp.tileSize/2);
		volumeWidth=24*gp.se.volumeScale;
		g2.fillRect(textX, textY, volumeWidth, 24);
		
		gp.config.saveConfig();
		
	}
	
	
	//Shows a notification about the full screen mode
	public void optionsFullScreenNotification(int frameX, int frameY) {
		
		int textX=frameX+gp.tileSize;
		int textY=frameY+gp.tileSize*3;
		
		currentDialogue="The change will take \neffect after restarting \nthe game";
		
		for(String line:currentDialogue.split("\n")) {
			
			g2.drawString(line, textX, textY);
			textY+=40;
			
		}
		
		//Back
		textY=frameY+gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		
		//Cursor
		if(pointer==0) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed) {
				
				subState=0;
				
			}
			
		}
		
	}
	
	
	//Shows the game controls on screen
	public void optionsControl(int frameX, int frameY) {
		
		int textX;
		int textY;
		
		//Title
		String text="Controls";
		textX=getXforCenteredText(text);
		textY=frameY+gp.tileSize;
		
		textX=frameX+gp.tileSize;
		textY+=gp.tileSize;		
		g2.drawString("Move", textX, textY); textY+=gp.tileSize;
		g2.drawString("Check/Confirm", textX, textY); textY+=gp.tileSize;
		g2.drawString("Inventory", textX, textY); textY+=gp.tileSize;
		g2.drawString("Pause", textX, textY); textY+=gp.tileSize;
		g2.drawString("Options", textX, textY); textY+=gp.tileSize;
		
		textX=frameX+gp.tileSize*6;
		textY=frameY+gp.tileSize*2;
		g2.drawString("WASD", textX, textY); textY+=gp.tileSize;
		g2.drawString("K", textX, textY); textY+=gp.tileSize;
		g2.drawString("L", textX, textY); textY+=gp.tileSize;
		g2.drawString("P", textX, textY); textY+=gp.tileSize;
		g2.drawString("ESC", textX, textY); textY+=gp.tileSize;
		
		//Back
		textX=frameX+gp.tileSize;
		textY=frameY+gp.tileSize*9;
		g2.drawString("Back", textX, textY);
		
		if(pointer==0) {
			
			g2.drawString(">", textX-25, textY);
			if(gp.ih.kPressed) {
				
				subState=0;
				pointer=3;
				
			}
			
		}
		
	}
	
	
	//Asks the player for confirmation to close the game
	public void quitGameConfirmation(int frameX, int frameY) {
		
		int textX=frameX+gp.tileSize;
		int textY=frameY+gp.tileSize*3;
		
		currentDialogue="Are you sure you want\n to quit the game?";
		
		for(String line: currentDialogue.split("\n")) {
			
			g2.drawString(line, textX, textY);
			textY+=40;
			
		}
		
		//Yes option
		String text="Yes";
		textX=getXforCenteredText(text);
		textY+=gp.tileSize*3;
		g2.drawString(text, textX, textY);
		if(pointer==0) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed) {
				
				subState=0;
				gp.gameState=GAMESTATE.TITLESTATE;
				gp.stopMusic();
				
			}
			
		}
		
		//No option
		text="No";
		textX=getXforCenteredText(text);
		textY+=gp.tileSize;
		g2.drawString(text, textX, textY);
		if(pointer==1) {
			
			g2.drawString(">", textX-25, textY);
			
			if(gp.ih.kPressed) {
				
				subState=0;
				pointer=4;
				
			}
			
		}
		
	}
	
	
	//Draws the sleep animation
	public void drawSleepScreen() {
		
		counter++;
		
		if(counter<120) {
			
			gp.em.lighting.filterAlpha+=0.01f;
			
			if(gp.em.lighting.filterAlpha>1f) {
				
				gp.em.lighting.filterAlpha=1f;
				
			}
			
		}
		
		if(counter>=120) {
			
			gp.em.lighting.filterAlpha-=0.01f;
			
			if(gp.em.lighting.filterAlpha<=0f) {
				
				gp.em.lighting.filterAlpha=0f;
				counter=0;
				gp.em.lighting.dayState=gp.em.lighting.day;
				gp.em.lighting.dayCounter=0;
				gp.gameState=GAMESTATE.PLAYSTATE;
				gp.player.getPlayerImage();
				gp.music.resume();
				
			}
			
		}
		
	}
	
	
	//Returns the current index of the entity's inventory
	public int getItemIndexOnSlot(int slotCol, int slotRow) {return slotCol+(slotRow*5);}
	
	
	//Gets the X value for a text to be in the center of the screen
	public int getXforCenteredText(String text) {
		
		int length=(int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return gp.screenWidth/2-length/2;
		
	}
	
	
	//Gets the X value for a text to be in the right of a frame
	public int getXforAlignToRightText(String text, int tailX) {
		
		int length=(int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		return tailX-length;
		
	}
	
	
	
}






























