package data;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//PLAYER STATUS
	int level;
	int maxLife;
	int currentLife;
	int naturalAttack; //Maybe I could get this int by level calculation
	int naturalDefense;
	int money;
	int exp;
	int nextLevelExp;
	int attack;
	int defense;
	
	//Player inventory
	ArrayList<String> itemNames=new ArrayList<>();
	ArrayList<Integer> itemAmounts=new ArrayList<>();
	int currentWeaponSlot;
	int currentShieldSlot;
	int currentLightSlot;
	int currentShoesSlot;
	
	//Items on the map
	String mapItemNames[][];
	int mapItemWorldX[][];
	int mapItemWorldY[][];
	String mapItemLootNames[][]; //Chest
	boolean mapItemOpened[][]; //Chest
	
	
}



















