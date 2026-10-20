package enemies;

import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ENM_BlueGuy extends Entity{
	/*
	 * Blue thing I made in 5 minutes
	 */
	
	
	public ENM_BlueGuy(GamePanel gp) {
		super(gp);
		
		this.name="Blue guy";
		this.level=1;
		this.worldSpeed=1;
		this.maxLife=20;
		this.currentLife=maxLife;
		this.maxMana=30;
		this.currentMana=30;
		this.naturalAttack=2;
		this.naturalDefense=1;
		this.naturalSpeed=1;
		this.attack=getAttack();
		this.defense=getDefense();
		this.rpgSpeed=this.naturalSpeed;
		
		this.hasCollisions=true;
		this.type=TYPE.ENEMY;
		this.exp=7;
		this.money=4;
		
		this.solidArea.x=3;
		this.solidArea.y=18;
		this.solidArea.width=42;
		solidArea.height=30;
		this.solidAreaDefaultX=solidArea.x;
		this.solidAreaDefaultY=solidArea.y;
		
		getImage();
		
	}
	
	
	//Gets the enemy image
	public void getImage() {
		
		up1=setup("/enemies/enemy1Down1.png");
		up2=setup("/enemies/enemy1Down2.png");
		right1=setup("/enemies/enemy1Down1.png");
		right2=setup("/enemies/enemy1Down2.png");
		left1=setup("/enemies/enemy1Down1.png");
		left2=setup("/enemies/enemy1Down2.png");
		down1=setup("/enemies/enemy1Down1.png");
		down2=setup("/enemies/enemy1Down2.png");
		
	}
	
	
	public void setDialogue() {}
	
	
	
	
	//Character specific actions
	public void setAction() {
		
		if(onPath) {
			
			//Check if it stops chasing
			checkStopChasing(gp.player, 5, 100);
			
			//Search the direction to go
			worldSpeed=2;			
			searchPath(getGoalCol(gp.player), getGoalRow(gp.player));
			
		}else {
			
			worldSpeed=1;
			
			//Check if it starts chasing
			checkStartChasing(gp.player, 5, 50);
			
			//Get a random direction
			getRandomDirection();
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
