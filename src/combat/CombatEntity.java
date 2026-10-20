package combat;

import java.util.ArrayList;

import entity.Entity;

public class CombatEntity {

	public String name;
	public int maxLife;
	public int currentLife;
	public int maxMana;
	public int currentMana;
	public int level;
	public int naturalAttack; //Attack value without weapon
	public int naturalDefense; //Defense value without shield
	public int naturalSpeed;
	public int attack;
	public int defense;
	public int rpgSpeed;
	public Entity currentWeapon;
	public Entity currentShield;
	public Entity currentShoes;
	
	public ArrayList<Integer> attackBoost;
	public ArrayList<Integer> defenseBoost;
	public ArrayList<Integer> speedBoost;
	
	
	public CombatEntity(String name, int maxLife, int currentLife, int maxMana, int currentMana, int level, int naturalAttack, int naturalDefense, int naturalSpeed,
			Entity currentWeapon, Entity currentShield, Entity currentShoes) {
		
		this.name=name;
		this.maxLife=maxLife;
		this.currentLife=currentLife;
		this.maxMana=maxMana;
		this.currentMana=currentMana;
		this.level=level;
		this.naturalAttack=naturalAttack;
		this.naturalDefense=naturalDefense;
		this.naturalSpeed=naturalSpeed;
		this.currentWeapon=currentWeapon;
		this.currentShield=currentShield;
		this.currentShoes=currentShoes;
		
		this.attack=getAttack();
		this.defense=getDefense();
		this.rpgSpeed=getSpeed();
		
		this.attackBoost=new ArrayList<>();
		this.defenseBoost=new ArrayList<>();
		this.speedBoost=new ArrayList<>();
		
	}
	
	
	//Returns the entity's attack (taking account his weapon)
	public int getAttack() {
		
		if(this.currentWeapon==null) {
			
			return this.naturalAttack;
			
		}
		
		return this.naturalAttack+this.currentWeapon.itemAttack;
		
	}
	
	
	//Returns the entity's defense (taking account his shield)
	public int getDefense() {
		
		if(this.currentShield==null) {
			
			return this.naturalDefense;
			
		}
		
		return this.naturalDefense+this.currentShield.itemDefense;
		
	}
	
	
	//Returns the entity's speed (taking account his shoes)
	public int getSpeed() {
		
		if(this.currentShoes==null) {
			
			return this.naturalSpeed;
			
		}
		
		return this.naturalSpeed+this.currentShoes.itemSpeed;
		
	}
	
	
	
	
}




























