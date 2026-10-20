package database;

import java.util.ArrayList;
import java.util.Random;

public class DatabaseManager {

    AssetService service;
    public ImageLoader loader;
    public ArrayList<Asset> assets;

    
    public DatabaseManager() {
    	
    	this.service=new AssetService();
    	this.loader=new ImageLoader();
    	this.assets=service.getAssets();
    	
    }
    
    
    //Returns a random asset
    public Asset getRandomAsset() {
    	
    	Random random=new Random();
    	
    	int index=random.nextInt(assets.size());
    	if(assets==null || assets.isEmpty()) return null;
        return assets.get(index);
    	
    }
    
	
}
