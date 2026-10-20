package main;

import entity.Entity;
import item.ITM_8potion;
import item.ITM_Axe;
import item.ITM_BetterShield;
import item.ITM_Boots;
import item.ITM_Chest;
import item.ITM_Coin;
import item.ITM_Door;
import item.ITM_Key;
import item.ITM_Lantern;
import item.ITM_Revolo;
import item.ITM_Shield;
import item.ITM_Sword;
import item.ITM_Tent;

public class EntityGenerator {
	/*
	 * This class has one purpose
	 * It receives a name and converts it into an item
	 */	
	
	GamePanel gp;
	
	
	public EntityGenerator(GamePanel gp) {
		this.gp=gp;
		
		
	}
	
	
	//Creates an item entity from a name
	public Entity getItem(String itemName) {
		
		Entity item=null;
		
		switch(itemName) {
		
		case ITM_8potion.itemName: item=new ITM_8potion(gp); break;
		case ITM_Axe.itemName: item=new ITM_Axe(gp); break;
		case ITM_BetterShield.itemName: item=new ITM_BetterShield(gp); break;
		case ITM_Boots.itemName: item=new ITM_Boots(gp); break;
		case ITM_Chest.itemName: item=new ITM_Chest(gp); break;
		case ITM_Coin.itemName: item=new ITM_Coin(gp); break;
		case ITM_Door.itemName: item=new ITM_Door(gp); break;
		case ITM_Key.itemName: item=new ITM_Key(gp); break;
		case ITM_Lantern.itemName: item=new ITM_Lantern(gp); break;
		case ITM_Revolo.itemName: item=new ITM_Revolo(gp); break;
		case ITM_Shield.itemName: item=new ITM_Shield(gp); break;
		case ITM_Sword.itemName: item=new ITM_Sword(gp); break;
		case ITM_Tent.itemName: item=new ITM_Tent(gp); break;
		
		}
		
		return item;
		
	}
	
	
}






































