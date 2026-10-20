package item;

import entity.Entity;
import entity.TYPE;
import main.GamePanel;

public class ITM_Coin extends Entity{

	GamePanel gp;
	public static final String itemName="Coin";
	
	public ITM_Coin(GamePanel gp) {
		super(gp);
		
		this.gp=gp;
		this.type=TYPE.PICKUPITEM;
		this.name=itemName;
		this.down1=setup("/items/coin.png", gp.tileSize, gp.tileSize);
		this.value=1;
		this.price=1;
		
	}
	
	
	//Effect of the coin
	public boolean use(Entity entity) {
		
		gp.playSound("coin");
		
		gp.player.money+=value;
				
		return true;
		
	}	
	
}
