package enemies;

import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ENM_Vega extends Entity{
	/*
	 * Vega c:
	 */
	
	
	public ENM_Vega(GamePanel gp) {
		super(gp);
		
		this.name="Vega";
		this.level=3;
		this.worldSpeed=1;
		this.maxLife=33;
		this.currentLife=maxLife;
		this.maxMana=33;
		this.currentMana=33;
		this.naturalAttack=3;
		this.naturalDefense=3;
		this.naturalSpeed=3;
		this.attack=getAttack();
		this.defense=getDefense();
		this.rpgSpeed=getSpeed();
		
		this.hasCollisions=true;
		this.type=TYPE.ENEMY;
		this.exp=13;
		this.money=3;
		
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
		
		up1=setup("/enemies/vegaDown1.png");
		up2=setup("/enemies/vegaDown2.png");
		right1=setup("/enemies/vegaDown1.png");
		right2=setup("/enemies/vegaDown2.png");
		left1=setup("/enemies/vegaDown1.png");
		left2=setup("/enemies/vegaDown2.png");
		down1=setup("/enemies/vegaDown1.png");
		down2=setup("/enemies/vegaDown2.png");
		
	}
	
	
	public void setDialogue() {
		

		
	}
	
	
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