package tile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.UtilityTool;

public class TileManager {
	/*
	 * The manager for the tiles and their drawing
	 */
	
	GamePanel gp;
	public Tile[] tile; //Array for each unique tile of the game
	public int mapTile[][][]; //Array with the coordenates of each tile of the map
	public String currentMap;
	boolean drawPath=false; //Debug purposes
	
	
	//Constructor
	public TileManager(GamePanel gp) {
		
		this.gp=gp;
		
		tile=new Tile[24]; //This array length mirrors the amount of different tiles (must hardcode if making or deleting tiles)
		mapTile=new int[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap("/maps/mainLoby.txt", 0);
		loadMap("/maps/shop.txt", 1);
		loadMap("/maps/dungeon01.txt", 2);
		loadMap("/maps/snow.txt", 3);
		loadMap("/maps/fabrik.txt", 4);
		
	}
	
	
	//Loads the tile images to the tile array
	public void getTileImage() {
		
		setup(0, "002.png", true);
		setup(1, "015.png", false);
		setup(2, "007.png", false);
		setup(3, "017.png", false);
		setup(4, "010.png", true);
		setup(5, "001.png", true);
		setup(6, "021.png", false);
		setup(7, "013.png", true);
		setup(8, "022.png", false);
		setup(9, "003.png", false);
		setup(10, "023.png", false);
		setup(11, "012.png", true);
		setup(12, "000.png", false);
		setup(13, "020.png", false);
		setup(14, "008.png", false);
		setup(15, "016.png", true);
		setup(16, "004.png", false);
		setup(17, "014.png", false);
		setup(18, "018.png", true);
		setup(19, "006.png", true);
		setup(20, "005.png", true);
		setup(21, "009.png", false);
		setup(22, "011.png", true);
		setup(23, "019.png", true);
		
	}
	
	
	//Setups a tile by scaling it to tile size
	public void setup(int index, String imagePath, boolean collision) {
		
		UtilityTool ut=new UtilityTool();
		
		tile[index]=new Tile();
		try {
			
			tile[index].image=ImageIO.read(getClass().getResourceAsStream("/tiles/"+imagePath));
			tile[index].image=ut.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
			tile[index].collision=collision;
			//Draw pathFinding for npcs if needed
			if(drawPath) {
				
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	//Loads a map (txt) as numbers (cols and rows) in the mapTile array
	public void loadMap(String filePath, int map) {
		
		try {
			
			this.currentMap=filePath;
			
			InputStream is=getClass().getResourceAsStream(filePath);
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			
			int col=0;
			int row=0;
			
			while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
				
				String line=br.readLine();
				
				while(col<gp.maxWorldCol) {
					
					String numbers[]=line.split(" ");
					mapTile[map][col][row]=Integer.parseInt(numbers[col]);
					
					col++;
					
				}
				
				if(col==gp.maxWorldCol) {
					
					col=0;
					row++;
					
				}
				
			}
			
			br.close();
			is.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	//Draws the tiles on the screen
	public void draw(Graphics2D g2) {
		
		int worldCol=0;
		int worldRow=0;
		
		while(worldCol<gp.maxWorldCol && worldRow<gp.maxWorldRow) {
			
			int tileNum=mapTile[gp.currentMap][worldCol][worldRow];
			
			int worldX=worldCol*gp.tileSize;
			int worldY=worldRow*gp.tileSize;
			int screenX=worldX-gp.player.worldX+gp.player.screenX;
			int screenY=worldY-gp.player.worldY+gp.player.screenY;
			
			
			//Stop moving the camera at the edge of the world
			//THERE IS AN ERROR
			//The solid area does not follow the player
			if(gp.player.screenX>gp.player.worldX) {
				
				screenX=worldX;
				
			}
			if(gp.player.screenY>gp.player.worldY) {
				
				screenY=worldY;
				
			}
			int rightOffset=gp.screenWidth-gp.player.screenX;
			if(rightOffset>gp.worldWidth-gp.player.worldX) {
				
				screenX=gp.screenWidth-(gp.worldWidth-worldX);
				
			}
			int bottomOffset=gp.screenHeight-gp.player.screenY;
			if(bottomOffset>gp.worldHeight-gp.player.worldY) {
				
				screenY=gp.screenHeight-(gp.worldHeight-worldY);
				
			}
			
			
			//Only draws the tiles that the user can see
			if(screenX+gp.tileSize>0 &&
				screenX<gp.screenWidth &&
			    screenY+gp.tileSize>0 &&
			    screenY<gp.screenHeight+gp.tileSize) {
				
				g2.drawImage(tile[tileNum].image, screenX, screenY, null);
				
			}
			
			
			worldCol++;
			
			if(worldCol==gp.maxWorldCol) {
				
				worldCol=0;
				worldRow++;
				
			}
			
		}
		
		//Draw pathFinding for npcs if needed
		if(drawPath) {
			
			g2.setColor(new Color(255,0,0,70));
			
			for(int i=0;i<gp.pf.pathList.size();i++) {
				
				int worldX=gp.pf.pathList.get(i).col*gp.tileSize;
				int worldY=gp.pf.pathList.get(i).row*gp.tileSize;
				int screenX=worldX-gp.player.worldX+gp.player.screenX;
				int screenY=worldY-gp.player.worldY+gp.player.screenY;
				
				g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
			}
			
		}
		
	}
	
}























