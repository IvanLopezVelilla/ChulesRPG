package tile;

import java.awt.image.BufferedImage;

public class Tile {
	/*
	 * Tiles of the game world
	 */
	
	public BufferedImage image;
	public boolean collision=false; //Determines if a tile has collision (if true, the player can't go over it)
	
}
