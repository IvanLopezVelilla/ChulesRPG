package data;

import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.FileInputStream;

import main.GamePanel;

public class SaveLoad {

	GamePanel gp;
	
	public SaveLoad(GamePanel gp) {
		
		this.gp=gp;
		
	}
	
	
	//Saves the current game data
	public void save() {
		
		try {
			
			ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
			
			DataStorage ds=new DataStorage();
			
			ds.level=gp.player.level;
			ds.maxLife=gp.player.maxLife;
			ds.currentLife=gp.player.currentLife;
			ds.naturalAttack=gp.player.naturalAttack;//Maybe I could get this int by level calculation
			ds.naturalDefense=gp.player.naturalDefense;
			ds.money=gp.player.money;
			ds.exp=gp.player.exp;
			ds.nextLevelExp=gp.player.nextLevelExp;
			ds.attack=gp.player.attack;
			ds.defense=gp.player.defense;
			
			//Player inventory
			for(int i=0;i<gp.player.inventory.size();i++) {
				
				ds.itemNames.add(gp.player.inventory.get(i).name);
				ds.itemAmounts.add(gp.player.inventory.get(i).amount);
				
			}
			
			//Player equipment
			if(gp.player.currentWeapon!=null) ds.currentWeaponSlot=gp.player.getCurrentWeaponSlot(); else ds.currentWeaponSlot=-1;
			if(gp.player.currentShield!=null) ds.currentShieldSlot=gp.player.getCurrentShieldSlot(); else ds.currentShieldSlot=-1;
			if(gp.player.currentLight!=null) ds.currentLightSlot=gp.player.getCurrentLightSlot(); else ds.currentLightSlot=-1;
			if(gp.player.currentShoes!=null) ds.currentShoesSlot=gp.player.getCurrentShoesSlot(); else ds.currentShoesSlot=-1;
			
			//Items on map
			ds.mapItemNames=new String[gp.maxMap][gp.items[1].length];
			ds.mapItemWorldX=new int[gp.maxMap][gp.items[1].length];
			ds.mapItemWorldY=new int[gp.maxMap][gp.items[1].length];
			ds.mapItemLootNames=new String[gp.maxMap][gp.items[1].length];
			ds.mapItemOpened=new boolean[gp.maxMap][gp.items[1].length];
			
			for(int mapNum=0;mapNum<gp.maxMap;mapNum++) {
				
				for(int i=0;i<gp.items[1].length;i++) {
					
					if(gp.items[mapNum][i]==null) {
						
						ds.mapItemNames[mapNum][i]="NA";
						
					}else {
						
						ds.mapItemNames[mapNum][i]=gp.items[mapNum][i].name;
						ds.mapItemWorldX[mapNum][i]=gp.items[mapNum][i].worldX;
						ds.mapItemWorldY[mapNum][i]=gp.items[mapNum][i].worldY;
						
						//Check if the item has loot
						if(gp.items[mapNum][i].loot!=null) {
							
							ds.mapItemLootNames[mapNum][i]=gp.items[mapNum][i].loot.name;
							
						}
						ds.mapItemOpened[mapNum][i]=gp.items[mapNum][i].opened;
						
					}
					
				}
				
			}
			
			
			//Write the DataStorage object
			oos.writeObject(ds);
			oos.close();
			
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
			
		}		
		
	}
	
	
	//Loads the current game data
	public void load() {
		
		try {
			
			ObjectInputStream ois=new ObjectInputStream(new FileInputStream(new File("save.dat")));
			
			//Read the DataStorage object
			DataStorage ds=(DataStorage)ois.readObject();
			
			gp.player.level=ds.level;
			gp.player.maxLife=ds.maxLife;
			gp.player.currentLife=ds.currentLife;
			gp.player.naturalAttack=ds.naturalAttack;
			gp.player.naturalDefense=ds.naturalDefense;
			gp.player.money=ds.money;
			gp.player.exp=ds.exp;
			gp.player.nextLevelExp=ds.nextLevelExp;
			gp.player.attack=ds.attack;
			gp.player.defense=ds.defense;
			
			//Player inventory
			gp.player.inventory.clear();
			for(int i=0;i<ds.itemNames.size();i++) {
				
				gp.player.inventory.add(gp.eg.getItem(ds.itemNames.get(i)));
				gp.player.inventory.get(i).amount=ds.itemAmounts.get(i);
				
			}
			
			//Player equipment
			if(ds.currentWeaponSlot!=-1) gp.player.currentWeapon=gp.player.inventory.get(ds.currentWeaponSlot);
			if(ds.currentShieldSlot!=-1) gp.player.currentShield=gp.player.inventory.get(ds.currentShieldSlot);
			if(ds.currentShoesSlot!=-1) gp.player.currentShoes=gp.player.inventory.get(ds.currentShoesSlot);
			if(ds.currentLightSlot!=-1) gp.player.currentLight=gp.player.inventory.get(ds.currentLightSlot);
			gp.player.lightUpdated=true;
			gp.player.getAttack();
			gp.player.getDefense();
			gp.player.getSpeed();
			
			//Items on map
			for(int mapNum=0;mapNum<gp.maxMap;mapNum++) {
				
				for(int i=0;i<gp.items[1].length;i++) {
					
					if(ds.mapItemNames[mapNum][i].equals("NA")) {
						
						gp.items[mapNum][i]=null;
						
					}else {
						
						gp.items[mapNum][i]=gp.eg.getItem(ds.mapItemNames[mapNum][i]);
						gp.items[mapNum][i].worldX=ds.mapItemWorldX[mapNum][i];
						gp.items[mapNum][i].worldY=ds.mapItemWorldY[mapNum][i];
						
						if(ds.mapItemLootNames[mapNum][i]!=null) {
							
							gp.items[mapNum][i].setLoot(gp.eg.getItem(ds.mapItemLootNames[mapNum][i]));
							
						}
						
						gp.items[mapNum][i].opened=ds.mapItemOpened[mapNum][i];
						if(gp.items[mapNum][i].opened) {gp.items[mapNum][i].down1=gp.items[mapNum][i].image2;}
						
					}
					
				}
				
			}
			
			ois.close();
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	
}



































