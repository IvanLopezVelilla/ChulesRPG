package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JPanel;
import ai.PathFinder;
import combat.CombatEntity;
import combat.CombatManager;
import data.SaveLoad;
import database.DatabaseManager;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import tile.Map;
import tile.TileManager;
import sound.SoundEffect;
import sound.MusicPlayer;

public class GamePanel extends JPanel implements Runnable{
	/*
	 * GAME PANEL
	 */
	
	private static final long serialVersionUID = 1L;
	
	//World settings
	public final int maxScreenCol=20;
	public final int maxScreenRow=12;
	
	//Screen settings
	final int originalTileSize=16; //16x16 size
	final int scale=3;
	
	public final int tileSize=originalTileSize*scale; //48x48
	public final int screenWidth=tileSize*maxScreenCol; //960px
	public final int screenHeight=tileSize*maxScreenRow; //576px
	
	//Full screen
	int screenWidth2=screenWidth;
	int screenHeight2=screenHeight;
	BufferedImage tempScreen;
	Graphics2D g2;
	public boolean fullScreen=false;
	
	//Variables and handlers
	public TileManager tm=new TileManager(this);
	public InputHandler ih=new InputHandler(this);
	public CollisionManager cm=new CollisionManager(this);
	public EventHandler eh=new EventHandler(this);
	int FPS=60;
	public MusicPlayer music=new MusicPlayer();
	SoundEffect se=new SoundEffect();
	public UI ui=new UI(this);
	Thread gameThread;
	Config config=new Config(this);
	public PathFinder pf=new PathFinder(this);
	EnvironmentManager em=new EnvironmentManager(this);
	Map map=new Map(this);
	SaveLoad sl=new SaveLoad(this);
	public EntityGenerator eg=new EntityGenerator(this);
	public CombatManager combatM=new CombatManager(this);
	public DatabaseManager db=new DatabaseManager();
	
	
	//Game states
	public GAMESTATE gameState;
	
	
	//worldmap settings
	public final int maxWorldCol=50;//Can adjust it if needed to 100 or 150
	public final int maxWorldRow=50;
	public final int worldWidth=tileSize*maxWorldCol;
	public final int worldHeight=tileSize*maxWorldRow;
	public final int maxMap=10;
	public int currentMap=0;
	
	
	//Area
	public AREA currentArea;
	public AREA nextArea;
	
	
	//Entities and items
	public Player player=new Player(this, ih);
	public CombatEntity playerRpg;
	public AssetSetter as=new AssetSetter(this);
	public Entity items[][]=new Entity[maxMap][30]; //Item class array, size is hardcoded (maybe make it arrayList in the future)
	public Entity npcs[][]=new Entity[maxMap][10]; //Size is not the total amount of entities in the game, but rather amount of entities displayed at the same time on screen
	public Entity enemies[][]=new Entity[maxMap][20];
	ArrayList<Entity> entityList=new ArrayList<>();	
	
	
	//Main Thread
	public void start() {
		
		//Maybe move setupGame() method here
		gameThread=new Thread(this);
		gameThread.start();
		
	}
	
	
	//Constructor
	public GamePanel() {
		
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.gray);
		this.setDoubleBuffered(true);
		this.addKeyListener(ih);
		this.setFocusable(true);
		
	}
	
	
	//Setups the game assets
	public void setupGame() {
		
		as.setItem();
		as.setNPC();
		as.setEnemy();
		em.setup();
		loadSound();
		
		gameState=GAMESTATE.TITLESTATE;
		currentArea=AREA.MAINLOBBY;
		
		tempScreen=new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2=(Graphics2D)tempScreen.getGraphics();
		
		if(fullScreen) {
			
			setFullScreen();
			
		}
		
	}
	
	
	//Retrys the game
	public void retry() {
		
		gameState=GAMESTATE.PLAYSTATE;
		ui.resetCombatDialogue();
		
		currentArea=AREA.MAINLOBBY;
		stopMusic();
		playNextAreaMusic();
		currentMap=0;
		
		player.getPlayerImage();
		player.setDefaultPositions();
		player.restoreLifeAndMana();
		player.name=player.name.substring(4);
		
		as.setNPC();
		as.setEnemy();
		
	}
	
	
	//Restarts the game
	public void restart() {
		
		gameState=GAMESTATE.TITLESTATE;
		ui.resetCombatDialogue();
		
		player.setDefaultPositions();
		player.setDefaultValues();
		player.restoreLifeAndMana();
		player.setItems();
		
		as.setItem();
		as.setNPC();
		as.setEnemy();
		
	}
	
	
	//Loads all the sounds
	public void loadSound() {
		
		se.load("coin", "/sounds/coin.wav");
		se.load("1up", "/sounds/1up.wav");
		se.load("door", "/sounds/door.wav");
		se.load("victoryTheme", "/sounds/victoryTheme.wav");
		se.load("cursor", "/sounds/cursor.wav");
		se.load("click", "/sounds/click.wav");
		se.load("stairs", "/sounds/stairs.wav");
		se.load("sleep", "/sounds/sleep.wav");
		se.load("speak", "/sounds/speak.wav");
		se.load("buy", "/sounds/buy.wav");
		se.load("levelUp", "/sounds/levelUp.wav");
		
	}
	
	
	//Sets the full screen for the game
	public void setFullScreen() {
		
		//Get local screen device
		GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd=ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Main.window);
		
		//get full screen width and height
		screenWidth2=Main.window.getWidth();
		screenHeight2=Main.window.getHeight();
		
	}
	
	
	//Creates a CombatEntity from an entity
	public CombatEntity newCombatEntity(Entity entity) {return new CombatEntity(entity.name, entity.maxLife, entity.currentLife, entity.maxMana, entity.currentMana, entity.level, entity.naturalAttack, entity.naturalDefense, entity.naturalSpeed, entity.currentWeapon, entity.currentShield, entity.currentShoes);}

	
	//Update the game logic
	public void update() {
		
		if(gameState==GAMESTATE.PLAYSTATE) {
			
			player.update();
			
			//NPCs
			for(int i=0;i<npcs[currentMap].length;i++) {
				
				if(npcs[currentMap][i]!=null) {
					
					npcs[currentMap][i].update();
					
				}
				
			}
			
			//Enemies
			for(int i=0;i<enemies[currentMap].length;i++) {
				
				if(enemies[currentMap][i]!=null && !enemies[currentMap][i].defeated) {
					
					enemies[currentMap][i].update();
					
				}
				
			}
			
			em.update();
			
		}
		
	}
	
	
	//Changes the current game area
	public void changeArea() {
		
		if(nextArea!=currentArea) {
			
			stopMusic();
			
			switch(nextArea) {
			
				case MAINLOBBY: playMusic("Fantavision"); break;
				case SHOP: playMusic("merchantMusicWithCopyrightThatIshouldProbablyChange"); break;
				case DUNGEON: playMusic("dungeon"); break;
				case SNOW: playMusic("snow"); break;
				case FABRIK: playMusic("fabrik"); break;
			
			}
			
		}
		
		currentArea=nextArea; //Check EventHandler teleport() if want to add transition effect. Should call this in UI #52 //No past me, you should never put logic in UI, now we're fucked
		as.setEnemy();
		
	}
	
	
	//Draws the game to a temporal screen for size adjust
	public void drawToTempScreen() {
		
		//Clear screen
		g2.setColor(Color.GRAY);
		g2.fillRect(0, 0, screenWidth, screenHeight);
		
		//Get draw time
		long drawStart=0;
		if(ih.showDebugText) {		
			
			drawStart=System.nanoTime(); 			
			
		}
		
		
		//Title screen
		if(gameState==GAMESTATE.TITLESTATE) {
			
			ui.draw(g2);
			
		}
		
		//Map screen
		else if(gameState==GAMESTATE.MAPSTATE) {
			
			map.drawFullMap(g2);
			
		}
		
		else {
			
			//Tiles
			tm.draw(g2);
			

			//Entities
			//Add entities to the arrayList
			entityList.add(player);
			for(Entity e : npcs[currentMap]) { if(e != null) entityList.add(e); }
			for(Entity e : items[currentMap]) { if(e != null) entityList.add(e); }
			for(Entity e : enemies[currentMap]) { if(e != null) entityList.add(e); }
			
			//Sort by worldY from lowest to higher
			Collections.sort(entityList, new Comparator<Entity>() {

				@Override
				public int compare(Entity e1, Entity e2) {
					
					int result=Integer.compare(e1.worldY, e2.worldY);
					return result;
					
				}
				
			});
			
			//Draw entities
			for(int i=0;i<entityList.size();i++) {
				
				if(!entityList.get(i).defeated) entityList.get(i).draw(g2);
				
			}
			
			//Empty list
			entityList.clear();
						
		}
		
		//Mini map
		map.drawMinimap(g2);
		
		//Environment
		if(gameState!=GAMESTATE.MAPSTATE) {
			
			em.draw(g2);
			
		}
		
		//UI
		ui.draw(g2);

		
		//Get and show draw time if T is pressed
		//For now I don't calculate the draw time if it's not shown to the player
		//Maybe I'll add it to some game function
		if(ih.showDebugText) {
			
			long drawEnd=System.nanoTime();
			long passed=drawEnd-drawStart;
						
			g2.setFont(new Font("Arial", Font.PLAIN, 24));
			g2.setColor(Color.white);
			int x=10;
			int y=400;
			int lineHeight=20;
			
			g2.drawString("GameState: "+gameState, x, y);
			y+=lineHeight;
			
			g2.drawString("WorldX: "+player.worldX, x, y);
			y+=lineHeight;
			
			g2.drawString("WorldY: "+player.worldY, x, y);
			y+=lineHeight;
			
			g2.drawString("Col: "+(player.worldX+player.solidArea.x)/tileSize, x, y);
			y+=lineHeight;
			
			g2.drawString("Row: "+(player.worldY+player.solidArea.y)/tileSize, x, y);
			y+=lineHeight;
			
			g2.drawString("Draw time: " + passed, x, y);
			y+=lineHeight;
			
			g2.drawString("Pointer: " + ui.pointer, x, y);
			
		}
		
	}
	
	
	//Draws the game into the screen
	public void drawToScreen() {
		
		Graphics g=getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
		g.dispose();
		
	}
	
	
	//Plays the background music on a loop, default volume
	public void playMusic(String name) {
		
		music.play(name);
		
	}
	
	
	//Stops the background music
	public void stopMusic() {
		
		music.stop();
		
	}
	
	
	//Plays a sound effect, default volume
	public void playSound(String name) {
		
		se.play(name);
		
	}
	
	
	//Plays the right sound for the next area
	public void playNextAreaMusic() {
		
		stopMusic();
		
		switch(currentArea) {
		
			case MAINLOBBY: playMusic("Fantavision"); break;
			case SHOP: playMusic("merchantMusicWithCopyrightThatIshouldProbablyChange"); break;
			case DUNGEON: playMusic("dungeon"); break;
			case SNOW: playMusic("snow"); break;
			case FABRIK: playMusic("fabrik"); break;
	
		}
		
	}
	
	
	//Keeps updating the game logic and drawing on the screen 
	public void run() {
		
		double drawInterval=1000000000/FPS;
		double delta=0;
		long lastTime=System.nanoTime();
		long currentTime;
		
		long timer=0;
		//int drawCount=0;
		
		while(gameThread!=null) {
			
			currentTime=System.nanoTime();
			
			delta+=(currentTime-lastTime)/drawInterval;
			timer+=(currentTime-lastTime);
			
			lastTime=currentTime;
			
			if(delta>=1) {
				
				update();
				drawToTempScreen(); //Draws everything to the buffered image
				drawToScreen(); //Draws the buffered image to the screen
				delta--;
				//drawCount++;
				
			}
			
			//Check fps
			if(timer>=1000000000) {
				
				//System.out.println("FPS: "+drawCount); //Debug
				//drawCount=0;
				timer=0;
				
			}
				
		}
		
	}
}
