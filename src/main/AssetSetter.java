package main;

import java.awt.image.BufferedImage;

import database.Asset;
import entity.Entity;
import entity.NPC_HatCreature;
import entity.NPC_Oldman;
import entity.NPC_Vega;
import entity.TYPE;
import item.ITM_8potion;
import item.ITM_Axe;
import item.ITM_BetterShield;
import item.ITM_Chest;
import item.ITM_Coin;
import item.ITM_Door;
import item.ITM_Key;
import item.ITM_Lantern;
import item.ITM_Tent;

public class AssetSetter {
	/*
	 * Manage the assest of the items
	 */
	
	GamePanel gp;
	
	
	public AssetSetter(GamePanel gp) {
		
		this.gp=gp;
		
	}
	
	
	//Sets items on the item aray
	public void setItem() {
		
		int i=0;
		
		//0 Main lobby
		gp.items[0][i]=new ITM_Door(gp);
		gp.items[0][i].worldX=gp.tileSize*13;
		gp.items[0][i].worldY=gp.tileSize*8;
		i++;
		
		gp.items[0][i]=new ITM_Door(gp);
		gp.items[0][i].worldX=gp.tileSize*29;
		gp.items[0][i].worldY=gp.tileSize*7;
		i++;
		
		gp.items[0][i]=new ITM_Door(gp);
		gp.items[0][i].worldX=gp.tileSize*30;
		gp.items[0][i].worldY=gp.tileSize*7;
		i++;
		
		gp.items[0][i]=new ITM_Key(gp);
		gp.items[0][i].worldX=gp.tileSize*15;
		gp.items[0][i].worldY=gp.tileSize*14;
		i++;
		
		gp.items[0][i]=new ITM_Key(gp);
		gp.items[0][i].worldX=gp.tileSize*45;
		gp.items[0][i].worldY=gp.tileSize*13;
		i++;
		
		gp.items[0][i]=new ITM_Key(gp);
		gp.items[0][i].worldX=gp.tileSize*45;
		gp.items[0][i].worldY=gp.tileSize*14;
		i++;
		
		gp.items[0][i]=new ITM_Lantern(gp);
		gp.items[0][i].worldX=gp.tileSize*11;
		gp.items[0][i].worldY=gp.tileSize*13;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*19;
		gp.items[0][i].worldY=gp.tileSize*8;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*21;
		gp.items[0][i].worldY=gp.tileSize*8;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*18;
		gp.items[0][i].worldY=gp.tileSize*10;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*19;
		gp.items[0][i].worldY=gp.tileSize*11;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*20;
		gp.items[0][i].worldY=gp.tileSize*11;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*21;
		gp.items[0][i].worldY=gp.tileSize*11;
		i++;
		
		gp.items[0][i]=new ITM_Coin(gp);
		gp.items[0][i].worldX=gp.tileSize*22;
		gp.items[0][i].worldY=gp.tileSize*10;
		i++;
		
		gp.items[0][i]=new ITM_Chest(gp);
		gp.items[0][i].setLoot(new ITM_BetterShield(gp));
		gp.items[0][i].worldX=gp.tileSize*39;
		gp.items[0][i].worldY=gp.tileSize*1;
		i++;
		
				
		//2 Dungeon
		gp.items[2][i]=new ITM_Chest(gp);
		gp.items[2][i].setLoot(new ITM_Axe(gp));
		gp.items[2][i].worldX=gp.tileSize*36;
		gp.items[2][i].worldY=gp.tileSize*30;
		i++;
		
		gp.items[2][i]=new ITM_Chest(gp);
		gp.items[2][i].setLoot(new ITM_Tent(gp));
		gp.items[2][i].worldX=gp.tileSize*30;
		gp.items[2][i].worldY=gp.tileSize*38;
		i++;
		
		gp.items[2][i]=new ITM_Chest(gp);
		gp.items[2][i].setLoot(new ITM_8potion(gp));
		gp.items[2][i].worldX=gp.tileSize*39;
		gp.items[2][i].worldY=gp.tileSize*40;
		i++;
		
		gp.items[2][i]=new ITM_Chest(gp);
		gp.items[2][i].setLoot(new ITM_8potion(gp));
		gp.items[2][i].worldX=gp.tileSize*40;
		gp.items[2][i].worldY=gp.tileSize*40;
		i++;
		

		//3 Snow
		
		
		//4 Fabrik

		
	}
	
	
	//Sets npcs on the npc array
	public void setNPC() {

		//0 Main lobby
		gp.npcs[0][0]=new NPC_Oldman(gp);
		gp.npcs[0][0].worldX=gp.tileSize*7;
		gp.npcs[0][0].worldY=gp.tileSize*7;
		
		//1 Shop
		gp.npcs[1][0]=new NPC_HatCreature(gp);
		gp.npcs[1][0].worldX=gp.tileSize*24;
		gp.npcs[1][0].worldY=gp.tileSize*13;

		 
		// 2 Dungeon
		
		 
		//3 Snow
		gp.npcs[3][0]=new NPC_Vega(gp);
		gp.npcs[3][0].worldX=gp.tileSize*14;
		gp.npcs[3][0].worldY=gp.tileSize*38;
		
		
		//4 Fabrik
		
		
		
	}
	
	
	//Sets enemies on the enemy array
	public void setEnemy() {
		
		int map;
		int i=0;
		
		//2 Dungeon
		map=2;
		i=0;
		
		loadEnemy(map, i, 22, 37);
		i++;
		
		
		loadEnemy(map, i, 27, 11);
		i++;	
		
		
		//3 Snow
		map=3;
		i=0;
		
		loadEnemy(map, i, 21, 24);
		i++;
		
		
		loadEnemy(map, i, 28, 39);
		i++;
		
		
		//4 Fabrik
		map=4;
		i=0;
		loadEnemy(map, i, 42, 7);
		i++;
		
		
		loadEnemy(map, i, 39, 34);
		i++;
		
				
	}
	
	
	//Looks for an asset that is an enemy type and loads it
	public void loadEnemy(int map, int position, int worldX, int worldY) {
		
		Asset asset=gp.db.getRandomAsset();
		
		while(asset.category!=2) asset=gp.db.getRandomAsset();
		
		gp.enemies[map][position]=newEnemy(asset);
		gp.enemies[map][position].worldX=worldX*gp.tileSize;
		gp.enemies[map][position].worldY=worldY*gp.tileSize;
		
	}
	
	
	//Loads and returns a new enemy from an asset
	public Entity newEnemy(Asset asset) {
		
		Entity enemy=new Entity(gp);
		
		enemy.name=asset.name;
		enemy.level=gp.player.level;
		enemy.worldSpeed=1;
		enemy.maxLife=asset.health;
		enemy.currentLife=enemy.maxLife;
		enemy.maxMana=30;
		enemy.currentMana=30;
		enemy.naturalAttack=asset.damage;
		enemy.naturalDefense=asset.defense;
		enemy.naturalSpeed=asset.speed;
		enemy.attack=enemy.getAttack();
		enemy.defense=enemy.getDefense();
		enemy.rpgSpeed=enemy.getSpeed();
		
		enemy.hasCollisions=true;
		enemy.type=TYPE.ENEMY;
		enemy.exp=2*gp.player.level+(gp.player.nextLevelExp/2);
		enemy.money=7+(gp.player.money/2);
		
		enemy.solidArea.x=3;
		enemy.solidArea.y=18;
		enemy.solidArea.width=42;
		enemy.solidArea.height=30;
		enemy.solidAreaDefaultX=enemy.solidArea.x;
		enemy.solidAreaDefaultY=enemy.solidArea.y;
		
		//Images
		enemy.up1=setup(gp.db.loader.loadImage(asset.files.get(0).image));
		enemy.up2=setup(gp.db.loader.loadImage(asset.files.get(1).image));
		enemy.right1=setup(gp.db.loader.loadImage(asset.files.get(6).image));
		enemy.right2=setup(gp.db.loader.loadImage(asset.files.get(7).image));
		enemy.left1=setup(gp.db.loader.loadImage(asset.files.get(4).image));
		enemy.left2=setup(gp.db.loader.loadImage(asset.files.get(5).image));
		enemy.down1=setup(gp.db.loader.loadImage(asset.files.get(2).image));
		enemy.down2=setup(gp.db.loader.loadImage(asset.files.get(3).image));
		
		return enemy;
		
	}
	
	
	//Setups an image
	public BufferedImage setup(BufferedImage image) {
		
		UtilityTool ut=new UtilityTool();		
		image=ut.scaleImage(image, gp.tileSize, gp.tileSize);
		return image;
		
	}
	
	
	
	
	
	
}


















