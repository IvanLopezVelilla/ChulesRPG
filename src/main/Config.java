package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

	GamePanel gp;
	
	public Config(GamePanel gp) {
		
		this.gp=gp;
		
	}
	
	
	//Saves the current game configurations
	public void saveConfig() {
		
		try {
			
			BufferedWriter bw=new BufferedWriter(new FileWriter("config.txt"));
			
			//Full screen
			if(gp.fullScreen) {
				
				bw.write('1');
				
			}else {
				
				bw.write('0');
				
			}
			
			bw.newLine();
			
			//Music volume
			bw.write(String.valueOf(gp.music.volumeScale));
			bw.newLine();
			
			//SE volume
			bw.write(String.valueOf(gp.se.volumeScale));
			bw.newLine();
			
			bw.close();			
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	
	//Loads the stored game configurations
	public void loadConfig() {
		
		try {
			
			BufferedReader br=new BufferedReader(new FileReader("config.txt"));
			
			String aux=br.readLine();
			
			//Full screen
			if(aux.equals("1")) {
				
				gp.fullScreen=true;
				
			}else {
				
				gp.fullScreen=false;
				
			}
			
			//Music
			aux=br.readLine();
			gp.music.volumeScale=Integer.parseInt(aux);
			
			//SE
			aux=br.readLine();
			gp.se.volumeScale=Integer.parseInt(aux);
			
			br.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	
}



















